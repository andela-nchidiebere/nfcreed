package com.toknfc.nfctok.views.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import com.toknfc.nfctok.R
import com.toknfc.nfctok.R.layout
import com.toknfc.nfctok.R.string
import com.toknfc.nfctok.core.BasicFragmentManager
import com.toknfc.nfctok.core.CoreActivity
import com.toknfc.nfctok.core.MIME_TYPE
import com.toknfc.nfctok.presenters.HomeActivityPresenter
import com.toknfc.nfctok.views.fragments.HomeFragment
import com.toknfc.nfctok.views.fragments.WriteToNfcTagFragment
import java.io.UnsupportedEncodingException
import java.util.Arrays
import android.R.attr.tag



class HomeActivity : CoreActivity(), HomeActivityPresenter.View {

  // TODO Refactor: should inject this maybe
  private val presenter: HomeActivityPresenter by lazy {
    HomeActivityPresenter(this)
  }
  private val fm: BasicFragmentManager by lazy {
    BasicFragmentManager(supportFragmentManager, R.id.activityHomeFlContainer)
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_home)
  }

  override fun onStart() {
    super.onStart()
    presenter.init()
  }

  override fun showHomeScreen() {
    fm.replaceFragment(HomeFragment.getInstance())
  }

  override fun showWriteToNfcTagScreen() {
    fm.replaceFragment(WriteToNfcTagFragment.getInstance())
  }

  override fun onNewIntent(intent: Intent) {
    handleIntent(intent)
  }

  override fun handleIntent(intent: Intent) {
    //TODO get current fragment and pass action to it
    val action = intent.action
    when (action) {
      NfcAdapter.ACTION_NDEF_DISCOVERED -> {
        val type = intent.type
        if (type == MIME_TYPE) {
          intent.apply {
            val tag: Tag = getParcelableExtra(NfcAdapter.EXTRA_TAG)
            NdefTask().execute(tag)
          }
        } else {
          Toast.makeText(this, "Wrong mime type", Toast.LENGTH_SHORT).show()
        }
      }
      NfcAdapter.ACTION_TECH_DISCOVERED -> {
        intent.apply {
          val tag: Tag = getParcelableExtra(NfcAdapter.EXTRA_TAG)
          val techList: Array<String> = tag.techList
          val sharedTech = Ndef::class.java.name
          techList.first { tech -> sharedTech == tech }.run {
            NdefTask().execute(tag)
          }
        }
      }
    }

  }

  /**
   * Background task for reading tag to make it non blocking.
   * TODO: Refactor process with Rxjava
   *
   * Intentionally allowing this to exist while activity exist so it is gabbage collected at the
   * same time as when activity is destroyed.
   */
  @SuppressLint("StaticFieldLeak")
  private inner class NdefTask: AsyncTask<Tag, Void, String>() {

    override fun doInBackground(vararg params: Tag): String? {
      val tag: Tag = params[0]
      val ndef: Ndef? = Ndef.get(tag) ?: null
      val ndefSupportedTag = checkNotNull(ndef) {
        Toast.makeText(this@HomeActivity, getString(string.ndef_not_supported), Toast
            .LENGTH_LONG).show()
        return@checkNotNull getString(string.ndef_not_supported)
      }
      val ndefMesage: NdefMessage = ndefSupportedTag.cachedNdefMessage
      val ndefRecords: Array<NdefRecord> = ndefMesage.records
      ndefRecords.filter { record ->
        record.tnf == NdefRecord.TNF_WELL_KNOWN &&
            Arrays.equals(record.type, NdefRecord.RTD_TEXT)
      }.map { record ->
        return presenter.readToDatabase(record.payload)
      }

      return null
    }

    override fun onPostExecute(result: String?) {
      result?.let {
        Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
      }
    }


  }

  /*override fun onSaveInstanceState(outState: Bundle) {
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

  *//*override fun onNewIntent(intent: Intent) {
    handleNfcIntent(intent)
  }*//*

  *//*override fun onResume() {
    super.onResume()
    updateViews()
    handleNfcIntent(intent)
  }*//*

  private fun createRecords(): Array<NdefRecord> {
    return Array(messagesToSend.size + 1, {ind -> mes(ind)} )
  }

  private fun mes(index: Int): NdefRecord {
    if (index == messagesToSend.size) {
      */
  /**
   * Note: This doesn’t make the transaction secure or ensure that my app will be the one to open
   * the record. Including the application record only further specifies our preference to the OS.
   * If another activity that is currently in the foreground calls NfcAdapter.enableForegroundDispatch
   * it can catch the intent before it gets to us, there is no way to prevent this except to have
   * our activity in the foreground. Still, this is as close as we can get to ensuring that
   * our application is the one that processes this data.
   *//*
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
  }*/
}
