package com.toknfc.nfctok.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toknfc.nfctok.R
import com.toknfc.nfctok.core.CoreFragment
import com.toknfc.nfctok.core.WRITE_TO_NFC_TAG_FRAGMENT_TAG

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class WriteToNfcTagFragment : CoreFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_write_nfc, container, false)
  }

  override fun getName(): String {
    return WRITE_TO_NFC_TAG_FRAGMENT_TAG
  }

  companion object {
    fun getInstance(): WriteToNfcTagFragment {
      return getInstance()
    }
  }
}