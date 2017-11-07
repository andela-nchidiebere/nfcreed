package com.toknfc.nfctok.presenters

import com.toknfc.nfctok.core.FragmentPresenter

/**
 * Created by Chidi Justice on 07/11/2017.
 */
class TagInfoFragmentPresenter(private val view: View): FragmentPresenter {

  override fun dispose() {

  }

  fun setTagInfo(message: String) {
    view.setMessage(message)
  }

  interface View: FragmentPresenter.View {

    fun setMessage(info: String)

  }
}