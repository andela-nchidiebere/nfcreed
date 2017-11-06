package com.toknfc.nfctok.views.fragments

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

  override fun handleError(throwable: Throwable) {

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
    fragmentHomeBtnRead.setOnClickListener { presenter.scanForNfcTag() }
    fragmentHomeBtnWrite.setOnClickListener { showWriteToNfcTagScreen() }
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

  override fun startReadingNfcTag() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSearchForTagProgress() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showReadingFromTagProgress() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun dismissProgress(progressId: Int) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showReadTagInfoScreen() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showWriteToNfcTagScreen() {
    (context as HomeActivity).showWriteToNfcTagScreen()
  }

  companion object {
    fun getInstance(): HomeFragment {
      return HomeFragment()
    }
  }
}