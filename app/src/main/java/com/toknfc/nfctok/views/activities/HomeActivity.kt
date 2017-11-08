package com.toknfc.nfctok.views.activities

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import com.toknfc.nfctok.R
import com.toknfc.nfctok.R.layout
import com.toknfc.nfctok.R.string
import com.toknfc.nfctok.core.BUNDLE_TAG_PAYLOAD
import com.toknfc.nfctok.core.BasicFragmentManager
import com.toknfc.nfctok.core.CoreActivity
import com.toknfc.nfctok.core.NFC_BUNDLE_KEY
import com.toknfc.nfctok.presenters.HomeActivityPresenter
import com.toknfc.nfctok.views.fragments.HomeFragment
import com.toknfc.nfctok.views.fragments.TagInformationFragment
import com.toknfc.nfctok.views.fragments.WriteToNfcTagFragment
import kotlinx.android.synthetic.main.activity_home.root


class HomeActivity : CoreActivity(), HomeActivityPresenter.View {

  private val presenter: HomeActivityPresenter by lazy {
    HomeActivityPresenter(this)
  }
  private val fm: BasicFragmentManager by lazy {
    BasicFragmentManager(supportFragmentManager, R.id.activityHomeFlContainer)
  }

  lateinit var nfcIntent: Intent

  private fun getSnackbar(message: String): Snackbar {
    return Snackbar.make(root, message, Snackbar.LENGTH_LONG)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_home)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putParcelable(NFC_BUNDLE_KEY, nfcIntent)
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 1) {
      fm.popUp()
    } else {
      finish()
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    nfcIntent = savedInstanceState.getParcelable(NFC_BUNDLE_KEY)
  }

  fun navigateBack() {
    fm.popUp()
  }

  override fun onStart() {
    super.onStart()
    presenter.init()
  }

  override fun showHomeScreen() {
    fm.replaceFragment(HomeFragment.getInstance())
  }

  override fun openTagInfoScreen(payload: String) {
    val bundle = Bundle()
    bundle.putString(BUNDLE_TAG_PAYLOAD, payload)
    fm.replaceFragment(TagInformationFragment.getInstance(bundle))
  }

  override fun showWriteToNfcTagScreen() {
    fm.replaceFragment(WriteToNfcTagFragment.getInstance())
  }

  override fun onNewIntent(intent: Intent) {
    handleIntent(intent)
  }

  override fun handleIntent(intent: Intent) {
    val action = intent.action
    when (action) {
      NfcAdapter.ACTION_NDEF_DISCOVERED -> {
        intent.apply {
          val tag2: Array<Parcelable>? = getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
          tag2 ?: return
          val ndefMessage = tag2[0] as NdefMessage
          ndefMessage.records.forEach { message ->
            presenter.handlePayloadFromNfcTag(String(message.payload), message.id)
          }
        }
      }
      NfcAdapter.ACTION_TECH_DISCOVERED -> {
        intent.apply {
          val rawMessages = getParcelableArrayListExtra<Parcelable>(NfcAdapter.EXTRA_NDEF_MESSAGES)
          try {
            val ndefMessage = checkNotNull(rawMessages) { getString(string.nullRawMessage) }
            val message: NdefMessage = ndefMessage[0] as NdefMessage
            getSnackbar("${getString(string.discoveredNfc)} ${message.records.size} ${getString
            (string.records)}")
                .show()
          } catch (ile: IllegalStateException) {
            nfcIntent = this
            showWriteToNfcTagScreen()
          }
        }
      }
    }
  }

}
