package com.toknfc.nfctok.extensions

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.view.View
import com.toknfc.nfctok.R.string
import com.toknfc.nfctok.core.NFC_PATH_PREFIX

/**
 * Created by Chidi Justice
 *
 * This can be Unit tested by using a library like Mockito
 */

/**
 * Parses the message from an NFC intent and performs action.
 * @param action A function to be run for each [NdefRecord] in an [NdefMessage]
 */
fun Intent.parseArrayExtra(action: (payload: String) -> Unit) {
  val tag: Array<Parcelable>? = getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
  tag ?: return
  val ndefMessage = tag[0] as NdefMessage
  ndefMessage.records.forEach { message ->
    action(String(message.payload))
  }
}

/**
 * @param context Context for snackbar and accessing strings
 * @param view non null view necessary for snackbar
 * @param action action to be performed when [NdefMessage] is null from check
 * Perfoms the given action if there's no message from a detected NFC intent
 */
fun Intent.parseArrayListExtra(context: Context, view: View, action: () -> Unit) {
  val rawMessages = getParcelableArrayListExtra<Parcelable>(NfcAdapter.EXTRA_NDEF_MESSAGES)
  try {
    val ndefMessage = checkNotNull(rawMessages) { context.getString(string.nullRawMessage) }
    val message: NdefMessage = ndefMessage[0] as NdefMessage
    Snackbar.make(view,
        "${context.getString(string.discoveredNfc)} ${message.records.size} ${context.getString(
            string.records)}", Snackbar.LENGTH_LONG).show()
  } catch (ile: IllegalArgumentException) {
    action()
  }

}

/**
 * Creates an [NdefMessage] of one record from the string. Which it uses as the payload
 */
fun String.toNdefMessage(): NdefMessage {
  val pathPrefix = NFC_PATH_PREFIX
  val nfcRecord = NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE,
      pathPrefix.toByteArray(),
      ByteArray(0), this.toByteArray())
  return NdefMessage(arrayOf(nfcRecord))
}