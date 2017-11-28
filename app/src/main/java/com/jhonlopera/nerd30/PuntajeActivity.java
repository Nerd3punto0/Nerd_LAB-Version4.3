package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PuntajeActivity extends PrincipalActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FragmentManager fm;
    private FragmentTransaction ft;
    Jugador jugador;
    String usuario, contador;
    FragmentTransaction ft2;
    FragmentManager fm2;
    boolean aux;
    Bundle args1, args2, args3;
    int valorfinal;
    String jugadores [] = {"No hay jugador","No hay jugador","No hay jugador","No hay jugador","No hay jugador"};
    String puntaje4img [] = {"0","0","0","0","0"};
    String puntajecon [] = {"0","0","0","0","0"};
    String puntajetp [] = {"0","0","0","0","0"};
    DatabaseReference myRef;
    FirebaseDatabase database;
    Fragment fragment, fragment2, fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_puntajes);

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


        SharedPreferences preferencias;
        SharedPreferences.Editor editor_preferencias;
        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();
        usuario=preferencias.getString("usuario","No hay usuario");
        database = FirebaseDatabase.getInstance();

        //Pasar solo usuario a los fragments para leer de la base de datos de Firebase en cada uno de ellos
        args1 = new Bundle();
        args1.putString("usuario",usuario);
        fm2=getSupportFragmentManager();
        ft2=fm2.beginTransaction();
        ft2.addToBackStack("usuario");
        //

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
                    fragment.setArguments(args1);
                    return fragment;
                case 1:
                    fragment2 = new CreditosCtFragment();
                    //fragment2.setArguments(args2);
                    fragment2.setArguments(args1);
                    return fragment2;
                case 2:
                    fragment3 = new CreditosCPULFragment();
                    //fragment3.setArguments(args3);
                    fragment3.setArguments(args1);
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
                    return "Concentrese";
                case 2:
                    return "4 palabras 1 letra";
            }
            return null;
        }
    }
}
