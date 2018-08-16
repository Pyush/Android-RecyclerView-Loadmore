package com.recyclerview.loadmore.adapter

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

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.recyclerview.loadmore.R
import com.recyclerview.loadmore.model.DataView
import com.recyclerview.loadmore.utils.Constant
import com.recyclerview.loadmore.viewholder.GridHolder
import com.recyclerview.loadmore.viewholder.LoadingHolder

/**
 * Created by Piyush on 1/10/2017.
 */

class GridAdapter(internal var dataViews: MutableList<DataView>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    fun addData(dataViews: List<DataView>) {
        this.dataViews!!.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): DataView {
        return dataViews!![position]
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            var data = DataView();
            data.viewType = Constant.VIEW_TYPE_LOADING
            dataViews!!.add(data)
            notifyItemInserted(dataViews!!.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        dataViews!!.removeAt(dataViews!!.size - 1)
        notifyItemRemoved(dataViews!!.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_row, parent, false)
            return GridHolder(view)
        } else  {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_loading, parent, false)
            return LoadingHolder(view)
        }
    }


    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    override fun getItemCount(): Int {
        return if (dataViews == null) 0 else dataViews!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataViews!![position] == null) Constant.VIEW_TYPE_LOADING else Constant.VIEW_TYPE_ITEM
    }
}
