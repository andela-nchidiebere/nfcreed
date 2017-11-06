package com.toknfc.nfctok.presenters

import android.content.Intent
import com.toknfc.nfctok.core.Presenter

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
   * From the app, it is designed to handle tags written to from the app, or Andela Tags.
   * Andela tags has a single record written to it, in my db, it'll be my ID.
   *
   * Some edge cases, In a case where the same record is inserted twice
   */
  fun handlePayloadFromNfcTag(payload: String, hasId: ByteArray?) {

  }

  override fun dispose() {

  }

  interface View {
    fun showHomeScreen()

    fun showWriteToNfcTagScreen()

    fun handleIntent(intent: Intent)
  }
}