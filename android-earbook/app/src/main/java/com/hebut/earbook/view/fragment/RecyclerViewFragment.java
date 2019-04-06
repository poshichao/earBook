package com.hebut.earbook.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.hebut.earbook.R;
import com.hebut.earbook.view.widget.material.TestRecyclerViewAdapter;


public class RecyclerViewFragment extends Fragment {

    private TestRecyclerViewAdapter mViewAdapter;

    private static final boolean GRID_LAYOUT = false;

    private RecyclerView mRecyclerView;


    public static RecyclerViewFragment newInstance(String cate) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        // 根据传入的cate为fragment设置不同的页面参数
        Bundle bundle = new Bundle();
        bundle.putString("cate", cate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        // cate默认为nlts
        String cate = "nlts";
        if (getArguments() != null) {
            // 获取页面参数中的页面分类
            cate = getArguments().getString("cate", "nlts");
        }

        // 新建Recycler页面的adapter，传入分类，文章列表的开始序号和个数
        mViewAdapter = new TestRecyclerViewAdapter(cate, 1, 10);
        // 让adapter先根据上面传入的参数请求一次文章列表
        mViewAdapter.getArticleList();

        //setup materialviewpager
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(mViewAdapter);
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

}
