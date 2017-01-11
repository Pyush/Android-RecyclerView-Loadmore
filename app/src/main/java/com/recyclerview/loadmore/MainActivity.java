package com.recyclerview.loadmore;

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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.recyclerview.loadmore.fragment.GridLayoutFragment;
import com.recyclerview.loadmore.fragment.LinearLayoutFragment;
import com.recyclerview.loadmore.fragment.StaggeredGridLayoutFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tab_layout;
    private ViewPager viewpager;
    private LoadMorePagerAdapter loadMorePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize();
    }

    private void Initialize() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        tab_layout=(TabLayout)findViewById(R.id.tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("Linear"));
        tab_layout.addTab(tab_layout.newTab().setText("Grid"));
        tab_layout.addTab(tab_layout.newTab().setText("Staggered"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewpager=(ViewPager)findViewById(R.id.viewPager);
        viewpager.setOffscreenPageLimit(3);
        loadMorePagerAdapter=new LoadMorePagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(loadMorePagerAdapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private class LoadMorePagerAdapter extends FragmentPagerAdapter {

        final int mNumOfTab = 3;

        public LoadMorePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position) {
                case 0:
                    fragment = LinearLayoutFragment.newInstance();
                    break;
                case 1:
                    fragment = GridLayoutFragment.newInstance();
                    break;
                case 2:
                    fragment= StaggeredGridLayoutFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mNumOfTab;
        }
    }
}
