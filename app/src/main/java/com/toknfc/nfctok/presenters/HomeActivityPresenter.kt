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
    //1. navigate to tagInfo screen with the text.
    view.openTagInfoScreen(payload)
    /**
     * Ensure that andela tags isn't saved multiple times. Each tag should have a unique ID,
     * which should be used to distinguish them from each other. For Andela tags, first check
     * that no id with its payload exists, before saving. For new ones, save an id to the tag,
     * which would be used for distinquishing them in the db
     */
    //2. Save payload on the background
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