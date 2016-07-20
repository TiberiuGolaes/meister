package com.nalatarate.meister.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nalatarate.meister.R
import com.nalatarate.meister.api.model.data.Sport
import com.nalatarate.meister.ui.activities.BaseActivity
import com.nalatarate.meister.ui.activities.HomeActivity
import com.nalatarate.meister.ui.holders.SportsViewHolder
import java.util.*

/**
 * Created by tiberiugolaes on 03/07/16.
 */

class SportsAdapter : RecyclerView.Adapter<SportsViewHolder>() {

    val mSports = ArrayList<Sport>()
    var mParentActivity: BaseActivity? = null

    override fun onBindViewHolder(holder: SportsViewHolder, position: Int) {
        holder.bindSport(mSports[position], this@SportsAdapter)
    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): SportsViewHolder {
        val v = LayoutInflater.from(view.context).inflate(R.layout.view_item_user_sport, view, false)
        return SportsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mSports.size
    }

    fun getItem(position: Int): Sport? {
        return mSports[position]
    }

    fun setActivity(activity: HomeActivity) {
        mParentActivity = activity
    }

    fun removeItem(sport: Sport) {
        mSports.remove(sport)
    }

    fun addSports(sports: List<Sport>) {
        mSports.clear()
        mSports.addAll(sports)
        notifyDataSetChanged()
    }

}