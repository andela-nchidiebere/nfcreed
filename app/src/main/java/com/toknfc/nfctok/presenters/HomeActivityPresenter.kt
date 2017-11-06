package com.toknfc.nfctok.presenters

import android.content.Intent
import android.nfc.NdefRecord
import android.util.Log
import android.widget.Toast
import com.toknfc.nfctok.core.Presenter
import java.nio.charset.Charset
import kotlin.experimental.and

/**
 * Created by Chidi Justice
 *
 * Presenters should be free of android sdk classes
 */
class HomeActivityPresenter(private val view: View) : Presenter {

  fun init() {
    view.showHomeScreen()
  }

  /**
   * @param payload is the payload from an NdefRecord
   */
  fun readToDatabase(payload: ByteArray): String {
    /**
     * See NFC forum specification for "Text Record Type Definition" at 3.2.1
     * bit_7 defines encoding
     * bit_6 reserved for future use, must be 0
     * bit_5..0 length of IANA language code
     */
    val firstPayloadItem: Byte = payload[0]
    val bitVal = 128
    val payloadTextEncoding: Charset = if (firstPayloadItem.compareTo(
        bitVal.toByte()) == 0) Charsets.UTF_8 else Charsets.UTF_16
    val languageCodeLength: Int = (firstPayloadItem and 51).toInt()
    val text = String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, payloadTextEncoding)
    Log.d("ghjd", text)
    return text
  }

  override fun dispose() {

  }

  interface View {
    fun showHomeScreen()

    fun showWriteToNfcTagScreen()

    fun handleIntent(intent: Intent)
  }
}