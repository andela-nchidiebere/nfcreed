package com.toknfc.nfctok.presenters

import android.content.Intent
import com.toknfc.nfctok.core.Presenter

/**
 * Created by Chidi Justice
 **/
class HomeActivityPresenter(private val view: View) : Presenter {

  fun init() {
    view.showHomeScreen()
  }

  fun handlePayloadFromNfcTag(payload: String, hasId: ByteArray?) {
    view.openTagInfoScreen(payload)
  }

  override fun dispose() {

  }

  interface View {
    fun showHomeScreen()

    fun showWriteToNfcTagScreen()

    fun openTagInfoScreen(payload: String)

    fun handleIntent(intent: Intent)
  }
}