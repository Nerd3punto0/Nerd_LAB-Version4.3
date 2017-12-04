package com.jhonlopera.nerd30;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

public class PuntajeActivity extends PrincipalActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FragmentManager fm;
    private FragmentTransaction ft;
    Fragment fragment, fragment2, fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameprincipal);
        getLayoutInflater().inflate(R.layout.activity_puntajes, contentFrameLayout);
        getSupportActionBar().setTitle("Puntajes");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.remove(fragment1).commit(); //se remueve el fragment que se inicia por defecto en el oncreate de principal
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    fragment = new CreditosTpFragment();
                    return fragment;
                case 1:
                    fragment2 = new CreditosCtFragment();
                    return fragment2;
                case 2:
                    fragment3 = new CreditosCPULFragment();
                    return fragment3;
                default:return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Aplasta al topo";
                case 1:
                    return "Concéntrese";
                case 2:
                    return "4 imágenes 1 palabra";
            }
            return null;
        }
    }
}
