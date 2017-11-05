package com.toknfc.nfctok.core

import android.support.v4.app.FragmentManager

/**
 * Created by Chidi Justice on 05/11/2017.
 */
class BasicFragmentManager(private val fragmentManager: FragmentManager,
    private val fragmentContainerId: Int) {
  // TODO: Could implement a back stack I can manage later

  fun addFragment(fragment: CoreFragment) {
    fragmentManager.beginTransaction()
        .add(fragmentContainerId, fragment, fragment.getName())
        .addToBackStack(fragment.getName())
        .commit()
  }

  fun replaceFragment(fragment: CoreFragment) {
    fragmentManager.beginTransaction()
        .replace(fragmentContainerId, fragment, fragment.getName())
        .addToBackStack(fragment.getName())
        .commit()
  }

  /*fun onBackPressed(): Boolean {
    if (fragmentManager.backStackEntryCount > 1) {
      popUp()
      val currentFragment = getCurrentFragment()
      if (currentFragment != null) {
        currentFragment!!.setTitle()
        return true
      }
    }

    return false
  }*/

  /**
   * Getting current activity loaded in the activity
   */
  /*fun getCurrentFragment(): CoreFragment? {
    val fragmentByTag = fragmentManager.findFragmentByTag(fragmentTagStack.getActiveTag())
    return if (fragmentByTag != null) {
      fragmentByTag as IFragment
    } else {
      null
    }
  }*/

  fun popUp() {
    fragmentManager.popBackStackImmediate()
  }
}