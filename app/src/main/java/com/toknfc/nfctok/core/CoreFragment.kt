package com.toknfc.nfctok.core

import android.support.v4.app.Fragment

/**
 * Created by Chidi Justice
 */
open class CoreFragment: Fragment(), InfoFragment {

  private val DEFAULT: String = ""

  override fun getName(): String {
    return DEFAULT
  }
}