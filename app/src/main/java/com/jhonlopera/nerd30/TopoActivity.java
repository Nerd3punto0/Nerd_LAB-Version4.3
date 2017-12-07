package com.jhonlopera.nerd30;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TopoActivity extends AppCompatActivity implements View.OnClickListener {

    private Chronometer tiempo;
    boolean sianimar=true;
    int numero;
    ImageView Topos[]=new ImageView[9];
    ImageView Icorazon[]=new ImageView[3];
    int id[]={R.id.topo1,R.id.topo2,R.id.topo3,R.id.topo4,R.id.topo5,R.id.topo6,R.id.topo7,R.id.topo8,R.id.topo9};
    int level;
    AnimationDrawable animacionTopo[]=new AnimationDrawable[9];
    long contador,puntaje;
    String usuario;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private MediaPlayer player,aplastado;
    TextView score;
    private int estadomusica;
    private int velocidad,contadorgolpe;
    private boolean golpe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_topo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Aplasta al Topo");
        contador=0;
        velocidad=1500;
        contadorgolpe=0;
        golpe=false;

        Icorazon[0]=(ImageView) findViewById(R.id.corazon3);
        Icorazon[1]=(ImageView) findViewById(R.id.corazon2);
        Icorazon[2]=(ImageView) findViewById(R.id.corazon1);
        //Se cargan los datos necesarios desde preferencias
        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();
        estadomusica=preferencias.getInt("estadosonido",1);
        puntaje=0;
        usuario=preferencias.getString("usuario","No hay usuario");
        level=preferencias.getInt("niveltopo",1);

        //Se sincroniza Fire base y las preferencias


        // se asignan las animaciones a cada imageview
        for(int i=0;i<id.length;i++){
            Topos[i]=(ImageView) findViewById(id[i]);
            Topos[i].setBackgroundResource(R.drawable.animacion);
            animacionTopo[i] =(AnimationDrawable) Topos[i].getBackground();
        }

        tiempo=(Chronometer) findViewById(R.id.tiempoTopo);
        score=(TextView) findViewById(R.id.tscoreTopo);

        player = MediaPlayer.create(this, R.raw.sonidoparatopo);
        aplastado=MediaPlayer.create(this,R.raw.aplastado);
        //Se genera el onclick de cada imageview
        score.setText("Score: "+ String.valueOf(puntaje)); // se carga el puntaje en el textview
        for(int k=0;k<9;k++){
            Topos[k].setOnClickListener(this);
        }

        // Se activael sonido si el usuario lo permite
        if (estadomusica==1){
            player.setLooping(true);
            player.start();
        }

        //Crear un dialog para comezar
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("¿Estas listo listo?");
        builder.setMessage("Presiona 'Listo' para iniciar el juego\n");
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tiempo.start(); //Se inicia el reloj
                tiempo.setBase(SystemClock.elapsedRealtime()); // Se inicia el tiempo desde 0

                animar(); // Se inicia la animación
            }
        });
        builder.setNegativeButton("Aun no estoy listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player.stop();
                Intent intent=new Intent(TopoActivity.this,PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.setCancelable(false);
        dialog.show();

    }
    private void animar() {
        numero = (int) (Math.random() * 9 );
        if(sianimar) {
            animacionTopo[numero].stop();
            animacionTopo[numero].start();
            contador = (SystemClock.elapsedRealtime() - tiempo.getBase()) / 1000;


        }
        loop();
    }
    public void loop(){
        Long velaux=(SystemClock.elapsedRealtime()-tiempo.getBase())/1000;

        if(velaux<=30){
            velocidad=1500;
        }
        else if(velaux>30 && velaux<=60){
            velocidad=1200;
        }
        else if(velaux>60 && velaux<=90){
            velocidad=1000;
        }
        else{
            velocidad=750;
        }

        CountDownTimer countDownTimer=new CountDownTimer(velocidad,200) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {

                if(golpe==false){
                    if(contadorgolpe<3) {
                        Icorazon[contadorgolpe].setVisibility(View.GONE);
                        contadorgolpe++;
                    }
                }
                if(contadorgolpe<3){
                    animar();
                    golpe=false;
                }
                else{
                    mensaje();
                }

            }
        };
        countDownTimer.start();
    }

    private void mensaje() {

        player.stop();
        final Intent intent=new Intent(TopoActivity.this,TopoActivity.class);
        final Intent intent2=new Intent(TopoActivity.this,PrincipalActivity.class);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("JAJAJAJA LOOOSEEEER");
        builder.setMessage("¿Deseas reinciar el juego y mejorar tu puntaje?\n");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //puntaje=0;
                //editor_preferencias.putLong("puntajeTopo",puntaje).apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Long puntajeaux=preferencias.getLong("puntajeTopo",0);
                if(puntaje>puntajeaux){
                editor_preferencias.putLong("puntajeTopo",puntaje).apply();
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("DatosDeUsuario").child(usuario);
                Map<String, Object> newData = new HashMap<>();
                newData.put("puntajeTopo", puntaje);
                myRef.updateChildren(newData);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();

            }
        });
        AlertDialog dialog= builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onClick(View src) {

        int numeroaux=numero;
        int numeroaux2=0;
        long aux1= (SystemClock.elapsedRealtime()-tiempo.getBase())/1000;
        for (int i=0;i<id.length;i++){
            if(src==Topos[i]){
                numeroaux2=i;
                break;
            }
        }
        if(aux1-contador<=920 && numeroaux==numeroaux2){
            animacionTopo[numeroaux].stop();
            if(estadomusica==1){
                aplastado.start();
            }
            golpe=true;

            final int numerito=numeroaux;
            Topos[numeroaux].setBackgroundResource(R.drawable.golpe);
            puntaje+=5;
            //editor_preferencias.putLong("puntajeTopo",puntaje).apply();
            score.setText("Score: "+ String.valueOf(puntaje));


            CountDownTimer countDownTimer=new CountDownTimer(500,100) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    Topos[numerito].setBackgroundResource(R.drawable.animacion);
                    animacionTopo[numerito] =(AnimationDrawable) Topos[numerito].getBackground();
                }
            };
            countDownTimer.start();
        }
    }
    public void onBackPressed(){
        player.stop();
        Intent intent=new Intent(this,PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        player.stop();
        super.onPause();
    }

    @Override
    protected void onStop() {
        player.stop();
        super.onStop();
    }
    @Override
    protected void onRestart() {

        if (estadomusica==1){
            player = MediaPlayer.create(this, R.raw.sonido1);
            player.setLooping(true);
            player.start();
        }
        super.onRestart();
    }
}