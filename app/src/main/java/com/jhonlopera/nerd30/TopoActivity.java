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
import android.view.Window;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_topo);

        player = MediaPlayer.create(this, R.raw.sonidoparatopo);
        aplastado=MediaPlayer.create(this,R.raw.aplastado);
        player.setLooping(true);
        player.start();
        contador=0;
        score=(TextView) findViewById(R.id.tscoreTopo);

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();
        puntaje=preferencias.getLong("puntajetopo",0);
        usuario=preferencias.getString("usuario","No hay usuario");
        level=preferencias.getInt("niveltopo",1);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuario);
        Map<String, Object> newData = new HashMap<>();
        newData.put("puntajetopo", String.valueOf(puntaje));
        newData.put("niveltopo",String.valueOf(level));
        myRef.updateChildren(newData);
        //puntaje=0;
        score.setText("Score: "+ String.valueOf(puntaje));
        // se asignan las animaciones a cada imageview
        for(int i=0;i<id.length;i++){
            Topos[i]=(ImageView) findViewById(id[i]);
            Topos[i].setBackgroundResource(R.drawable.animacion);
            animacionTopo[i] =(AnimationDrawable) Topos[i].getBackground();
        }
        tiempo=(Chronometer) findViewById(R.id.tiempoTopo); // se enlace el cronometro al xml

        //Se genera el onclick de cada imageview
        for(int k=0;k<9;k++){
            Topos[k].setOnClickListener(this);
        }

        //Crear un dialog para comezar
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("¿Estas listo listo?");
        builder.setMessage("Presiona 'Listo' para iniciar el juego\n");
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tiempo.start();
                tiempo.setBase(SystemClock.elapsedRealtime());
                animar();
            }
        });
        builder.setNegativeButton("Aun no estoy listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        CountDownTimer countDownTimer=new CountDownTimer(800,200) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                animar();
            }
        };
        countDownTimer.start();
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
        if(aux1-contador<=950 && numeroaux==numeroaux2){
            animacionTopo[numeroaux].stop();
            //aplastado.stop();
            aplastado.start();


            final int numerito=numeroaux;
            Topos[numeroaux].setBackgroundResource(R.drawable.golpe);
            puntaje+=5;
            score.setText("Score: "+ String.valueOf(puntaje));
           // Toast.makeText(this,"Buena",Toast.LENGTH_SHORT).show();

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
        startActivity(intent);
        finish();
    }
}