package com.toknfc.nfctok.core

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NfcAdapter
import android.support.v4.app.Fragment
import android.widget.Toast
import com.toknfc.nfctok.R.string

/**
 * Created by Chidi Justice
 */
open class CoreFragment: Fragment(), InfoFragment {

  private val DEFAULT: String = ""

  var nfcAdapter: NfcAdapter? = null

  override fun getName(): String {
    return DEFAULT
  }

  override fun onResume() {
    super.onResume()
    setupForeGroundDispatch()
  }

  override fun onPause() {
    super.onPause()
    stopForeGroundDispatch()
  }

  fun setupForeGroundDispatch() {
    val intent = Intent(activity.applicationContext, activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    val pendingIntent: PendingIntent = PendingIntent.getActivity(activity.applicationContext, 0,
        intent, 0)
    val techList = arrayOf<Array<String>>()
    val filters: Array<IntentFilter> = Array(1) { makeIntentFilter() }
    nfcAdapter?.let {
      it.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

  }

  private fun makeIntentFilter(): IntentFilter {
    val intentFilter = IntentFilter()
    intentFilter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
    intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
    try {
      intentFilter.addDataType(MIME_TYPE)
    } catch (mlf: MalformedMimeTypeException) {
      handleRuntimeError(RuntimeException())
      throw RuntimeException(getString(string.check_mime_type))
    }
    return intentFilter
  }

  fun stopForeGroundDispatch() {
    nfcAdapter?.let {
      it.disableForegroundDispatch(activity)
    }
  }

  fun handleRuntimeError(throwable: Throwable) {
    when (throwable) {
      is RuntimeException -> Toast.makeText(context, getString(string.check_mime_type),
          Toast.LENGTH_SHORT).show()
    }
  }
}