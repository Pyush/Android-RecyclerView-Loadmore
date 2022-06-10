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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recyclerview.loadmore.R;
import com.recyclerview.loadmore.adapter.LinearAdapter;
import com.recyclerview.loadmore.listener.OnLoadMoreListener;
import com.recyclerview.loadmore.listener.RecyclerViewLoadMoreScroll;
import com.recyclerview.loadmore.model.DataView;

import java.util.List;

/**
 * Created by Piyush on 1/10/2017.
 */
public class LinearLayoutFragment extends Fragment {

    private View rootView;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerview;
    private LinearAdapter linearAdapter;
    private RecyclerViewLoadMoreScroll scrollListener;

    public LinearLayoutFragment() {
        // Required empty public constructor
    }

    public static LinearLayoutFragment newInstance() {
        LinearLayoutFragment fragment = new LinearLayoutFragment();
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
                        List<DataView> dataViews=DataView.getDataViews(15);
                        linearAdapter=new LinearAdapter(dataViews);
                        recyclerview.setAdapter(linearAdapter);
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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        linearAdapter=new LinearAdapter(DataView.getDataViews(15));
        recyclerview.setAdapter(linearAdapter);

        scrollListener=new RecyclerViewLoadMoreScroll(linearLayoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadMoreData();
            }
        });

        recyclerview.addOnScrollListener(scrollListener);
    }

    private void LoadMoreData() {
        linearAdapter.addLoadingView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<DataView> dataViews=DataView.getDataViews(15);
                linearAdapter.removeLoadingView();
                linearAdapter.addData(dataViews);
                linearAdapter.notifyDataSetChanged();
                scrollListener.setLoaded();
            }
        },5000);

    }
}
