package com.intoverflown.sasakazi.ui.discussions.mobile

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

class ScrollToBottomObserver (
        private val mRecycler: RecyclerView,
        private val mAdapter: RecyclerView.Adapter<*>,
        private val mManager: LinearLayoutManager) : AdapterDataObserver() {
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        val count = mAdapter.itemCount
        val lastVisiblePosition = mManager.findLastCompletelyVisibleItemPosition()

        // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll to the bottom of the list to show the newly added message.
        val loading = lastVisiblePosition == -1
        val atBottom = positionStart >= count - 1 && lastVisiblePosition == positionStart - 1
        if (loading || atBottom) {
            mRecycler.scrollToPosition(positionStart)
        }
    }
}
