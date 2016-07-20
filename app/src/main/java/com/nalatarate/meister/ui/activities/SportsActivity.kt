package com.nalatarate.meister.ui.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.nalatarate.meister.R
import com.nalatarate.meister.api.model.data.Sport
import com.nalatarate.meister.api.requester.SportsRequester
import com.nalatarate.meister.ui.adapters.SportsAdapter
import com.nalatarate.meister.utils.InertObserver
import com.nalatarate.meister.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.activity_sports.*
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by tiberiugolaes on 18/07/16.
 */
class SportsActivity : BaseActivity() {

    private lateinit var mSportsAdapter : SportsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports)
        title = getString(R.string.sports_menu)
        setSupportActionBar(sports_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowTitleEnabled(true);
        rv_all_sports.layoutManager = LinearLayoutManager(this@SportsActivity, LinearLayoutManager.VERTICAL, false)
        rv_all_sports.addItemDecoration(SimpleDividerItemDecoration(ContextCompat.getDrawable(baseContext, R.drawable.listview_divider)))
        rv_all_sports.setHasFixedSize(true)
        rv_all_sports.emptyView = findViewById(R.id.emptySport)
        rv_all_sports.adapter = SportsAdapter()
        (rv_all_sports.adapter as SportsAdapter).mParentActivity = this@SportsActivity
        mSportsAdapter = rv_all_sports.adapter as SportsAdapter
        getAllSports()
    }

    fun getAllSports(){
        SportsRequester.getAllSports().subscribeOn(Schedulers.io())
                .subscribe(object : InertObserver<List<Sport>>() {
                    override fun onNext(sports: List<Sport>) {
                        val sports2 = sports.plus(Sport("1", "Football"))
                        Log.d("Sports size", sports2.size.toString())
                        mSportsAdapter.addSports(sports2)
                    }

                    override fun onError(e: Throwable?) {
                        val sports2 = ArrayList<Sport>()
                        sports2.add(Sport("1", "Football"))
                        sports2.add(Sport("2", "Basketball"))
                        Log.d("Sports size", sports2.size.toString())
                        mSportsAdapter.addSports(sports2)
                        Log.d("Error", e?.message)
                    }
                })
    }
}