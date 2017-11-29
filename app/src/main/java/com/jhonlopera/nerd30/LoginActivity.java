package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    private String correoR, contraseñaR, correo, contraseña, nombreR, foto, log, prueba,id;
    int numerousuario;
    int numerito;
    private Uri urifoto;
    private EditText ecorreo, econtraseña;
    int duration = Toast.LENGTH_SHORT;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1035;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int silog,contador4imagenes, contadornivel;
    Jugador jugador;
    private int nivel4img, nivelcon, niveltopo;
    int contadordeespacios=0;
    //Para trabajar con firebase
    DatabaseReference myRef;
    FirebaseDatabase database;
    private long puntaje4imagenes,puntajeConcentrese,puntajeTopo;

    ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(LoginActivity.this);
        ecorreo = (EditText) findViewById(R.id.eCorreo);
        econtraseña = (EditText) findViewById(R.id.eContraseña);




        // Se define el archivo "Preferencias" donde se almacenaran los valores de las preferencias
        preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        //se declara instancia el editor de "Preferencias"
        editor_preferencias = preferencias.edit();




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getApplicationContext(), "Error en el loggin", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signIn();

        signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == 1234 && resultCode == RESULT_OK)
        if (requestCode == RC_SIGN_IN) {
            //login con google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            //login facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater1 = this.getLayoutInflater();
            View mView = inflater1.inflate(R.layout.barra_de_carga,null);
            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            GoogleSignInAccount acct = result.getSignInAccount();
            correoR = acct.getEmail();//obtener email
            nombreR = acct.getDisplayName();
            urifoto = acct.getPhotoUrl();
            log = "google";
            silog = 1;

            if ((urifoto == null)) {
                foto = null;
            } else {
                foto = urifoto.toString();
            }
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Contadores");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    id = dataSnapshot.child("contador").getValue().toString();

                    if(id.equals("0")){ // Si no hay ningun usuario en la base de datos simplemente se agrega

                        Toast.makeText(getApplicationContext(),"Se creado un nuevo usuario", Toast.LENGTH_SHORT).show();
                        contador4imagenes = Integer.parseInt(id);
                        contadornivel=Integer.parseInt(id);
                        //Añadir un un usuario
                        myRef = database.getReference("DatosDeUsuario").child("user" + id);

                        //Para un usuario nuevo se cargan los  valores por defecto para puntaje y nivel
                        valores_por_defecto();
                        jugador = new Jugador("user" + id, correoR, nombreR, puntaje4imagenes,puntajeConcentrese,puntajeTopo,
                                nivel4img, nivelcon, niveltopo);
                        myRef.setValue(jugador);

                        //Guardo el id del jugador en preferencias
                        editor_preferencias.putString("usuario","user"+id);

                        //Actualizo el numero de usuarios en la base de datos
                        contador4imagenes++;
                        contadornivel++;
                        myRef = database.getReference("Contadores").child("contador");
                        myRef.setValue(contadornivel);
                        guardarPreferencias(silog, correoR, nombreR, foto, log);

                    }else{ //Se comprueba si ya existe el correo
                        final int contadora=Integer.parseInt(id);
                        myRef = database.getReference("DatosDeUsuario");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(int i=0;i<contadora;i++){

                                    String correofirebase=dataSnapshot.child("user"+String.valueOf(i)).child("correo").getValue().toString();

                                    if(correofirebase.equals(" ")){
                                        contadordeespacios=i;
                                    }

                                    if(correofirebase.equals(correoR)){
                                        numerito=i;
                                        // Se almacena el nombre ya que se puedo haber y los puntajes
                                        nombreR=dataSnapshot.child("user"+String.valueOf(numerito)).child("nombre").getValue().toString();
                                        puntaje4imagenes=Long.parseLong(dataSnapshot.child("user"+String.valueOf(numerito)).child("puntaje4imagenes").getValue().toString());
                                        editor_preferencias.putLong("puntaje4imagenes",puntaje4imagenes).commit();
                                        puntajeConcentrese=Long.parseLong(dataSnapshot.child("user"+String.valueOf(numerito)).child("puntajeConcentrese").getValue().toString());
                                        editor_preferencias.putLong("puntajeConcentrese",puntajeConcentrese).commit();
                                        puntajeTopo=Long.parseLong(dataSnapshot.child("user"+String.valueOf(numerito)).child("puntajeTopo").getValue().toString());
                                        editor_preferencias.putLong("puntajeTopo",puntajeTopo).commit();
                                        nivel4img= Integer.parseInt(dataSnapshot.child("user" + String.valueOf(numerito)).child("nivel4img").getValue().toString());
                                        editor_preferencias.putInt("nivel4img",nivel4img).commit();
                                        nivelcon= Integer.parseInt(dataSnapshot.child("user" + String.valueOf(numerito)).child("nivelcon").getValue().toString());
                                        editor_preferencias.putInt("nivelcon",nivelcon).commit();
                                        niveltopo= Integer.parseInt(dataSnapshot.child("user" + String.valueOf(numerito)).child("niveltopo").getValue().toString());
                                        editor_preferencias.putInt("niveltopo",niveltopo).commit();
                                        guardarPreferencias(silog, correoR, nombreR, foto, log);
                                        break;
                                    }
                                    else {
                                        numerito=-1; //-1 cuando el usuario no existe
                                    }
                                }

                                if(numerito==-1){
                                    //Si el usuario no existe lo agrego a la base de datos
                                    Toast.makeText(getApplicationContext(),"Se creado un nuevo usuario", Toast.LENGTH_SHORT).show();
                                    contador4imagenes = Integer.parseInt(id);
                                    contadornivel= Integer.parseInt(id);

                                    if(contadordeespacios!=0){
                                        //Añadir un un usuario en un espacio
                                        valores_por_defecto();
                                        myRef = database.getReference("DatosDeUsuario").child("user" + String.valueOf(contadordeespacios));
                                        jugador = new Jugador("user" + String.valueOf(contadordeespacios), correoR, nombreR, puntaje4imagenes,puntajeConcentrese,puntajeTopo,
                                                nivel4img, nivelcon, niveltopo);
                                        myRef.setValue(jugador);
                                        editor_preferencias.putString("usuario","user"+String.valueOf(contadordeespacios)).commit();//Guardo el id del jugador en preferencias
                                        guardarPreferencias(silog, correoR, nombreR, foto, log);

                                    }else{
                                        //Añadir un un usuario en una nueva posicion
                                        valores_por_defecto();
                                        myRef = database.getReference("DatosDeUsuario").child("user" + id);
                                        jugador = new Jugador("user" + id, correoR, nombreR, puntaje4imagenes,puntajeConcentrese,puntajeTopo,
                                                nivel4img, nivelcon, niveltopo);
                                        myRef.setValue(jugador);
                                        editor_preferencias.putString("usuario","user"+id).commit();//Guardo el id del jugador en preferencias
                                        //Actualizo el numero de usuarios en la base de datos
                                        contador4imagenes++;
                                        contadornivel++;
                                        myRef = database.getReference("Contadores").child("contador");
                                        myRef.setValue(contadornivel);
                                        guardarPreferencias(silog, correoR, nombreR, foto, log);
                                    }

                                }else{
                                    //Almaceno los datos en preferencias para cargarlos en el peril
                                    guardarPreferencias(silog, correoR, nombreR, foto, log);
                                    editor_preferencias.putString("usuario","user"+String.valueOf(numerito)).commit();
                                    Toast.makeText(getApplicationContext(),"Este usuario ya existe", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
        }
    }

    private void valores_por_defecto() {
        puntaje4imagenes = 0; puntajeConcentrese=0; puntajeTopo=0;
        nivel4img=1; nivelcon=1;
        niveltopo=1;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void guardarPreferencias(int silog, String correo, String nombre, String foto, String log) {
        editor_preferencias.putInt("silog", silog);
        editor_preferencias.putString("correo", correo);
        editor_preferencias.putString("nombre", nombre);
        editor_preferencias.putString("foto", foto);
        editor_preferencias.putString("log", log);
        editor_preferencias.commit();
    }
}