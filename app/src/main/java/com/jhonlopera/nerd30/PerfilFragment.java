package com.jhonlopera.nerd30;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.Timer;
import java.util.TimerTask;

public class PerfilFragment extends Fragment implements View.OnClickListener {

    private String correoR,nombreR, foto;
    private TextView tvcorreo,tvnombre;
    private ImageView imagen_perfil;
    private ImageButton nada;
    private  int counter;
    private OpenInterface openInterface;

    public PerfilFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_perfil, container, false);
        Bundle bundle= getArguments();
        nombreR=bundle.getString("nombre");
        correoR=bundle.getString("correo");
        foto=bundle.getString("foto");
        counter=bundle.getInt("contadorbroma");
        tvcorreo=(TextView) view.findViewById(R.id.tvfcorreo);
        tvnombre=(TextView) view.findViewById(R.id.tvfnombre);
        imagen_perfil=(ImageView) view.findViewById(R.id.imagen_perfilf);
        nada=(ImageButton) view.findViewById(R.id.imageButtonNada);
        nada.setOnClickListener(this);
        cambiarnombres(nombreR,correoR);

        if (foto!=null){
            try {
                loadImageFromUrl(foto);

            }catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imagen_perfil.setImageDrawable(getResources().getDrawable(R.drawable.perfil5));
        }
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openInterface =(OpenInterface) activity;
    }

    public void cambiarnombres(String nombreR, String correoR) {
        tvnombre.setText(nombreR);
        tvcorreo.setText(correoR);
    }

    private void loadImageFromUrl(String foto) {
        Glide.with(getActivity()).load(foto).transform(new CircleTransform(getContext())).into(imagen_perfil);

    }

    @Override
    public void onClick(View v) {
        if (v==nada){
            mensajemolestar(counter);
            counter++;
            openInterface.guardarpreferencias(counter);
        }
    }

    private void mensajemolestar(int counter) {

        if(counter==1){
            Toast.makeText(getActivity(),"Enserio, aqui no hay nada",Toast.LENGTH_SHORT).show();
        }
        else if(counter==2){
            Toast.makeText(getActivity(),"¿De nuevo?",Toast.LENGTH_SHORT).show();
        }
        else if(counter==3){
            Toast.makeText(getActivity(),"Insisto, ¡aqui no hay nada!",Toast.LENGTH_SHORT).show();
        }
        else if(counter==4){
            Toast.makeText(getActivity(),"¿Enserio?, ¿tu de nuevo?",Toast.LENGTH_SHORT).show();
        }
        else if(counter==5){
            Toast.makeText(getActivity(),"¡YA!, deja el boton en paz",Toast.LENGTH_SHORT).show();
        }
        else if(counter==6){
            Toast.makeText(getActivity(),"¿No te cansas?",Toast.LENGTH_SHORT).show();
        }
        else if(counter==7){
            Toast.makeText(getActivity(),"¿Cual es tu problema?",Toast.LENGTH_SHORT).show();
        }
        else if(counter==8){
            Toast.makeText(getActivity(),"Te estas volviendo realmente pesado",Toast.LENGTH_SHORT).show();
        }
        else if(counter==9){
            Toast.makeText(getActivity(),"Comienzo a pensar que solo quieres llamar la atención",Toast.LENGTH_SHORT).show();
        }
        else if(counter==10){
            Toast.makeText(getActivity(),"Seguro tu madre no te quiere",Toast.LENGTH_SHORT).show();
        }
        else if(counter==11){
            Toast.makeText(getActivity(),"Ok, fuera de aquí...",Toast.LENGTH_SHORT).show();
            TimerTask task =new TimerTask() {
                @Override
                public void run() {
                    openInterface.cerrarjuego();
                }
            };
            Timer timer =new Timer();
            timer.schedule(task,2000);

        }
        else if(counter==12){
            Toast.makeText(getActivity(),"Te lo advierto, comienzo a cansarme",Toast.LENGTH_SHORT).show();
        }
        else if(counter==13){
            Intent intent=new Intent(getActivity(),SustoActivity.class);
            startActivity(intent);

        }
        else if(counter==14){
            Toast.makeText(getActivity(),"¿Qué debo hacer para que te canses?",Toast.LENGTH_SHORT).show();
        }
        else if(counter==15){
            Toast.makeText(getActivity(),"Si estas leyendo esto es por que eres mateo y eres una loca\nEn esta parte voy aponer el sonido del orgasmo",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"ADIOS",Toast.LENGTH_LONG).show();
            nada.setVisibility(View.GONE);
        }

    }
}
