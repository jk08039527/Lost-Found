package com.jerry.zhoupro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titles;
	private FragmentManager fm;

	public FragmentViewPagerAdapter(FragmentManager _fm, String[] _titles, List<Fragment> _fragmentList) {
		super(_fm);
		this.titles = _titles;
		this.fragmentList = _fragmentList;
		this.fm = _fm;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return fragmentList == null ? 0 : fragmentList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	public void clearFragmentCache() {
		if (fm.isDestroyed()){
			return;
		}
		FragmentTransaction ft = fm.beginTransaction();
		for (Fragment fm : fragmentList) {
			ft.remove(fm);
		}
		ft.commitAllowingStateLoss();
	}
}