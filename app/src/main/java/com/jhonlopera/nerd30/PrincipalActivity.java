package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.view.Window;
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
    int aux=1;
    private  String correoR,nombreR,log,foto,usuario;
    private ImageView Foto_perfil_Header;
    private TextView tvnombre;
    private long puntaje4imagenes,puntajeConcentrese,puntajeTopo;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int silog;
    Fragment fragment1 ;
    private int estadosonido;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude=0, longitude=0;
    boolean bandera=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();

        //Para cerrar cesion con google
        paralogincongoogle();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        tvnombre = (TextView) headerView.findViewById(R.id.headernombre);
        Foto_perfil_Header = (ImageView) headerView.findViewById(R.id.imagenheader);
        modificarbanner(); //modifica nombre y foto de perfil del navdrawer
        navigationView.setNavigationItemSelectedListener(this);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        fragment1= new MenuPrincipalFragment();
        getSupportActionBar().setTitle("Menu principal");
        ft.add(R.id.frameprincipal, fragment1).commit();

        usuario = preferencias.getString("usuario", "No hay usuario");
        puntaje4imagenes = preferencias.getLong("puntaje4imagenes",0);
        puntajeConcentrese=preferencias.getLong("puntajeConcentrese",0);
        puntajeTopo=preferencias.getLong("puntajeTopo",0);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuario);
        Map<String, Object> newData = new HashMap<>();
        newData.put("puntaje4imagenes", puntaje4imagenes);
        newData.put("puntajeConcentrese", puntajeConcentrese);
        newData.put("puntajeTopo", puntajeTopo);
        myRef.updateChildren(newData);

        bandera=preferencias.getBoolean("bandera",false);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                editor_preferencias.putBoolean("bandera",bandera).apply();
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                if(latitude!=0 && longitude!=0) {
                    editor_preferencias.putString("latitude", String.valueOf(latitude)).apply();
                    editor_preferencias.putString("longitude", String.valueOf(longitude)).apply();
                    /*Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(i);*/
                    //locationManager.removeUpdates(locationListener);
                    if(!bandera) {
                        bandera=true;
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
       // location();
    }

    /*@Override
    protected void onStart() {
        location();
        super.onStart();
    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    @Override
    protected void onStop() {
        locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }*/

    private void location() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                return;
            }
        } else {
            //location();
        }
        //if(!bandera)
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(locationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location();
                }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            if(aux==0){
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                Fragment fragment= new MenuPrincipalFragment();
                getSupportActionBar().setTitle("Menu principal");
                ft.replace(R.id.frameprincipal, fragment).commit();
                aux=1;
                //super.onBackPressed();
            }else{
                super.onBackPressed();
            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        aux=0;
        int id = item.getItemId();
        boolean FragmentTransaction = false;
        Fragment fragment = null;
        Intent intent;
        // Si se abre el navigation se para la musica

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
            int contadorbroma=preferencias.getInt("contadorbroma",1);
            ft = fm.beginTransaction();

            fragment = new PerfilFragment();
            Bundle args=new Bundle();
            args.putString("correo",correoR);
            args.putString("nombre",nombreR);
            args.putString("foto",foto);
            args.putInt("contadorbroma",contadorbroma);
            fragment.setArguments(args);
            ft.addToBackStack("nombre");
            ft.addToBackStack("correo");
            ft.addToBackStack("foto");
            ft.addToBackStack("contadorbroma");
            getSupportActionBar().setTitle(item.getTitle());
            ft.replace(R.id.frameprincipal, fragment).commit();
            FragmentTransaction = false;


        } else if (id == R.id.nav_ranking) {

            intent = new Intent(this, PuntajeActivity.class);
            startActivity(intent);
        }
         //else if (id == R.id.nav_map){

          //  intent = new Intent(this, LocationActivity.class);
            //startActivity(intent);

        //}
        else if (id == R.id.nav_config) {

            estadosonido=preferencias.getInt("estadosonido",1);
            fragment=new ConfiguracionFragment();
            Bundle args=new Bundle();
            args.putString("nombre",nombreR);
            args.putString("foto",foto);
            args.putInt("estadosonido",estadosonido);
            fragment.setArguments(args);
            ft.addToBackStack("nombre");
            ft.addToBackStack("estadosonido");
            ft.addToBackStack("foto");
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
            if(foto==null){
                Foto_perfil_Header.setImageDrawable(getResources().getDrawable(R.drawable.perfil5));
            } else{
            Glide.with(this).load(foto).transform(new CircleTransform(this)).into(Foto_perfil_Header);}
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

        aux=0;
        Fragment fragment=new Menu4imagenesFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameprincipal, fragment).commit();
        getSupportActionBar().setTitle("Asociación");

    }

    @Override
    public void OpenConcentreseMenu() {
        aux=0;
        Fragment fragment=new MenuConcentreseFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameprincipal, fragment).commit();
        getSupportActionBar().setTitle("Memoria");
    }

    @Override
    public void OpenTopoMenu() {
        aux=0;
        Fragment fragment=new MenuTopoFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameprincipal, fragment).commit();
        getSupportActionBar().setTitle("Velocidad de reacción ");
    }

    @Override
    public void OpenCuantroImagenes() {
        Intent intent=new Intent(this,CuatroImagenesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
    public void cerrarjuego() {
        finish();
        System.exit(0);
    }

    @Override
    public void guardarpreferencias(int contadorbroma) {
        editor_preferencias.putInt("contadorbroma",contadorbroma).commit();
    }

    @Override
    public void OpenConcentrese() {
        Intent intent=new Intent(this,ConcentreseActivity.class);
        startActivity(intent);
    }

    @Override
    public void OpenTopo() {
        Intent intent=new Intent(this,TopoActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }

    @Override
    public void cerrarSesion() {
        silog=0;
        editor_preferencias.putInt("silog",silog);
        editor_preferencias.commit();
        signOut(); //cerrar sesion en google
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void actualizarpuntajes(long p4imagenes, long pconcentrese, long ptopo) {
        usuario = preferencias.getString("usuario", "No hay usuario");
        puntaje4imagenes = preferencias.getLong("puntaje4imagenes",0);
        puntajeConcentrese=preferencias.getLong("puntajeConcentrese",0);
        puntajeTopo=preferencias.getLong("puntajeTopo",0);

        editor_preferencias.putLong("puntaje4imagenes",puntaje4imagenes+p4imagenes).apply();
        editor_preferencias.putLong("puntajeConcentrese",puntajeConcentrese+pconcentrese).apply();
        editor_preferencias.putLong("puntajeTopo",puntajeTopo+ptopo).apply();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuario);
        Map<String, Object> newData = new HashMap<>();
        newData.put("puntaje4imagenes", puntaje4imagenes+p4imagenes);
        newData.put("puntajeConcentrese", puntajeConcentrese+pconcentrese);
        newData.put("puntajeTopo", puntajeTopo+ptopo);
        myRef.updateChildren(newData);
    }

    @Override
    public void estadomusica(int musicstate) {
        editor_preferencias.putInt("estadosonido",musicstate).apply();
    }

    @Override
    public void obtenerUbicacion() {
        location();
    }


}