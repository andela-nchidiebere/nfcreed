package com.toknfc.nfctok.presenters

import com.toknfc.nfctok.core.Presenter

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class HomeActivityPresenter(private val view: View): Presenter {

  fun init() {
    view.showHomeScreen()
  }

  override fun dispose() {

  }

  interface View {
    fun showHomeScreen()

    fun showWriteToNfcTagScreen()
  }
}