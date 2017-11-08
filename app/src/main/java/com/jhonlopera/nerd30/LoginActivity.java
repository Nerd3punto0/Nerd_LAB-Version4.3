package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
    int silog,contador4imagenes;
    Jugador jugador;

    //Para trabajar con firebase
    DatabaseReference myRef;
    FirebaseDatabase database;
    ValueEventListener listener;
    long punaje4imagenes,puntajeConcentrese,puntajeTopo;


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
        //Cargar puntajes
        punaje4imagenes = preferencias.getLong("puntaje4imagenes",0);
        puntajeConcentrese=preferencias.getLong("puntajeConcentrese",0);
        puntajeTopo=preferencias.getLong("puntajeTopo",0);

        //-------------------------------------------------------
        //Si el logggin es con el registro de usuario
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            correoR = extras.getString("correo");
            contraseñaR = extras.getString("contraseña");
            nombreR = extras.getString("nombre");

        }
        //--------------------------------------------------------------------

        //Login con facebook
        /*
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile"); //Permisos del perfil publico
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                log = "facebook";
                silog = 1;
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {


                        } else {
                            try {
                                nombreR = object.getString("name");
                                correoR = object.getString("email");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Profile profile = com.facebook.Profile.getCurrentProfile();
                            urifoto = profile.getProfilePictureUri(400, 400); //foto de tamaño 400x400

                            if ((urifoto == null))
                                foto = null;
                            else
                                foto = urifoto.toString();

                            guardarPreferencias(silog, correoR, nombreR, foto, log);
                            //prueba = preferencias.getString("nombre", "noseobtuvo");
                           // Toast.makeText(getApplicationContext(), prueba, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            //intent.putExtra("correo", correoR);
                            //intent.putExtra("nombre", nombreR);
                            //intent.putExtra("foto", foto);
                            //intent.putExtra("log", log);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login  cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error en el login", Toast.LENGTH_SHORT).show();
            }
        });
*/
        //Para loggin con google
        //-------------------------------------------------------------------------------------------
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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //-------------------------------------------------------------------------------------------
    }

    public void iniciar(View view) {

        correo = ecorreo.getText().toString();
        contraseña = econtraseña.getText().toString();
        log = "registro";
        silog = 1;
        contraseñaR = preferencias.getString("contraseña", "/((&!=");
        correoR = preferencias.getString("correo", "/(/%#%//(%#");

        if (correo.equals(correoR) && contraseña.equals(contraseñaR)) {
            editor_preferencias.putInt("silog", silog);
            editor_preferencias.putString("log", log);
            editor_preferencias.commit();
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Datos incorrectos";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            //registro
            prueba = preferencias.getString("nombre", "no se obtuvo");
            Toast.makeText(getApplicationContext(), prueba, Toast.LENGTH_SHORT).show();
            correoR = preferencias.getString("correo", "no se obtivo");
            contraseñaR = preferencias.getString("contraseña", "no se obtivo");
            nombreR = preferencias.getString("nombre", "no se obtivo");
        } else if (requestCode == RC_SIGN_IN) {//login con google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            //facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //google
        if (result.isSuccess()) {
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

            //Firebase
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Contadores");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    id = dataSnapshot.child("contador").getValue().toString();

                    if(id.equals("0")){// Si no hay ningun usuario en la base de datos simplemente se agrega


                        Toast.makeText(getApplicationContext(),"Se creado un nuevo usuario", Toast.LENGTH_SHORT).show();
                        contador4imagenes = Integer.parseInt(id);
                        //Añadir un un usuario
                        myRef = database.getReference("DatosDeUsuario").child("user" + id);
                        jugador = new Jugador("user" + id, correoR, nombreR, punaje4imagenes,puntajeConcentrese,puntajeTopo);
                        myRef.setValue(jugador);

                        //Guardo el id del jugador en preferencias
                        editor_preferencias.putString("usuario","user"+id);

                        //Actualizo el numero de usuarios en la base de datos
                        contador4imagenes++;
                        myRef = database.getReference("Contadores").child("contador");
                        myRef.setValue(contador4imagenes);
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
                                        //Añadir un un usuario
                                        myRef = database.getReference("DatosDeUsuario").child("user" + String.valueOf(i));
                                        jugador = new Jugador("user" + String.valueOf(i), correoR, nombreR, punaje4imagenes,puntajeConcentrese,puntajeTopo);
                                        myRef.setValue(jugador);
                                        editor_preferencias.putString("usuario","user"+String.valueOf(i)).commit();//Guardo el id del jugador en preferencias
                                        guardarPreferencias(silog, correoR, nombreR, foto, log);
                                        break;

                                    }

                                    else if(correofirebase.equals(correoR)){
                                        numerito=i;
                                        // Se almacena el nombre ya que se puedo haber cambiado
                                        nombreR=dataSnapshot.child("user"+String.valueOf(i)).child("nombre").getValue().toString();

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
                                    //Añadir un un usuario
                                    myRef = database.getReference("DatosDeUsuario").child("user" + id);
                                    jugador = new Jugador("user" + id, correoR, nombreR, punaje4imagenes,puntajeConcentrese,puntajeTopo);
                                    myRef.setValue(jugador);
                                    editor_preferencias.putString("usuario","user"+id).commit();//Guardo el id del jugador en preferencias
                                    //Actualizo el numero de usuarios en la base de datos
                                    contador4imagenes++;
                                    myRef = database.getReference("Contadores").child("contador");
                                    myRef.setValue(contador4imagenes);
                                    guardarPreferencias(silog, correoR, nombreR, foto, log);



                                }else{
                                    //Almaceno los datos en preferencias para cargarlos en el peril
                                    guardarPreferencias(silog, correoR, nombreR, foto, log);
                                    editor_preferencias.putString("usuario","user"+String.valueOf(numerito)).commit();
                                    Toast.makeText(getApplicationContext(),"Este usuario ya existe", Toast.LENGTH_SHORT).show();
                                }
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

            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
        }
    }


    public void Registrarse(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivityForResult(intent, 1234);
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void generarHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.jhonlopera.nerd30",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
        }

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