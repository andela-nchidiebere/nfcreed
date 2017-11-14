package com.toknfc.nfctok.views.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.toknfc.nfctok.R
import com.toknfc.nfctok.R.layout
import com.toknfc.nfctok.core.BUNDLE_TAG_PAYLOAD
import com.toknfc.nfctok.core.BasicFragmentManager
import com.toknfc.nfctok.core.NFC_BUNDLE_KEY
import com.toknfc.nfctok.extensions.parseArrayExtra
import com.toknfc.nfctok.extensions.parseArrayListExtra
import com.toknfc.nfctok.presenters.HomeActivityPresenter
import com.toknfc.nfctok.views.fragments.HomeFragment
import com.toknfc.nfctok.views.fragments.TagInformationFragment
import com.toknfc.nfctok.views.fragments.WriteToNfcTagFragment
import kotlinx.android.synthetic.main.activity_home.root


class HomeActivity : AppCompatActivity(), HomeActivityPresenter.View {

  private val presenter: HomeActivityPresenter by lazy {
    HomeActivityPresenter(this)
  }
  private val fm: BasicFragmentManager by lazy {
    BasicFragmentManager(supportFragmentManager, R.id.activityHomeFlContainer)
  }

  lateinit var nfcIntent: Intent

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
          parseArrayExtra { payload ->
            presenter.handlePayloadFromNfcTag(payload)
          }
        }
      }
      NfcAdapter.ACTION_TECH_DISCOVERED -> {
        intent.apply {
          nfcIntent = this
          parseArrayListExtra(this@HomeActivity, root, {showWriteToNfcTagScreen()})
        }
      }
    }
  }

}
