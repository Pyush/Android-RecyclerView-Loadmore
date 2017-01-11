package com.recyclerview.loadmore.fragment;

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

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recyclerview.loadmore.R;
import com.recyclerview.loadmore.adapter.GridAdapter;
import com.recyclerview.loadmore.listener.OnLoadMoreListener;
import com.recyclerview.loadmore.listener.RecyclerViewLoadMoreScroll;
import com.recyclerview.loadmore.model.DataView;
import com.recyclerview.loadmore.widget.GridSpacingItemDecoration;

import java.util.List;

import static com.recyclerview.loadmore.utils.Constant.VIEW_TYPE_ITEM;
import static com.recyclerview.loadmore.utils.Constant.VIEW_TYPE_LOADING;

/**
 * Created by Piyush on 1/10/2017.
 */

public class GridLayoutFragment extends Fragment {

    private View rootView;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerview;
    private GridLayoutManager gridLayoutManager;
    private GridAdapter gridAdapter;
    private RecyclerViewLoadMoreScroll scrollListener;

    public GridLayoutFragment() {
        // Required empty public constructor
    }

    public static GridLayoutFragment newInstance() {
        GridLayoutFragment fragment = new GridLayoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.layout_recyclerview, container, false);
        Initialize();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                        swipeRefresh.setRefreshing(false);
                    }
                },5000);
            }
        });
    }

    private void Initialize() {
        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        recyclerview=(RecyclerView)rootView.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        int spacingInPixels=5;
        recyclerview.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true, 0));
        setAdapter();

        scrollListener=new RecyclerViewLoadMoreScroll(gridLayoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadMoreData();
            }
        });

        recyclerview.addOnScrollListener(scrollListener);
    }

    private void setAdapter() {
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerview.setLayoutManager(gridLayoutManager);
        gridAdapter=new GridAdapter(DataView.getDataViews(15));
        recyclerview.setAdapter(gridAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(gridAdapter.getItemViewType(position)) {
                    case VIEW_TYPE_ITEM:
                        return 1;
                    case VIEW_TYPE_LOADING:
                        return 3; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
    }

    private void LoadMoreData() {
        gridAdapter.addLoadingView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<DataView> dataViews=DataView.getDataViews(15);
                gridAdapter.removeLoadingView();
                gridAdapter.addData(dataViews);
                gridAdapter.notifyDataSetChanged();
                scrollListener.setLoaded();
            }
        },5000);

    }
}
