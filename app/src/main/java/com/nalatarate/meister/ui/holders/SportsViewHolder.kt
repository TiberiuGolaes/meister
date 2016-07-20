package com.nalatarate.meister.ui.holders

import android.location.Location
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.nalatarate.meister.R
import com.nalatarate.meister.api.model.data.Sport
import com.nalatarate.meister.api.requester.SportsRequester
import com.nalatarate.meister.ui.adapters.SportsAdapter
import com.nalatarate.meister.utils.InertObserver
import kotlinx.android.synthetic.main.view_item_user_sport.view.*
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by tiberiugolaes on 02/07/16.
 */
class SportsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    lateinit var mAdapter: SportsAdapter

    fun bindSport(sport: Sport, adapter: SportsAdapter) = itemView.apply {
        txtSport.text = sport.name
        mAdapter = adapter
        btnRemove.setOnClickListener {
            removeSport(sport)
        }
    }

    fun removeSport(sport: Sport) {
        SportsRequester.removeUserSport(sport.id).subscribeOn(Schedulers.io())
                .subscribe(object : InertObserver<Void>() {
                    override fun onNext(void: Void) {
                        mAdapter.removeItem(sport)
                        mAdapter.notifyDataSetChanged()
                        Snackbar.make(itemView, R.string.remove_sport, Snackbar.LENGTH_SHORT)
                    }

                    override fun onError(e: Throwable?) {
                        Snackbar.make(itemView, e?.message.toString(), Snackbar.LENGTH_SHORT)
                        Log.d("Error", e?.message)
                    }
                })

    }
}