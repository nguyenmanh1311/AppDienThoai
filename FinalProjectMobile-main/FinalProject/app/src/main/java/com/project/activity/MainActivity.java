package com.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variable
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // set tab title
        ArrayList<String> title = new ArrayList<String>();
        title.add("Inbox");
        title.add("Ordered");
        title.add("Manager Product");
        title.add("Report");


        tabLayout.setupWithViewPager(viewPager);
        prepareViewPaper(viewPager, title);

        //Start service
        if(!foregroundServiceRunning()){
            Intent service = new Intent(getApplicationContext(), MyService.class);
            startForegroundService(service);
        }
    }

    private boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(MyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set tab title
        ArrayList<String> title = new ArrayList<String>();
        title.add("Inbox");
        title.add("Ordered");
        title.add("Manager Product");
        title.add("Report");


        tabLayout.setupWithViewPager(viewPager);
        prepareViewPaper(viewPager, title);
    }

    private void prepareViewPaper(ViewPager viewPager, ArrayList<String> title){
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        OrderFragment orderFragment = new OrderFragment();
        ReportFragment reportFragment = new ReportFragment();
        ManagerProductsFragment managerProductsFragment = new ManagerProductsFragment();
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle;

        bundle = new Bundle();
        bundle.putString("title", title.get(0));
        messageFragment.setArguments(bundle);
        adapter.addFragment(messageFragment, title.get(0));

        bundle = new Bundle();
        bundle.putString("title", title.get(1));
        orderFragment.setArguments(bundle);
        adapter.addFragment(orderFragment, title.get(1));

        bundle = new Bundle();
        bundle.putString("title", title.get(2));
        managerProductsFragment.setArguments(bundle);
        adapter.addFragment(managerProductsFragment, title.get(2));

        bundle = new Bundle();
        bundle.putString("title", title.get(3));
        reportFragment.setArguments(bundle);
        adapter.addFragment(reportFragment, title.get(3));

        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        public MainAdapter(FragmentManager fm){
            super(fm);
        }

        public void addFragment(Fragment fragment, String s){
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            SpannableString sp = new SpannableString(""+stringArrayList.get(position));
            return sp;
        }
    }
}