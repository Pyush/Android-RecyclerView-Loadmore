package com.recyclerview.loadmore.adapter;

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

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recyclerview.loadmore.R;
import com.recyclerview.loadmore.model.DataView;
import com.recyclerview.loadmore.utils.Constant;
import com.recyclerview.loadmore.viewholder.LoadingHolder;
import com.recyclerview.loadmore.viewholder.StaggeredHolder;

import java.util.List;

/**
 * Created by Piyush on 1/10/2017.
 */
public class StaggeredAdapter extends RecyclerView.Adapter {

    List<DataView> dataViews;

    public StaggeredAdapter(List<DataView> dataViews) {
        this.dataViews = dataViews;
    }

    public void addData(List<DataView> dataViews) {
        this.dataViews.addAll(dataViews);
        notifyDataSetChanged();
    }

    public DataView getItemAtPosition(int position) {
        return dataViews.get(position);
    }

    public void addLoadingView() {
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dataViews.add(null);
                notifyItemInserted(dataViews.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        //Remove loading item
        dataViews.remove(dataViews.size() - 1);
        notifyItemRemoved(dataViews.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== Constant.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_row, parent, false);
            return new StaggeredHolder(view);
        } else if(viewType==Constant.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_loading, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DataView dataView=dataViews.get(position);
        if(holder instanceof StaggeredHolder) {
            StaggeredHolder staggeredHolder=(StaggeredHolder)holder;

        } else if(holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder=(LoadingHolder)holder;
        }

        if(dataView==null) {
            // Span the item if active
            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(true);
                holder.itemView.setLayoutParams(sglp);
            }
        } else {
            // Span the item if active
            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(false);
                holder.itemView.setLayoutParams(sglp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataViews == null ? 0 : dataViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataViews.get(position) == null ? Constant.VIEW_TYPE_LOADING : Constant.VIEW_TYPE_ITEM;
    }
}
