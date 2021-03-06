package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//public class ConcentreseActivity extends AppCompatActivity {
public class ConcentreseActivity extends AppCompatActivity implements  View.OnClickListener{

/**/    FragmentManager fm;
    FragmentTransaction ft;

    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int nivelcon;
    long puntaje, tiempotranscurrido;
    Button button[] = new Button[42];
    boolean isButtonClicked[]= new boolean[42];
    int presionados=0, img1=0, img2=0, pos1=0, pos2=0, auxid=0, numeroBotones=0, correctas=0, incorrectas=0;
    int imagenporDefecto=0;
    Button auxbutton=null;
    View auxv;
    Chronometer tiempo;
    private int estadomusica;
    private MediaPlayer musica,efecto;
    private String usuario;
    DatabaseReference myRef;
    FirebaseDatabase database;

    int buttonsId[]={R.id.img1,R.id.img2,R.id.img3,R.id.img4,R.id.img5,R.id.img6,
            R.id.img7,R.id.img8,R.id.img9,R.id.img10,R.id.img11,R.id.img12,
            R.id.img13,R.id.img14,R.id.img15,R.id.img16,R.id.img17,R.id.img18,
            R.id.img19,R.id.img20,R.id.img21,R.id.img22,R.id.img23,R.id.img24,
            R.id.img25,R.id.img26,R.id.img27,R.id.img28,R.id.img29,R.id.img30,
            R.id.img31,R.id.img32,R.id.img33,R.id.img34,R.id.img35,R.id.img36,
            R.id.img37,R.id.img38,R.id.img39,R.id.img40,R.id.img41,R.id.img42};

    List<Integer> pictures = Arrays.asList(R.drawable.concentrese_astronauta,R.drawable.concentrese_batman,R.drawable.concentrese_bombero,
            R.drawable.concentrese_chickenhawk,R.drawable.concentrese_garfield, R.drawable.concentrese_hulk,
            R.drawable.concentrese_lisa,R.drawable.concentrese_lobo,R.drawable.concentrese_luigi,
            R.drawable.concentrese_mago,R.drawable.concentrese_marciano,R.drawable.concentrese_pajaroloco,
            R.drawable.concentrese_panterarosa,R.drawable.concentrese_perro,R.drawable.concentrese_piolin,
            R.drawable.concentrese_samurai,R.drawable.concentrese_shrek,R.drawable.concentrese_spiderman,
            R.drawable.concentrese_ubuntu,R.drawable.concentrese_ubuntu1,R.drawable.concentrese_vaca2);

    List<Integer> mostrar = new ArrayList<>();
    LinearLayout fondocon;
    TextView tpuntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_concentrese);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_concentrese);
        getSupportActionBar().setTitle("Concentrese");


        imagenporDefecto=R.drawable.concentrese_question;

        //Enlazo widgets con variables tipo botón------
        tpuntaje=(TextView) findViewById(R.id.tpuntos);
        tiempo=(Chronometer) findViewById(R.id.tiempoConcentrese);
        tpuntaje.setText(String.valueOf(puntaje));
        //fondocon =(LinearLayout) findViewById(R.id.fondoconcentrese);
        for(int i=0;i<42;i++){
            button[i] = (Button) findViewById(buttonsId[i]);
            button[i].setSoundEffectsEnabled(true);
        }
        //----------------------------------

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();

        puntaje=preferencias.getLong("puntajeConcentrese",0);
        tpuntaje.setText(String.valueOf(puntaje));
        estadomusica=preferencias.getInt("estadosonido",1);
        musica= MediaPlayer.create(this, R.raw.juegoconcentresecut);
        efecto=MediaPlayer.create(this,R.raw.sonidopaginac);
        musica.setVolume(1,20);
        efecto.setVolume(1,30);
        usuario=preferencias.getString("usuario","No hay usuario");
        nivelcon=preferencias.getInt("nivelcon",1);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DatosDeUsuario").child(usuario);
        Map<String, Object> newData = new HashMap<>();
        newData.put("puntajeConcentrese", puntaje);
        newData.put("nivelcon",nivelcon);
        myRef.updateChildren(newData);

        if (estadomusica==1){
            musica.setLooping(true);
            musica.start();
        }

        cargarNivel();
        //Toast.makeText(this, "Se cargó el nivel", Toast.LENGTH_SHORT).show();
    }

    private void cargarNivel() {

        Random random = new Random();
        int aleatorio;
        aleatorio = random.nextInt(7)+3;
        tiempo.start();

        //Obtengo el nivel del juego de preferencias
        nivelcon=preferencias.getInt("nivelcon",1);
        //Log.d("nivelconCargar",String.valueOf(nivelcon));

        //Inicia la interfaz del juego según el nivel
        //Nivel 1: 3x4
        if(nivelcon==1){
            Toast.makeText(this, "NIVEL 1", Toast.LENGTH_SHORT).show();
            int numTag=0;

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(pictures);
            }

            mostrar.clear();

            for(int i=0, j=0; j<6; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(mostrar);
            }

            for(int i=0;i<42;i++){
                if((i>12 && i<17)||(i>18 && i<23)||(i>24 && i<29)){
                    button[i].setVisibility(View.VISIBLE);
                    button[i].setBackgroundResource(imagenporDefecto);
                    button[i].setOnClickListener(this);
                    button[i].setTag("Button"+numTag);
                    numTag++;
                }else{
                    button[i].setVisibility(View.INVISIBLE);
                }
            }
        }

        //Nivel 2: 5x4
        else if(nivelcon==2){
            Toast.makeText(this, "NIVEL 2", Toast.LENGTH_SHORT).show();
            int numTag=0;

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(pictures);
            }

            mostrar.clear();

            for(int i=0, j=0; j<10; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(mostrar);
            }

            for(int i=0;i<42;i++){
                if((i>6 && i<11)||(i>12 && i<17)||(i>18 && i<23)||(i>24 && i<29)||(i>30 && i<35)){
                    button[i].setVisibility(View.VISIBLE);
                    button[i].setBackgroundResource(imagenporDefecto);
                    button[i].setOnClickListener(this);
                    button[i].setTag("Button"+numTag);
                    numTag++;
                }else{
                    button[i].setVisibility(View.INVISIBLE);
                }
            }
        }

        //Nivel 3: 5x6
        else if(nivelcon==3){
            Toast.makeText(this, "NIVEL 3", Toast.LENGTH_SHORT).show();
            int numTag=0;

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(pictures);
            }

            mostrar.clear();

            for(int i=0, j=0; j<15; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(mostrar);
            }

            for(int i=0;i<42;i++){
                if(i>5 && i<36){
                    button[i].setVisibility(View.VISIBLE);
                    button[i].setBackgroundResource(imagenporDefecto);
                    button[i].setOnClickListener(this);
                    button[i].setTag("Button"+numTag);
                    numTag++;
                }else{
                    button[i].setVisibility(View.INVISIBLE);
                }
            }
        }

        //Nivel 4: 7x6
        else if(nivelcon==4 || nivelcon==5) {
            Toast.makeText(this, "ÚLTIMO NIVEL: NIVEL 4", Toast.LENGTH_SHORT).show();
            int numTag = 0;

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(pictures);
            }

            mostrar.clear();

            for (int i = 0, j = 0; j < 21; i += 2, j++) {
                mostrar.add(i, pictures.get(j));
                mostrar.add(i + 1, pictures.get(j));
            }

            for(int i=0;i<aleatorio;i++){
                Collections.shuffle(mostrar);
            }

            for (int i = 0; i < 42; i++) {
                button[i].setVisibility(View.VISIBLE);
                button[i].setBackgroundResource(imagenporDefecto);
                button[i].setOnClickListener(this);
                button[i].setTag("Button" + numTag);
                numTag++;
            }

            if(nivelcon==5){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("YA PASASTE UNA VEZ EL ÚLTIMO NIVEL");
                builder.setMessage("Desde este momento tu puntaje no se actualizará pero podrás seguir jugando en el nivel 4. " +
                        "¿Deseas volver al nivel 1? (Al presionar SÍ, se reiniciará el puntaje)");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nivelcon=1;
                        editor_preferencias.putInt("nivelcon",nivelcon).commit();
                        puntaje=0;
                        tpuntaje.setText(String.valueOf(puntaje));
                        editor_preferencias.putLong("puntajeConcentrese",puntaje).apply();
                        tiempo.setBase(SystemClock.elapsedRealtime());
                        myRef = database.getReference("DatosDeUsuario").child(usuario);
                        Map<String, Object> newData = new HashMap<>();
                        newData.put("puntajeConcentrese", puntaje);
                        newData.put("nivelcon",nivelcon);
                        myRef.updateChildren(newData);
                        cargarNivel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tiempo.setBase(SystemClock.elapsedRealtime());
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "Estoy en OnClick", Toast.LENGTH_SHORT).show();
        auxv=v;
        String tag = String.valueOf(v.getTag());
        //Log.d("tagBoton",tag);
        int auxpos=0;
        boolean verificar=false;

        //Log.d("nivelcononClick",String.valueOf(nivelcon));

        //Se define numero de botones por nivel
        if(nivelcon==1) numeroBotones=12;
        else if(nivelcon==2) numeroBotones=20;
        else if(nivelcon==3) numeroBotones=30;
        else if(nivelcon==4 || nivelcon==5) numeroBotones=42;
        //Log.d("numeroBotones",String.valueOf(numeroBotones));

        //Cuando se presiona un nivel del botón, se verifica cual botón se presionó y se toma una acción;
        for(int i=0;i<numeroBotones;i++){
            if(tag.equals("Button"+String.valueOf(i))){
                auxpos=i;
                isButtonClicked[i] = !isButtonClicked[i];
                v.setBackgroundResource(isButtonClicked[i]? mostrar.get(i) : imagenporDefecto);

                //v.setBackgroundColor(isButtonClicked[i]? Color.WHITE: Color.GREEN);
                //v.setBackgroundResource(nombre3);
                //fondocon.setBackgroundColor(Color.GREEN); //tpuntaje.setText("Eres la hostia");
            }
        }

        for(int j=1; j<numeroBotones; j++){
            verificar = verificar | isButtonClicked[j-1] | isButtonClicked[j];
        }

        if(verificar){
            presionados=presionados+1;
        }
        else{
            presionados=0;
        }

        if(presionados==0){
            auxid=0;
            auxbutton=null;
        }
        else if(presionados==1){
            pos1=auxpos;
            auxid = v.getId();
            auxbutton = (Button) findViewById(auxid);
            img1=mostrar.get(pos1);
        }

        else if(presionados==2){
            pos2=auxpos;
            img2=mostrar.get(pos2);
            auxv=v;

            for(int i=0;i<42;i++){
                button[i].setEnabled(false);
            }

            CountDownTimer countDownTimer = new CountDownTimer(500,100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //Toast.makeText(ConcentreseActivity.this, "acá sí", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    //Toast.makeText(ConcentreseActivity.this, "acá también", Toast.LENGTH_SHORT).show();
                    if(img2==img1){
                        if (estadomusica==1){
                            efecto.start();
                        }
                        auxbutton.setVisibility(View.INVISIBLE);
                        auxv.setVisibility(View.INVISIBLE);
                        correctas++;

                    }
                    else{
                        auxbutton.setBackgroundResource(imagenporDefecto);
                        auxv.setBackgroundResource(imagenporDefecto);
                        incorrectas++;
                    }
                    isButtonClicked[pos1]=false;
                    isButtonClicked[pos2]=false;
                    presionados=0;

                    for(int i=0;i<42;i++){
                        button[i].setEnabled(true);
                    }

                    if(correctas==(numeroBotones/2)){
                        tiempotranscurrido=(SystemClock.elapsedRealtime()-tiempo.getBase())/1000;
                        if(nivelcon<5){
                            puntaje=puntaje+((correctas*50)-tiempotranscurrido-incorrectas)*nivelcon;
                            if(puntaje<0){
                                puntaje=nivelcon*10;
                            }
                            editor_preferencias.putLong("puntajeConcentrese",puntaje).apply();
                            tpuntaje.setText(String.valueOf(puntaje));
                            nivelcon++;
                        }
                        correctas=0;
                        incorrectas=0;
                        tiempo.setBase(SystemClock.elapsedRealtime());
                        editor_preferencias.putInt("nivelcon",nivelcon).apply();
                        //nivelcon=preferencias.getInt("nivelcon",nivelcon);
                        myRef = database.getReference("DatosDeUsuario").child(usuario);
                        Map<String, Object> newData = new HashMap<>();
                        newData.put("puntajeConcentrese", puntaje);
                        newData.put("nivelcon",nivelcon);
                        myRef.updateChildren(newData);
                        cargarNivel();
                    }
                }
            };
            countDownTimer.start();
        }
    }

    public void onBackPressed(){
        musica.stop();
        Intent intent=new Intent(this,PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        musica.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        musica.stop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        if (estadomusica==1){
            musica = MediaPlayer.create(this, R.raw.sonido1);
            musica.setLooping(true);
            musica.start();
        }
        super.onRestart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
