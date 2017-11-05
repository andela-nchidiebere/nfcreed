package com.toknfc.nfctok.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toknfc.nfctok.R
import com.toknfc.nfctok.core.CoreFragment
import com.toknfc.nfctok.core.HOME_FRAGMENT_TAG
import com.toknfc.nfctok.presenters.HomeFragmentPresenter

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class HomeFragment: CoreFragment(), HomeFragmentPresenter.View {



  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun getName(): String {
    return HOME_FRAGMENT_TAG
  }

  override fun handleError(throwable: Throwable) {

  }

  override fun showWriteToNfcTagScreen() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun startReadingNfcTag() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSearchForTagProgress() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showReadingFromTagProgress() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun dismissProgress(progressId: Int) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showReadTagInfoScreen() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    fun getInstance(): HomeFragment {
      return HomeFragment()
    }
  }
}