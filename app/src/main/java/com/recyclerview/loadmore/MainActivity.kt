package com.recyclerview.loadmore

/*
 * MIT License
 *
 * Copyright (c) 2017 Piyush
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.recyclerview.loadmore.fragment.GridLayoutFragment
import com.recyclerview.loadmore.fragment.LinearLayoutFragment
import com.recyclerview.loadmore.fragment.StaggeredGridLayoutFragment

class MainActivity : AppCompatActivity() {

    private var tab_layout: TabLayout? = null
    private var viewpager: ViewPager? = null
    private var loadMorePagerAdapter: LoadMorePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Initialize()
    }

    private fun Initialize() {
        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        mToolbar.setTitle(R.string.app_name)
        setSupportActionBar(mToolbar)

        tab_layout = findViewById<TabLayout>(R.id.tab_layout)
        tab_layout!!.addTab(tab_layout!!.newTab().setText("Linear"))
        tab_layout!!.addTab(tab_layout!!.newTab().setText("Grid"))
        tab_layout!!.addTab(tab_layout!!.newTab().setText("Staggered"))
        tab_layout!!.tabGravity = TabLayout.GRAVITY_FILL

        viewpager = findViewById<ViewPager>(R.id.viewPager)
        viewpager!!.offscreenPageLimit = 3
        loadMorePagerAdapter = LoadMorePagerAdapter(supportFragmentManager)
        viewpager!!.adapter = loadMorePagerAdapter
        viewpager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private inner class LoadMorePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        internal val mNumOfTab = 3

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = LinearLayoutFragment.newInstance()
                1 -> fragment = GridLayoutFragment.newInstance()
                2 -> fragment = StaggeredGridLayoutFragment.newInstance()
            }
            return fragment
        }

        override fun getCount(): Int {
            return mNumOfTab
        }
    }
}
