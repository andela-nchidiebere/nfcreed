package com.toknfc.nfctok.presenters

import com.toknfc.nfctok.core.FragmentPresenter

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class HomeFragmentPresenter(private val view: View) : FragmentPresenter {

  override fun dispose() {

  }

  fun initViewListeners() {
    view.initializeListeners()
  }

  fun validateNfcDevice() {
    try {
      if (!view.isNfcCapable()) {
        view.toastNotice("NFC is deactivated")
      }
    } catch (il: IllegalAccessException) {
      view.dismissProgress(0)
      view.closeApp()
    }
  }

  fun scanForNfcTag(): Boolean {
    view.showSearchForTagProgress() // show progress indicating searching for tag and tell user
    // to bring device closer.


    return false
  }

  fun readNfcTag(): Boolean {
    return true
  }

  interface View: FragmentPresenter.View {

    fun startReadingNfcTag()

    fun showSearchForTagProgress()

    fun showReadingFromTagProgress()

    fun dismissProgress(progressId: Int)

    fun showReadTagInfoScreen()

    fun showWriteToNfcTagScreen()

    fun initializeListeners()

    fun isNfcCapable(): Boolean

    fun closeApp()

    fun toastNotice(message: String)

  }

}