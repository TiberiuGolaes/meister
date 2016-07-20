package com.nalatarate.meister.utils

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by tiberiugolaes on 03/07/16.
 */
open class EmptyStateRecyclerView : RecyclerView {
    var emptyView: View? = null
        set(view) {
            field = view
            checkEmptyView()
        }

    lateinit var checkArray: Array<Boolean>

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private fun checkEmptyView() {
        if (adapter == null) {
            return;
        }
        if (adapter.itemCount > 0) {
            emptyView?.visibility = View.GONE
        } else {
            emptyView?.visibility = View.VISIBLE
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (firstPos >= 0 && lastPos >= 0 && checkArray.count() > 0) {
                for (i in firstPos..lastPos)
                    if (!checkArray[i]) {
                        checkArray[i] = true
                        (adapter as SimpleAdapter<*, *>).sendMixpanelEvents(i)
                    }
            }
        }
    }

    private val observer = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged();
            checkEmptyView();
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount);
            checkEmptyView();
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkEmptyView();
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            checkEmptyView();
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkEmptyView();

        }
    };


    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        val oldAdapter = this.adapter;
        oldAdapter?.unregisterAdapterDataObserver(observer);
        super.setAdapter(adapter);
        adapter?.registerAdapterDataObserver(observer);
    }

    override fun swapAdapter(adapter: Adapter<ViewHolder>?, removeAndRecycleExistingViews: Boolean) {
        val oldAdapter = this.adapter;
        oldAdapter?.unregisterAdapterDataObserver(observer);
        adapter?.registerAdapterDataObserver(observer);
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        checkEmptyView();
    }

    abstract class SimpleAdapter<T, VH : SimpleViewHolder<T>>(var text: String?) : RecyclerView.Adapter<VH>() {
        val mItems = ArrayList<T>()
        override fun getItemCount(): Int {
            return mItems.size
        }

        fun setData(items: List<T>?) {
            mItems.clear()
            if (items != null) {
                mItems.addAll(items)
            }
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int {

            return super.getItemViewType(position);
        }

        open fun sendMixpanelEvents(position: Int) {
        }

        override final fun onBindViewHolder(holder: VH, i: Int) {
            if (i < mItems.size) {
                holder.bind(mItems[i])
            }
        }
    }

    abstract class SimpleViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

}