package com.toknfc.nfctok.core

import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Created by Chidi Justice on 05/11/2017.
 */
open class CoreActivity: AppCompatActivity() {


  /*fun onNavigateToFragment(newFragment: CoreFragment, tag: String, container: Int,
      addToBackStack: Boolean, animated: Boolean) {
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()
    var fragment = fragmentManager.findFragmentByTag(tag)
    if (fragment == null) {
      fragment = newFragment
    }
    transaction.replace(container, fragment, tag)
    if (animated) {
      transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    }
    if (addToBackStack) {
      transaction.addToBackStack(tag)
    }
    transaction.commit()
  }*/
}