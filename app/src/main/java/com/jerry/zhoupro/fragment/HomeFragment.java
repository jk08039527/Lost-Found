package com.jerry.zhoupro.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.adapter.FragmentViewPagerAdapter;
import com.jerry.zhoupro.command.Constants;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;

/**
 * Created by wzl-pc on 2017/5/9.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.rb_lost)
    RadioButton mRbLost;
    @BindView(R.id.rb_found)
    RadioButton mRbFound;
    @BindView(R.id.rg_main)
    RadioGroup mRgMain;
    @BindView(R.id.vp_lost_found)
    private ViewPager mVpLostFound;
    private int type;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        mRgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                type = checkedId == R.id.rb_lost ? Constants.LOST : Constants.FOUND;
                mVpLostFound.setCurrentItem(checkedId == R.id.rb_lost ? 0 : 1);
            }
        });

        mFragmentList = new ArrayList<>(2);
        mVpLostFound.setAdapter(mAdapter);
        mVpLostFound.setOffscreenPageLimit(mFragmentList.size());
        initFragment();
    }

    private void initFragment() {
        if (mAdapter != null) {
            mAdapter.clearFragmentCache();
            mFragmentList.clear();
        }
        mFragmentList.add(InfoListBaseFragment.newInstance(Constants.LOST));
        mFragmentList.add(InfoListBaseFragment.newInstance(Constants.FOUND));
        String[] tabs = {getString(R.string.lost), getString(R.string.found)};
        mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), tabs, mFragmentList);
        mVpLostFound.setAdapter(mAdapter);
        mVpLostFound.setOffscreenPageLimit(mFragmentList.size());
    }
}
