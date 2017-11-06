package com.toknfc.nfctok.views.fragments

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
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
import com.toknfc.nfctok.presenters.HomeFragmentPresenter
import com.toknfc.nfctok.views.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_home.fragmentHomeBtnRead
import kotlinx.android.synthetic.main.fragment_home.fragmentHomeBtnWrite
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import com.toknfc.nfctok.core.MIME_TYPE


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
    val layoutView = inflater.inflate(R.layout.fragment_home, container, false)
    presenter.validateNfcDevice()
    presenter.initViewListeners()
    return layoutView
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

  override fun initializeListeners() {
    //fragmentHomeBtnRead.setOnClickListener { presenter.scanForNfcTag() }
    //fragmentHomeBtnWrite.setOnClickListener { showWriteToNfcTagScreen() }
  }

  override fun toastNotice(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    val notNullView = view ?: return
    Snackbar.make(notNullView, message, Snackbar.LENGTH_LONG)
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
    //TODO.Hate that I have to handle null case for the adapter. I require it to be not null always
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
      throw RuntimeException("Check your mime type.")
    }
    return intentFilter
  }

  override fun startReadingNfcTag() {
  }

  override fun showSearchForTagProgress() {
  }

  /**
   * When reading an nfc tag (which should be run on background), show this progressbar
   */
  override fun showReadingFromTagProgress() {
  }

  override fun dismissProgress(progressId: Int) {

  }

  /**
   * Navigates to another screen showing information read from nfc tag
   */
  override fun showReadTagInfoScreen() {
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