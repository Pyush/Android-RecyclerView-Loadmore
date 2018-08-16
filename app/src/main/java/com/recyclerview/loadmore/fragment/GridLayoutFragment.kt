package com.recyclerview.loadmore.fragment

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
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recyclerview.loadmore.R
import com.recyclerview.loadmore.adapter.GridAdapter
import com.recyclerview.loadmore.listener.OnLoadMoreListener
import com.recyclerview.loadmore.listener.RecyclerViewLoadMoreScroll
import com.recyclerview.loadmore.model.DataView
import com.recyclerview.loadmore.utils.Constant.VIEW_TYPE_ITEM
import com.recyclerview.loadmore.utils.Constant.VIEW_TYPE_LOADING
import com.recyclerview.loadmore.widget.GridSpacingItemDecoration

/**
 * Created by Piyush on 1/10/2017.
 */

class GridLayoutFragment : Fragment() {

    private var rootView: View? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var recyclerview: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var gridAdapter: GridAdapter? = null
    private var scrollListener: RecyclerViewLoadMoreScroll? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.layout_recyclerview, container, false)
        Initialize()
        return rootView
    }

    override fun onResume() {
        super.onResume()

        swipeRefresh!!.setOnRefreshListener {
            Handler().postDelayed({
                setAdapter()
                swipeRefresh!!.isRefreshing = false
            }, 5000)
        }
    }

    private fun Initialize() {
        swipeRefresh = rootView!!.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        recyclerview = rootView!!.findViewById<View>(R.id.recyclerview) as RecyclerView
        recyclerview!!.setHasFixedSize(true)
        val spacingInPixels = 5
        recyclerview!!.addItemDecoration(GridSpacingItemDecoration(3, spacingInPixels, true, 0))
        setAdapter()

        scrollListener = RecyclerViewLoadMoreScroll(gridLayoutManager!!)
        scrollListener!!.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })

        recyclerview!!.addOnScrollListener(scrollListener!!)
    }

    private fun setAdapter() {
        gridLayoutManager = GridLayoutManager(activity, 3)
        recyclerview!!.layoutManager = gridLayoutManager
        gridAdapter = GridAdapter(DataView.getDataViews(15))
        recyclerview!!.adapter = gridAdapter

        gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (gridAdapter!!.getItemViewType(position)) {
                    VIEW_TYPE_ITEM -> return 1
                    VIEW_TYPE_LOADING -> return 3 //number of columns of the grid
                    else -> return -1
                }
            }
        }
    }

    private fun LoadMoreData() {
        gridAdapter!!.addLoadingView()
        Handler().postDelayed({
            val dataViews = DataView.getDataViews(15)
            gridAdapter!!.removeLoadingView()
            gridAdapter!!.addData(dataViews)
            gridAdapter!!.notifyDataSetChanged()
            scrollListener!!.setLoaded()
        }, 5000)

    }

    companion object {

        fun newInstance(): GridLayoutFragment {
            val fragment = GridLayoutFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
