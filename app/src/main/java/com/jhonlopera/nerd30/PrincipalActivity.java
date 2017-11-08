package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OpenInterface{

   private FragmentManager fm;
    private FragmentTransaction ft;
    DatabaseReference myRef;
    FirebaseDatabase database;

    private  String correoR,nombreR,log,foto;
    private ImageView Foto_perfil_Header;
    private TextView tvnombre;


    GoogleApiClient mGoogleApiClient;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int silog;
    Fragment fragment1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();

        //Para cerrar cesion con google
        // ________________________________________________________________________________________________
        paralogincongoogle();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        tvnombre = (TextView) headerView.findViewById(R.id.headernombre);
        Foto_perfil_Header = (ImageView) headerView.findViewById(R.id.imagenheader);
        modificarbanner();

        navigationView.setNavigationItemSelectedListener(this);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        fragment1= new MenuPrincipalFragment();
        getSupportActionBar().setTitle("Menu principal");
        ft.add(R.id.frameprincipal, fragment1).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean FragmentTransaction = false;
        Fragment fragment = null;
        Intent intent;

        if (id == R.id.nav_main) {

            fragment = new MenuPrincipalFragment();
            FragmentTransaction = true;

        } else if (id == R.id.nav_profile) {

            log=preferencias.getString("log","error");
            correoR=preferencias.getString("correo","Su correo no es público");
            nombreR=preferencias.getString("nombre","Su nombre no es público");

            if (log.equals("facebook") || log.equals("google")){
                foto=preferencias.getString("foto",null);
            }
            else{
                foto=null;
            }
            ft = fm.beginTransaction();
            Bundle args=new Bundle();
            fragment = new PerfilFragment();
            args.putString("nombre",nombreR);
            args.putString("correo",correoR);
            args.putString("foto",foto);
            fragment.setArguments(args);
            ft.addToBackStack("nombre");
            ft.addToBackStack("correo");
            ft.addToBackStack("foto");
            getSupportActionBar().setTitle(item.getTitle());
            ft.replace(R.id.frameprincipal, fragment).commit();
            FragmentTransaction = false;


        } else if (id == R.id.nav_ranking) {

            intent = new Intent(this, PuntajeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_config) {

            fragment=new ConfiguracionFragment();
            FragmentTransaction =true;


        }  else if (id == R.id.nav_cerrarsesion){
            silog=0;
            editor_preferencias.putInt("silog",silog);
            editor_preferencias.commit();

            log=preferencias.getString("log","error");
            if(log.equals("google")){
                signOut(); //cerrar sesion en google
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                intent=new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        else if(id==R.id.nav_4imagenes){
            fragment = new Menu4imagenesFragment();
            FragmentTransaction = true;

        }
        else if(id==R.id.nav_concentrese){
            fragment = new MenuConcentreseFragment();
            FragmentTransaction = true;
        }
        else  if(id==R.id.nav_aplasta){
            fragment = new MenuTopoFragment();
            FragmentTransaction = true;
        }


        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameprincipal,fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(),"Saliendo de Google", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void modificarbanner( ) {

        log=preferencias.getString("log","error");
        nombreR=preferencias.getString("nombre"," ");
        tvnombre.setText(nombreR);
        if (log.equals("facebook")||log.equals("google")){
            foto=preferencias.getString("foto",null);
            Glide.with(this).load(foto).transform(new CircleTransform(this)).into(Foto_perfil_Header);
        }
        else {
            Foto_perfil_Header.setImageDrawable(getResources().getDrawable(R.drawable.perfil5));
        }
    }
    private void paralogincongoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){

            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    @Override
    public void OpenCuatroImagenesMenu() {

        Fragment fragment=new Menu4imagenesFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameprincipal, fragment).commit();
        getSupportActionBar().setTitle("Asociación");

    }

    @Override
    public void OpenConcentreseMenu() {
        Fragment fragment=new MenuConcentreseFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameprincipal, fragment).commit();
        getSupportActionBar().setTitle("Concentración");
    }

    @Override
    public void OpenTopoMenu() {
        Fragment fragment=new MenuTopoFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameprincipal, fragment).commit();
        getSupportActionBar().setTitle("Velocidad de reacción ");

    }

    @Override
    public void OpenCuantroImagenes() {
        Intent intent=new Intent(this,CuatroImagenesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void cambiarnombre(String nuevonombre) {
        String usuariojuego=preferencias.getString("usuario"," ");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuariojuego);
        Map<String, Object> newData = new HashMap<>();
        newData.put("nombre", nuevonombre);
        myRef.updateChildren(newData);
        editor_preferencias.putString("nombre",nuevonombre).commit();
        modificarbanner();


    }

    @Override
    public void eliminardatos() {

        String usuariojuego=preferencias.getString("usuario"," ");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuariojuego);
        Map<String, Object> newData = new HashMap<>();
        newData.put("nombre", " ");
        newData.put("correo"," ");
        newData.put("puntaje4imagenes",0);
        newData.put("puntajeConcentrese",0);
        newData.put("puntajeTopo",0);
        myRef.updateChildren(newData);


    }
}

