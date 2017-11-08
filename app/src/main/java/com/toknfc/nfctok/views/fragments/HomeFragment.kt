package com.toknfc.nfctok.views.fragments

import android.content.Intent
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
import com.toknfc.nfctok.presenters.HomeFragmentPresenter
import com.toknfc.nfctok.views.activities.HomeActivity


/**
 * Created by Chidi Justice
 */
class HomeFragment : CoreFragment(), HomeFragmentPresenter.View {

  private val presenter: HomeFragmentPresenter by lazy {
    HomeFragmentPresenter(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun handleError(throwable: Throwable) {
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.validateNfcDevice()
  }

  override fun getName(): String {
    return HOME_FRAGMENT_TAG
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

  override fun showWriteToNfcTagScreen() {
    (context as HomeActivity).showWriteToNfcTagScreen()
  }

  companion object {
    fun getInstance(): HomeFragment {
      return HomeFragment()
    }
  }
}