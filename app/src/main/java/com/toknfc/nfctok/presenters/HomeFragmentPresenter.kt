package com.toknfc.nfctok.presenters

import com.toknfc.nfctok.core.FragmentPresenter

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class HomeFragmentPresenter(private val view: View) : FragmentPresenter {


  override fun dispose() {

  }

  //fun load

  interface View: FragmentPresenter.View {

    fun showWriteToNfcTagScreen()

    fun startReadingNfcTag()

    fun showSearchForTagProgress()

    fun showReadingFromTagProgress()

    fun dismissProgress(progressId: Int)

    fun showReadTagInfoScreen()

  }

}