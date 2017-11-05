package com.toknfc.nfctok.core

/**
 * Created by Chidi Justice
 *
 */
interface Presenter {

  fun dispose()

  interface View {

    fun handleError(throwable: Throwable)
  }
}