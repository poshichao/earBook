package com.hebut.earbook.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.hebut.earbook.R;
import com.hebut.earbook.view.fragment.RecyclerViewFragment;


public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private MaterialViewPager mViewPager;
    private String[][] titles = {{"能力提升", "nlts"}, {"文化娱乐", "whyl"}, {"生活实用", "shsy"}};
    private int cateNumber = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewpager();

    }


    private void initViewpager() {
        setTitle("");
        mViewPager = findViewById(R.id.materialViewPager);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // 为viewPager设置适配器
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                // 根据nlts/whyl/shsy，返回RecyclerViewFragment不同category的实例
                return RecyclerViewFragment.newInstance(titles[position][1]);
            }

            @Override
            public int getCount() {
                return cateNumber;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position % cateNumber][0];
            }


        });

        // 设置顶部颜色和图案
        mViewPager.setMaterialViewPagerListener(page -> {
            switch (page) {
                case 0:
                    return HeaderDesign.fromColorResAndDrawable(R.color.blue, getDrawable(R.drawable.bg_nlts));
                case 1:
                    return HeaderDesign.fromColorResAndDrawable(R.color.green, getDrawable(R.drawable.bg_whyl));
                case 2:
                    return HeaderDesign.fromColorResAndDrawable(R.color.cyan, getDrawable(R.drawable.bg_shsy));

            }


            return null;
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        // 设置顶部文字图标的点击事件监听
        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(v -> {
                mViewPager.notifyHeaderChanged();
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }
}
