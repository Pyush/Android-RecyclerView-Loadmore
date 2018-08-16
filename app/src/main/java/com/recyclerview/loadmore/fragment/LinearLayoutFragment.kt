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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.recyclerview.loadmore.R
import com.recyclerview.loadmore.adapter.LinearAdapter
import com.recyclerview.loadmore.listener.OnLoadMoreListener
import com.recyclerview.loadmore.listener.RecyclerViewLoadMoreScroll
import com.recyclerview.loadmore.model.DataView

/**
 * Created by Piyush on 1/10/2017.
 */
class LinearLayoutFragment : Fragment() {

    private var rootView: View? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var recyclerview: RecyclerView? = null
    private var linearAdapter: LinearAdapter? = null
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
                val dataViews = DataView.getDataViews(15)
                linearAdapter = LinearAdapter(dataViews)
                recyclerview!!.adapter = linearAdapter
                swipeRefresh!!.isRefreshing = false
            }, 5000)
        }
    }

    private fun Initialize() {
        swipeRefresh = rootView!!.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        recyclerview = rootView!!.findViewById<View>(R.id.recyclerview) as RecyclerView
        recyclerview!!.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview!!.layoutManager = linearLayoutManager
        linearAdapter = LinearAdapter(DataView.getDataViews(15))
        recyclerview!!.adapter = linearAdapter

        scrollListener = RecyclerViewLoadMoreScroll(linearLayoutManager)
        scrollListener!!.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })

        recyclerview!!.addOnScrollListener(scrollListener!!)
    }

    private fun LoadMoreData() {
        linearAdapter!!.addLoadingView()
        Handler().postDelayed({
            val dataViews = DataView.getDataViews(15)
            linearAdapter!!.removeLoadingView()
            linearAdapter!!.addData(dataViews)
            linearAdapter!!.notifyDataSetChanged()
            scrollListener!!.setLoaded()
        }, 5000)

    }

    companion object {

        fun newInstance(): LinearLayoutFragment {
            val fragment = LinearLayoutFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
