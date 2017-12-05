package com.jhonlopera.nerd30;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuatroImagenesActivity extends AppCompatActivity implements   View.OnClickListener {

    private ImageView im[]=new ImageView[4];
    Button bavanzar,bdel;
    LinearLayout layoutletra[]=new LinearLayout[7];
    private int idletras[]={R.id.tletra1,R.id.tletra2,R.id.tletra3,R.id.tletra4,R.id.tletra5,R.id.tletra6,R.id.tletra7};
    private int idimagenes[]={R.id.im1,R.id.im2,R.id.im3,R.id.im4};
    private int idbotones[]={R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,R.id.b9,R.id.b10};
    private int idlayouts[]={R.id.linear1,R.id.linear2,R.id.linear3,R.id.linear4,R.id.linear5,R.id.linear6,R.id.linear7};
    Chronometer tiempo;
    private int idimagennivel[]={R.drawable.nivel1_1,R.drawable.nivel1_2,R.drawable.nivel1_2,R.drawable.nivel1_4,
                                 R.drawable.nivel2_1,R.drawable.nivel2_2,R.drawable.nivel2_3,R.drawable.nivel2_4,
                                 R.drawable.nivel3_1,R.drawable.nivel3_2,R.drawable.nivel3_3,R.drawable.nivel3_4,
                                 R.drawable.nivel4_1,R.drawable.nivel4_2,R.drawable.nivel4_3,R.drawable.nivel4_4,
                                 R.drawable.nivel5_1,R.drawable.nivel5_2,R.drawable.nivel5_3,R.drawable.nivel5_4};
    private String lista_palabras[]={"ROJO","TIRO","PRESA","TRAMPA","EMOCION"};
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    DatabaseReference myRef;
    FirebaseDatabase database;
    int level;
    int casillanumero=0;
    final Button botones[]=new Button[10];
    final  TextView letras[] =new TextView[7];
    TextView score;
    public MediaPlayer player;
    String palabra;
    String palabracorrecta="";
    long puntaje=0;
    long puntajeaux=0;
    long p;
    String usuario;
    int estadomusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_cuatro_imagenes);
        getSupportActionBar().setTitle("4 imágenes 1 plabra");

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();
        estadomusica=preferencias.getInt("estadosonido",1);
        puntaje=preferencias.getLong("puntaje4imagenes",0);
        usuario=preferencias.getString("usuario","No hay usuario");
        level=preferencias.getInt("nivel4img",1);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuario);
        Map<String, Object> newData = new HashMap<>();
        newData.put("puntaje4imagenes", puntaje);
        newData.put("nivel4img",level);
        myRef.updateChildren(newData);

        for (int cont=0;cont<letras.length;cont++){
            //Casillas de las letras
            letras[cont]=(TextView) findViewById(idletras[cont]);
        }
        for (int cont=0;cont<im.length;cont++){
            //imagenes
            im[cont]=(ImageView) findViewById(idimagenes[cont]);
        }
        //botones
        for (int cont=0;cont<botones.length;cont++){
            botones[cont]=(Button) findViewById(idbotones[cont]);
        }

        for (int cont=0;cont<layoutletra.length;cont++){
            layoutletra[cont]=(LinearLayout) findViewById(idlayouts[cont]);
        }
        bavanzar=(Button) findViewById(R.id.bavanzar);
        bdel=(Button) findViewById(R.id.bdel);

        //Musica
        player = MediaPlayer.create(this, R.raw.sonido1);
        if (estadomusica==1){
            player.setLooping(true);
            player.start();
        }

        tiempo=(Chronometer) findViewById(R.id.tiempo);//Tiempo

        //puntaje
        score=(TextView) findViewById(R.id.tscore);
        score.setText("Puntaje: "+ String.valueOf(puntaje));
        if (level<=5) {
            palabra = cargarnivel(level);
            tiempo.start();
        }else{
            palabra = cargarnivel(level-1);
            final Intent intent=new Intent(this,CuatroImagenesActivity.class);
            final Intent intent2=new Intent(this,PrincipalActivity.class);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            builder.setTitle("¡Felicitaciones!");
            builder.setMessage("Lamentablemente esto es una aplicacion de prueba así que contamos pocos niveles... por ahora." +
                    "\n\n¿Deseas reiniciar el juego y mejorar tu puntaje?");

            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    level=1;
                    puntaje=0;
                    editor_preferencias.putInt("nivel4img",level).apply();
                    editor_preferencias.putLong("puntaje4imagenes",puntaje).apply();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    finish();
                }
            });
            AlertDialog dialog= builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }

        bavanzar.setOnClickListener(this);
        bdel.setOnClickListener(this);

        for (int i=0;i<10;i++){
            botones[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if(v==bavanzar){
            for (int i=0;i<palabra.length();i++){
                palabracorrecta+=letras[i].getText();
            }
            if(palabracorrecta.equals(palabra)){
                puntajeaux=SystemClock.elapsedRealtime()-tiempo.getBase();
                puntajeaux=(puntajeaux/1000);
                if (puntajeaux<10){
                    puntaje+=100*level;
                }
                else{
                    p=100*level;
                    for(int n=10;n<puntajeaux;n++){
                        p=p-(n-5);

                        if(p<=10){
                            p=10;
                            break;
                        }
                    }
                    puntaje+=p;
                }

                editor_preferencias.putLong("puntaje4imagenes",puntaje).apply();
                score.setText("Score: "+String.valueOf(puntaje));
                Toast.makeText(this,"GOOD!",Toast.LENGTH_SHORT).show();

                for (int k=0;k<palabra.length();k++){
                    letras[k].setText("");
                }

                for (int j=0;j<10;j++){
                    botones[j].setVisibility(View.VISIBLE);
                }

                if (level==5){
                    level++;
                    final Intent intent=new Intent(this,CuatroImagenesActivity.class);
                    final Intent intent2=new Intent(this,PrincipalActivity.class);
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("¡Felicitaciones!");
                    builder.setMessage("Lamentablemente esto es una aplicacion de prueba así que contamos pocos niveles... por ahora." +
                                        "\n\n¿Deseas reiniciar el juego y meejorar tu puntaje?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            level=1;
                            puntaje=0;
                            editor_preferencias.putInt("nivel4img",level).apply();
                            editor_preferencias.putLong("puntaje4imagenes",puntaje).apply();

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent2);
                            finish();
                           // level=6;


                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                    editor_preferencias.putInt("nivel4img",level).apply();
                    editor_preferencias.putLong("puntaje4imagenes",puntaje).apply();
                    score.setText("Puntaje: " + String.valueOf(puntaje));

                }else{
                    level++;
                }
                myRef = database.getReference("DatosDeUsuario").child(usuario);
                Map<String, Object> newData = new HashMap<>();
                newData.put("puntaje4imagenes", puntaje);
                newData.put("nivel4img",level);
                myRef.updateChildren(newData);

                editor_preferencias.putInt("nivel4img",level).apply();
                if(level<=5){
                    palabra=cargarnivel(level);}
                palabracorrecta="";
                casillanumero=0;
                tiempo.setBase(SystemClock.elapsedRealtime());
            }else{
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
            }

        }
        else if(v==bdel){
            casillanumero=0;
            palabracorrecta="";
            for (int i=0;i<10;i++){
                botones[i].setVisibility(View.VISIBLE);
            }

            for (int j=0;j<palabra.length();j++){
                letras[j].setText("");
            }
        }
        else {

            for (int i=0;i<10;i++){
                if(casillanumero<7){
                    if(v==botones[i]){
                        letras[casillanumero].setText(botones[i].getText());
                        botones[i].setVisibility(View.INVISIBLE);
                        casillanumero++;
                    }
                }
            }
        }
    }

    private String cargarnivel(int level) {

        String palabra="";
        palabra=lista_palabras[level-1];
        cambiarletras(palabra);

        for (int cont=0;cont<layoutletra.length;cont++){
            layoutletra[cont].setVisibility(View.GONE);
        }
        for (int cont=0;cont<palabra.length();cont++){
            layoutletra[cont].setVisibility(View.VISIBLE);
        }
        int i=0;
        for (int cont=(level-1)*4;cont<level*4;cont++,i++){
            im[i].setImageDrawable(getResources().getDrawable(idimagennivel[cont]));
        }
        return palabra;
    }

    private void cambiarletras(String palabra) {

        for (int i=palabra.length();i<10;i++){
            palabra=palabra+aleatorio();
        }

        String palabraper=shuffle(palabra);
        String letra;
        for (int j=0;j<10;j++){
            letra=String.valueOf(palabraper.charAt(j));
            botones[j].setText(letra);
        }
    }

    private String aleatorio(){
        //Colocar letras aleatorias
        int num1 = 65;
        int num2 = 90;
        int numAleatorio = (int)Math.floor(Math.random()*(num2 -num1)+num1);
        char letra=(char)numAleatorio;
        return String.valueOf(letra);

    }

    private static String shuffle(String str) {
        List<String> letters = Arrays.asList(str.split(""));
        Collections.shuffle(letters);
        String salida = "";
        for (String s : letters)
            salida += s;
        return salida;
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
        player.pause();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
