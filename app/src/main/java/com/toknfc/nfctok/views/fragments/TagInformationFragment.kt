package com.toknfc.nfctok.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toknfc.nfctok.R
import com.toknfc.nfctok.core.BUNDLE_TAG_PAYLOAD
import com.toknfc.nfctok.core.CoreFragment
import com.toknfc.nfctok.core.EMPTY_STRING
import com.toknfc.nfctok.presenters.TagInfoFragmentPresenter
import kotlinx.android.synthetic.main.fragment_tag_info.fragmentTagInfoContent

/**
 * Created by Chidi Justice
 */
class TagInformationFragment: CoreFragment(), TagInfoFragmentPresenter.View {

  private val payload: String by lazy {
    val args = arguments ?: return@lazy EMPTY_STRING
    args.getString(BUNDLE_TAG_PAYLOAD, EMPTY_STRING)
  }

  private val presenter: TagInfoFragmentPresenter by lazy {
    TagInfoFragmentPresenter(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_tag_info, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setTagInfo(payload)
  }

  override fun setMessage(info: String) {
    fragmentTagInfoContent.text = info
  }

  override fun handleError(throwable: Throwable) {

  }

  companion object {
    private var tagInfoFragment: TagInformationFragment? = null

    fun getInstance(bundle: Bundle?): TagInformationFragment? {
      if (tagInfoFragment == null) {
        tagInfoFragment = TagInformationFragment()
      }
      bundle.let {
        tagInfoFragment?.arguments = it
      }
      return tagInfoFragment
    }
  }

}