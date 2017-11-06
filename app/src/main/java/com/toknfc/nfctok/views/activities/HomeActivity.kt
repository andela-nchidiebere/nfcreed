package com.toknfc.nfctok.views.activities

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.toknfc.nfctok.R
import com.toknfc.nfctok.R.layout
import com.toknfc.nfctok.core.BasicFragmentManager
import com.toknfc.nfctok.core.CoreActivity
import com.toknfc.nfctok.core.MIME_TYPE
import com.toknfc.nfctok.presenters.HomeActivityPresenter
import com.toknfc.nfctok.views.fragments.HomeFragment
import com.toknfc.nfctok.views.fragments.WriteToNfcTagFragment
import kotlinx.android.synthetic.main.activity_home.root
import java.io.IOException


class HomeActivity : CoreActivity(), HomeActivityPresenter.View {

  private val presenter: HomeActivityPresenter by lazy {
    HomeActivityPresenter(this)
  }
  private val fm: BasicFragmentManager by lazy {
    BasicFragmentManager(supportFragmentManager, R.id.activityHomeFlContainer)
  }

  private fun getSnackbar(message: String): Snackbar {
    return Snackbar.make(root, message, Snackbar.LENGTH_LONG)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_home)
  }

  override fun onStart() {
    super.onStart()
    presenter.init()
  }

  override fun showHomeScreen() {
    fm.replaceFragment(HomeFragment.getInstance())
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
        val type = intent.type
        if (type == MIME_TYPE) {
          intent.apply {
            val tag2 = Ndef.get(getParcelableExtra(NfcAdapter.EXTRA_TAG))
            try {
              tag2.connect()
            } catch (ioe: IOException) {
              Toast.makeText(this@HomeActivity, "Error connecting for NFC Tag", Toast
                  .LENGTH_LONG).show()
              return
            }
            tag2.cachedNdefMessage.records
                .forEach {message ->
                  presenter.handlePayloadFromNfcTag(String(message.payload), message.id)
                }
          }
        } else {
          getSnackbar("Wrong mime type").show()
        }
      }
      NfcAdapter.ACTION_TECH_DISCOVERED -> {
        intent.apply {
          val rawMessages = getParcelableArrayListExtra<Parcelable>(NfcAdapter.EXTRA_NDEF_MESSAGES)
          try {
            val ndefMessage = checkNotNull(rawMessages) {"RawMessages is null"}
            val message: NdefMessage = ndefMessage[0] as NdefMessage
            getSnackbar("Discovered NFC Tech with ${message.records.size} records").show()
          } catch (ile: IllegalStateException) {
            getSnackbar("Tag is empty").setAction("Write to Tag?", {showWriteToNfcTagScreen()})
          }
        }
      }
    }
  }

}
