package com.toknfc.nfctok.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toknfc.nfctok.R
import com.toknfc.nfctok.core.CoreFragment

/**
 * Created by Chidi Justice
 */
class TagInformationFragment: CoreFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_tag_info, container, false)
  }


  companion object {
    private var tagInfoFragment: TagInformationFragment? = null

    fun getInstance(): TagInformationFragment? {
      if (tagInfoFragment == null) {
        tagInfoFragment = TagInformationFragment()
      }
      return tagInfoFragment
    }
  }

}