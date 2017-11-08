package com.toknfc.nfctok.presenters

import com.toknfc.nfctok.core.FragmentPresenter

/**
 * Created by Chidi Justice
 */
class WriteToNfcTagFragmentPresenter(private val view: View): FragmentPresenter {

  override fun dispose() {

  }

  fun saveMessageToTag() {
    view.shouldSaveToTag()
  }

  interface View: FragmentPresenter.View {

    fun shouldSaveToTag()

    fun showReadNfcTag(payload: String)

    fun fetchPayload(): String

    fun showToast(toastMessage: String, length: Int = 1)

    fun createNfcMessage(): Boolean
  }
}