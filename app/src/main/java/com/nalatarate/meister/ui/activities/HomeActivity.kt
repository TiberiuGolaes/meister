package com.nalatarate.meister.ui.activities

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import com.nalatarate.meister.R
import com.nalatarate.meister.api.PrefManager
import com.nalatarate.meister.api.model.data.Sport
import com.nalatarate.meister.api.requester.SportsRequester
import com.nalatarate.meister.ui.adapters.SportsAdapter
import com.nalatarate.meister.utils.EmptyStateRecyclerView
import com.nalatarate.meister.utils.InertObserver
import com.nalatarate.meister.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.activity_home.*
import rx.schedulers.Schedulers
import java.util.*


class HomeActivity : BaseActivity() {

    private var descrVisible = false
    private var sportVisible = false
    private var editing = false
    lateinit var mSportsAdapter: SportsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(getTintedDrawable(R.drawable.menu, R.color.colorAccent))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowTitleEnabled(false);
        descriptionHeader.setOnClickListener {
            descrVisible = !descrVisible
            descriptionContent.visibility = if (descrVisible) View.VISIBLE else View.GONE
            description_expand.isSelected = descrVisible
        }
        sportHeader.setOnClickListener {
            sportVisible = !sportVisible
            sportsContent.visibility = if (sportVisible) View.VISIBLE else View.GONE
            sport_expand.isSelected = sportVisible
        }
        rv_sports.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        rv_sports.addItemDecoration(SimpleDividerItemDecoration(ContextCompat.getDrawable(baseContext, R.drawable.listview_divider)))
        rv_sports.setHasFixedSize(true)
        rv_sports.emptyView = findViewById(R.id.emptySport)
        rv_sports.adapter = SportsAdapter()
        (rv_sports.adapter as SportsAdapter).mParentActivity = this@HomeActivity
        mSportsAdapter = rv_sports.adapter as SportsAdapter
        emptySport.setOnClickListener{
            startActivity(Intent(this@HomeActivity, SportsActivity::class.java))
        }
        getUserSports()
        tv_description.text = PrefManager.description
        tvEdit.setOnClickListener {
            if (!editing) {
                editing = true
                tv_description.visibility = View.GONE
                et_description.clearComposingText()
                et_description.text.clear()
                et_description.text.insert(0, tv_description.text)
                et_description.visibility = View.VISIBLE
                et_description.requestFocus()
                tvEdit.setText(R.string.user_done)
            } else {
                editing = false
                tv_description.text = et_description.text.toString()
                et_description.visibility = View.GONE
                tv_description.visibility = View.VISIBLE
                PrefManager.setDescription(tv_description.text.toString())
                tvEdit.setText(R.string.user_edit)
            }
        }
        et_description.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                editing = false
                tv_description.text = et_description.text.toString()
                et_description.visibility = View.GONE
                tv_description.visibility = View.VISIBLE
                tvEdit.setText(R.string.user_edit)
                true
            }
            false
        }
        navigation_view.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers();
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    PrefManager.clearSession()
                    startActivity(Intent(this@HomeActivity, FirstActivity::class.java))
                    this.finish()
                }
                R.id.action_sports ->{
                    startActivity(Intent(this@HomeActivity, SportsActivity::class.java))
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_full_home, menu);
        var drawable = menu.findItem(R.id.action_messages).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorAccent));
        menu.findItem(R.id.action_messages).icon = drawable;
        return true;
    }

    fun getUserSports() {
        SportsRequester.getUserSports().subscribeOn(Schedulers.io())
                .subscribe(object : InertObserver<List<Sport>>() {
                    override fun onNext(sports: List<Sport>) {
                        val sports2 = sports.plus(Sport("1", "Football"))
                        Log.d("Sports size", sports2.size.toString())
                        mSportsAdapter.addSports(sports2)
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("Error", e?.message)
                    }
                })

    }

    fun getTintedDrawable(@DrawableRes drawableResId: Int, @ColorRes colorResId: Int): Drawable? {
        if (drawableResId == 0) return null
        val drawable = ContextCompat.getDrawable(this, drawableResId).mutate()
        val color = ContextCompat.getColor(this, colorResId)
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}