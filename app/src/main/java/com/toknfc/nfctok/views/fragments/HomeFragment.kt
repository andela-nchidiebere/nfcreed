package com.toknfc.nfctok.views.fragments

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.toknfc.nfctok.R
import com.toknfc.nfctok.R.string
import com.toknfc.nfctok.core.CoreFragment
import com.toknfc.nfctok.core.HOME_FRAGMENT_TAG
import com.toknfc.nfctok.core.MIME_TYPE
import com.toknfc.nfctok.presenters.HomeFragmentPresenter
import com.toknfc.nfctok.views.activities.HomeActivity


/**
 * Created by Chidi Justice
 */
class HomeFragment : CoreFragment(), HomeFragmentPresenter.View {

  private var nfcAdapter: NfcAdapter? = null

  private val presenter: HomeFragmentPresenter by lazy {
    HomeFragmentPresenter(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.validateNfcDevice()
  }

  override fun getName(): String {
    return HOME_FRAGMENT_TAG
  }

  override fun onResume() {
    super.onResume()
    setupForeGroundDispatch()
  }

  override fun onPause() {
    super.onPause()
    stopForeGroundDispatch()
  }

  override fun handleError(throwable: Throwable) {
    when (throwable) {
      is RuntimeException -> Toast.makeText(context, getString(string.check_mime_type),
          Toast.LENGTH_SHORT).show()
    }
  }

  override fun isNfcCapable(): Boolean {
    nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    val nfc = requireNotNull(nfcAdapter) {
      Toast.makeText(context, getString(string.no_nfc_adapter), Toast
          .LENGTH_LONG).show()
      return@requireNotNull getString(string.no_nfc_adapter)
    }
    return nfc.isEnabled
  }

  override fun closeApp() {
    (context as HomeActivity).finish()
  }

  override fun toastNotice(message: String) {
    val nonNullView: View = view?: return
    Snackbar.make(nonNullView, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(string.turn_on_nfc), {
          startSettingsIntent()
        }).show()

  }

  private fun startSettingsIntent() {
    val intent = Intent(Settings.ACTION_NFC_SETTINGS)
    startActivity(intent)
  }

  override fun setupForeGroundDispatch() {
    val intent = Intent(activity.applicationContext, activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    val pendingIntent: PendingIntent = PendingIntent.getActivity(activity.applicationContext, 0,
        intent, 0)
    val techList = arrayOf<Array<String>>()
    val filters: Array<IntentFilter> = Array(1) { makeIntentFilter() }
    nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)

  }

  override fun stopForeGroundDispatch() {
    nfcAdapter?.disableForegroundDispatch(activity)
  }

  private fun makeIntentFilter(): IntentFilter {
    val intentFilter = IntentFilter()
    intentFilter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
    intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
    try {
      intentFilter.addDataType(MIME_TYPE)
    } catch (mlf: MalformedMimeTypeException) {
      handleError(RuntimeException())
      throw RuntimeException(getString(string.check_mime_type))
    }
    return intentFilter
  }

  /**
   * Navigates to screen for creating a profile which is to be written to an NFC tag
   */
  override fun showWriteToNfcTagScreen() {
    (context as HomeActivity).showWriteToNfcTagScreen()
  }

  companion object {
    fun getInstance(): HomeFragment {
      return HomeFragment()
    }
  }
}