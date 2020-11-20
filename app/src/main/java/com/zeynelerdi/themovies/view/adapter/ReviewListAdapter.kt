
package com.zeynelerdi.themovies.view.adapter

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.SectionRow
import com.zeynelerdi.themovies.R
import com.zeynelerdi.themovies.models.Resource
import com.zeynelerdi.themovies.models.Review
import com.zeynelerdi.themovies.view.viewholder.ReviewListViewHolder
import com.skydoves.whatif.whatIfNotNull

class ReviewListAdapter : BaseAdapter() {

  init {
    addSection(ArrayList<Review>())
  }

  fun addReviewList(resource: Resource<List<Review>>) {
    resource.data.whatIfNotNull {
      sections()[0].addAll(it)
      notifyDataSetChanged()
    }
  }

  override fun layout(sectionRow: SectionRow) = R.layout.item_review

  override fun viewHolder(layout: Int, view: View) = ReviewListViewHolder(view)
}
