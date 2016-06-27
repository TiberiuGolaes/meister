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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import com.nalatarate.meister.R
import com.nalatarate.meister.api.PrefManager
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    private var descrVisible = false
    private var editing = false


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
        DrawableCompat.setTint(drawable, resources.getColor(R.color.colorAccent, this@HomeActivity.theme));
        menu.findItem(R.id.action_messages).icon = drawable;
        return true;
    }

    fun getTintedDrawable(@DrawableRes drawableResId: Int, @ColorRes colorResId: Int): Drawable? {
        if (drawableResId == 0) return null
        val drawable = ContextCompat.getDrawable(this, drawableResId).mutate()
        val color = ContextCompat.getColor(this, colorResId)
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}