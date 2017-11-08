package com.toknfc.nfctok.views.fragments

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.toknfc.nfctok.R
import com.toknfc.nfctok.core.CoreFragment
import com.toknfc.nfctok.core.WRITE_TO_NFC_TAG_FRAGMENT_TAG
import com.toknfc.nfctok.exceptions.MessageTooLargeException
import com.toknfc.nfctok.exceptions.NdefNotSupportedException
import com.toknfc.nfctok.exceptions.ReadOnlyTagException
import com.toknfc.nfctok.exceptions.WriteOperationFailedException
import com.toknfc.nfctok.presenters.WriteToNfcTagFragmentPresenter
import com.toknfc.nfctok.views.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_write_nfc.fragmentWriteNfcBtSave
import kotlinx.android.synthetic.main.fragment_write_nfc.fragmentWriteNfcEtFirstName
import kotlinx.android.synthetic.main.fragment_write_nfc.fragmentWriteNfcEtLastName
import kotlinx.android.synthetic.main.fragment_write_nfc.fragmentWriteNfcSpRoles
import java.io.IOException

/**
 * Created by Chidi Justice
 */
class WriteToNfcTagFragment : CoreFragment(), WriteToNfcTagFragmentPresenter.View {

  private val nfcIntent: Intent by lazy {
    (context as HomeActivity).nfcIntent
  }

  private val presenter: WriteToNfcTagFragmentPresenter by lazy {
    WriteToNfcTagFragmentPresenter(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_write_nfc, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fragmentWriteNfcBtSave.setOnClickListener { presenter.saveMessageToTag() }
  }

  override fun getName(): String {
    return WRITE_TO_NFC_TAG_FRAGMENT_TAG
  }

  override fun showToast(toastMessage: String, length: Int) {
    Toast.makeText(context, toastMessage, length).show()
  }

  override fun handleError(throwable: Throwable) {
    when (throwable) {
      is MessageTooLargeException -> showToast("Message too large")
      is ReadOnlyTagException -> showToast("Tag is read only")
      is NdefNotSupportedException -> showToast("Ndef not supported by tag")
      is IOException -> showToast("Failed to format tag")
      is WriteOperationFailedException -> showToast("Write operation faled")
    }
  }

  override fun shouldSaveToTag() {
    createNfcMessage()
  }

  override fun showReadNfcTag(payload: String) {
    (context as HomeActivity).openTagInfoScreen(payload)
  }

  override fun createNfcMessage(): Boolean {
    val pathPrefix = "chidijustice.com:nfcapp"
    val payload = fetchPayload()
    val nfcRecord = NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE,
        pathPrefix.toByteArray(),
        ByteArray(0), payload.toByteArray())
    val nfcMessage = NdefMessage(arrayOf(nfcRecord))
    nfcIntent.apply {
      val tag = getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
      try {
        return writeMessageToTag(nfcMessage, tag)
      } catch (exc: Exception) {
        handleError(exc)
      }
    }
    return false
  }

  private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {
    try {
      val nDefTag = Ndef.get(tag)

      nDefTag?.let {
        it.connect()
        if (it.maxSize < nfcMessage.toByteArray().size) {
          throw MessageTooLargeException("Message too large")
        }
        if (it.isWritable) {
          it.writeNdefMessage(nfcMessage)
          it.close()
          Toast.makeText(context, "Message successfully written to Tag", Toast.LENGTH_LONG).show()
          (context as HomeActivity).navigateBack()
          return true
        } else {
          throw ReadOnlyTagException("Tag is read only")
        }
      }

      val nDefFormatableTag = NdefFormatable.get(tag)

      nDefFormatableTag?.let {
        try {
          it.connect()
          it.format(nfcMessage)
          it.close()
          Toast.makeText(context, "Message successfully written to Tag", Toast.LENGTH_LONG).show()
          return true
        } catch (e: IOException) {
          handleError(e)
          return false
        }
      }
      throw NdefNotSupportedException("Ndef not supported")

    } catch (e: Exception) {
      throw WriteOperationFailedException("Write operation failed")
    }
  }

  //TODO Validate entries
  override fun fetchPayload(): String {
    val firstName = fragmentWriteNfcEtFirstName.text.toString()
    val lastName = fragmentWriteNfcEtLastName.text.toString()
    val role = fragmentWriteNfcSpRoles.selectedItem.toString()
    return "First name: $firstName\nLast name: $lastName\nRole: $role"
  }

  companion object {
    fun getInstance(): WriteToNfcTagFragment {
      return WriteToNfcTagFragment()
    }
  }
}