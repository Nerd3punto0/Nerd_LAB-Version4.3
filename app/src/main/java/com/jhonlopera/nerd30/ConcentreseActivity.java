package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//public class ConcentreseActivity extends AppCompatActivity {
public class ConcentreseActivity extends PrincipalActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

/**/    FragmentManager fm;
    FragmentTransaction ft;

    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int nivelcon;
    long puntaje;
    Button button[] = new Button[42];
    boolean isButtonClicked[]= new boolean[42];
    int presionados=0, img1=0, img2=0, pos1=0, pos2=0, auxid=0, numeroBotones=0, correctas=0;
    int imagenporDefecto=0;
    Button auxbutton=null;
    View auxv;

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

        //Toast.makeText(this, "onCreateJuego", Toast.LENGTH_SHORT).show();
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameprincipal);
        getLayoutInflater().inflate(R.layout.activity_concentrese, contentFrameLayout);
        ft.remove(fragment1).commit(); //se remueve el fragment que se inicia por defecto en el oncreate de principal
        getSupportActionBar().setTitle("Concentrese");

        imagenporDefecto=R.drawable.concentrese_question;

        tpuntaje=(TextView) findViewById(R.id.tpuntaje);
        tpuntaje.setText("Puntaje: "+String.valueOf(puntaje));

        //Enlazo widgets con variables tipo botón------
        //fondocon =(LinearLayout) findViewById(R.id.fondoconcentrese);

        for(int i=0;i<42;i++){
            button[i] = (Button) findViewById(buttonsId[i]);
            button[i].setSoundEffectsEnabled(true);
        }
        //----------------------------------

        // Se define el archivo "Preferencias" donde se almacenaran los valores de las preferencias
        preferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        //se declara instancia el editor de "Preferencias"
        editor_preferencias = preferencias.edit();

        cargarNivel();
        //Toast.makeText(this, "Se cargó el nivel", Toast.LENGTH_SHORT).show();
    }

    private void cargarNivel() {
        //Obtengo el nivel del juego de preferencias
        nivelcon=preferencias.getInt("nivelCon",1);
        //Log.d("nivelconCargar",String.valueOf(nivelcon));

        //Inicia la interfaz del juego según el nivel
        //Nivel 1: 3x4
        if(nivelcon==1){
            Toast.makeText(this, "NIVEL 1", Toast.LENGTH_SHORT).show();
            int numTag=0;
            Collections.shuffle(pictures);
            mostrar.clear();
            for(int i=0, j=0; j<6; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);

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
            Collections.shuffle(pictures);
            mostrar.clear();
            for(int i=0, j=0; j<10; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);

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
            Collections.shuffle(pictures);
            mostrar.clear();
            for(int i=0, j=0; j<15; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);

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
        else if(nivelcon==4 || nivelcon==5){
            Toast.makeText(this, "NIVEL 4", Toast.LENGTH_SHORT).show();
            int numTag=0;
            Collections.shuffle(pictures);
            mostrar.clear();
            for(int i=0, j=0; j<21; i+=2, j++){
                mostrar.add(i,pictures.get(j));
                mostrar.add(i+1,pictures.get(j));
            }
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);
            Collections.shuffle(mostrar);

            for(int i=0;i<42;i++){
                button[i].setVisibility(View.VISIBLE);
                button[i].setBackgroundResource(imagenporDefecto);
                button[i].setOnClickListener(this);
                button[i].setTag("Button"+numTag);
                numTag++;
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
                        auxbutton.setVisibility(View.INVISIBLE);
                        auxv.setVisibility(View.INVISIBLE);
                        correctas++;
                    }
                    else{
                        auxbutton.setBackgroundResource(imagenporDefecto);
                        auxv.setBackgroundResource(imagenporDefecto);
                    }
                    isButtonClicked[pos1]=false;
                    isButtonClicked[pos2]=false;
                    presionados=0;

                    for(int i=0;i<42;i++){
                        button[i].setEnabled(true);
                    }

                    if(correctas==(numeroBotones/2)){
                        correctas=0;
                        if(nivelcon<5){
                            nivelcon++;
                        }
                        editor_preferencias.putInt("nivelCon",nivelcon).commit();
                        nivelcon=preferencias.getInt("nivelCon",nivelcon);
                        cargarNivel();
                    }
                }
            };
            countDownTimer.start();
        }
    }
}
