package com.toknfc.nfctok.presenters

import com.toknfc.nfctok.core.FragmentPresenter
import com.toknfc.nfctok.core.NFC_DEACTIVATED_MESSAGE

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class HomeFragmentPresenter(private val view: View) : FragmentPresenter {

  override fun dispose() {

  }

  fun validateNfcDevice() {
    try {
      if (!view.isNfcCapable()) {
        view.toastNotice(NFC_DEACTIVATED_MESSAGE)
      }
    } catch (il: IllegalArgumentException) {
      view.closeApp()
    }
  }

  interface View: FragmentPresenter.View {


    fun showWriteToNfcTagScreen()

    fun isNfcCapable(): Boolean

    fun closeApp()

    fun toastNotice(message: String)

    fun setupForeGroundDispatch()

    fun stopForeGroundDispatch()

  }

}