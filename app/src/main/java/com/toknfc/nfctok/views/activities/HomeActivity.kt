package com.toknfc.nfctok

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.nfc.NfcManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.buttonAddMessage
import kotlinx.android.synthetic.main.activity_home.txtBoxAddMessage
import kotlinx.android.synthetic.main.activity_home.txtMessageToSend
import kotlinx.android.synthetic.main.activity_home.txtMessagesReceived
import java.lang.IllegalStateException
import java.nio.charset.Charset

class HomeActivity : AppCompatActivity(),
    NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {

  private var messagesToSend: ArrayList<String> = arrayListOf()
  private var receivedMessages: ArrayList<String> = arrayListOf()
  private lateinit var nfcAdapter: NfcAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    val nfcManager: NfcManager = this.getSystemService(Context.NFC_SERVICE) as NfcManager
    if (nfcManager.defaultAdapter != null && nfcManager.defaultAdapter.isEnabled) {
      nfcAdapter = NfcAdapter.getDefaultAdapter(applicationContext)
      nfcAdapter.setNdefPushMessageCallback(this, this)
      nfcAdapter.setOnNdefPushCompleteCallback(this, this)
      buttonAddMessage.setOnClickListener { addMessages() }
    }

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.apply {
      putStringArrayList("messagesToSend", messagesToSend)
      putStringArrayList("messagesReceived", receivedMessages)
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    savedInstanceState.apply {
      messagesToSend = getStringArrayList("messagesToSend")
      receivedMessages = getStringArrayList("messagesReceived")
    }
  }

  private fun addMessages() {
    val message: String = txtBoxAddMessage.text.toString()
    if (message.isNotBlank())
      messagesToSend.add(message)
    txtBoxAddMessage.text = null
    Toast.makeText(this, "Message added", Toast.LENGTH_LONG).show()
  }

  private fun updateViews() {
    txtMessageToSend.text = "Messages to send:\n"
    messagesToSend.forEach { message ->
      txtMessageToSend.append("$message\n")
    }
    txtMessagesReceived.text = "Messages received:\n"
    receivedMessages.forEach { message ->
      txtMessagesReceived.append("$message\n")
    }
  }

  override fun onNdefPushComplete(event: NfcEvent?) {
    messagesToSend.clear()
  }

  override fun createNdefMessage(event: NfcEvent?): NdefMessage {
    // Called when another nfc enabled device is detected
    check(messagesToSend.size > 0) {throw IllegalStateException("Messages list must not be null")}
    val nfcRecords: Array<NdefRecord> = createRecords()
    return NdefMessage(nfcRecords)
  }

  override fun onNewIntent(intent: Intent) {
    handleNfcIntent(intent)
  }

  override fun onResume() {
    super.onResume()
    updateViews()
    handleNfcIntent(intent)
  }

  private fun createRecords(): Array<NdefRecord> {
    return Array(messagesToSend.size + 1, {ind -> mes(ind)} )
  }

  private fun mes(index: Int): NdefRecord {
    if (index == messagesToSend.size) {
      /**
       * Note: This doesn’t make the transaction secure or ensure that my app will be the one to open
       * the record. Including the application record only further specifies our preference to the OS.
       * If another activity that is currently in the foreground calls NfcAdapter.enableForegroundDispatch
       * it can catch the intent before it gets to us, there is no way to prevent this except to have
       * our activity in the foreground. Still, this is as close as we can get to ensuring that
       * our application is the one that processes this data.
       */
      return  NdefRecord.createApplicationRecord(packageName)
    }
    val payload: ByteArray = messagesToSend[index].toByteArray(Charset.forName("UTF-8"))
    return NdefRecord.createMime("text/plain", payload)
  }

  private fun handleNfcIntent(nfcIntent: Intent) {
    if (NfcAdapter.ACTION_NDEF_DISCOVERED == nfcIntent.action) {
      val receivedArray = nfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
      receivedArray?.let {
        receivedMessages.clear()
        val ndefMessage: NdefMessage = it[0] as NdefMessage
        val attachedRecords: Array<NdefRecord> = ndefMessage.records
        attachedRecords.filterNot { record ->
          String(record.payload) == packageName
        }.forEach { rcd ->
          val payload = String(rcd.payload)
          receivedMessages.add(payload)
        }
        Toast.makeText(this, "Received ${receivedMessages.size} messages", Toast.LENGTH_LONG).show()
        updateViews()
      }
    } else {
      Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show()
    }
  }
}
