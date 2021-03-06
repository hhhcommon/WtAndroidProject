package com.wotingfm.ui.base.baseadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);  
        this.list = list;  
    }  
      
    @Override  
    public int getCount() {  
        return list.size();  
        
    }  
      
    @Override  
    public Fragment getItem(int arg0) {
//    	Fragment fragment=null;
//    	if(list.size()>arg0){
//    		fragment=list.get(arg0);
//    		if(fragment!=null){
//    			return fragment;
//    		}
//    	}
        return list.get(arg0);
    }
}  

