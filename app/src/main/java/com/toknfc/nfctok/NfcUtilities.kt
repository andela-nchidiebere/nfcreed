package com.toknfc.nfctok

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.widget.Toast
import com.toknfc.nfctok.R.string
import com.toknfc.nfctok.exceptions.MessageTooLargeException
import com.toknfc.nfctok.exceptions.NdefNotSupportedException
import com.toknfc.nfctok.exceptions.ReadOnlyTagException
import com.toknfc.nfctok.exceptions.WriteOperationFailedException
import com.toknfc.nfctok.views.activities.HomeActivity
import java.io.IOException

/**
 * Created by Chidi Justice on 14/11/2017.
 */
class NfcUtilities {

  companion object {
    fun writeMessageToTag(
        nfcMessage: NdefMessage,
        tag: Tag?,
        context: Context,
        onErrorAction: (Exception) -> Unit): Boolean {
      try {
        val nDefTag = Ndef.get(tag)
        nDefTag?.let {
          it.connect()
          if (it.maxSize < nfcMessage.toByteArray().size) {
            throw MessageTooLargeException(context.getString(string.message_too_large))
          }
          if (it.isWritable) {
            it.writeNdefMessage(nfcMessage)
            it.close()
            Toast.makeText(context, context.getString(string.message_written_successfully), Toast
                .LENGTH_LONG).show()
            (context as HomeActivity).navigateBack()
            return true
          } else {
            throw ReadOnlyTagException(context.getString(string.read_only_tag))
          }
        }
        val nDefFormatableTag = NdefFormatable.get(tag)
        nDefFormatableTag?.let {
          try {
            it.connect()
            it.format(nfcMessage)
            it.close()
            Toast.makeText(context, context.getString(string.message_written_successfully), Toast
                .LENGTH_LONG).show()
            return true
          } catch (e: IOException) {
            onErrorAction(e)
            return false
          }
        }
        throw NdefNotSupportedException(context.getString(string.ndef_not_supported))
      } catch (e: Exception) {
        throw WriteOperationFailedException(context.getString(string.write_operation_failed))
      }
    }
  }
}