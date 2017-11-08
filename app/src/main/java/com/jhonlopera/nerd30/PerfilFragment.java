package com.jhonlopera.nerd30;

import android.content.SharedPreferences;
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


public class PerfilFragment extends Fragment implements View.OnClickListener {

    private String correoR,nombreR, foto;

    private TextView tvcorreo,tvnombre;
    private ImageView imagen_perfil;
    ImageButton nada;
    private  int counter=1;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle= getArguments();
        nombreR=bundle.getString("nombre");
        correoR=bundle.getString("correo");
        foto=bundle.getString("foto");



        View view= inflater.inflate(R.layout.fragment_perfil, container, false);
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
        }
        else {
            imagen_perfil.setImageDrawable(getResources().getDrawable(R.drawable.perfil5));
        }

        return view;
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

        }
    }

    private void mensajemolestar(int counter) {
        if(counter==1){
            Toast.makeText(getActivity(),"Enserio, aqui no hay nada",Toast.LENGTH_LONG).show();
        }
        else if(counter==2){
            Toast.makeText(getActivity(),"¿De nuevo?",Toast.LENGTH_LONG).show();
        }
        else if(counter==3){
            Toast.makeText(getActivity(),"Insisto, ¡aqui no hay nada!",Toast.LENGTH_LONG).show();
        }
        else if(counter==4){
            Toast.makeText(getActivity(),"¿Enserio?, ¿tu de nuevo?",Toast.LENGTH_LONG).show();
        }
        else if(counter==5){
            Toast.makeText(getActivity(),"¡YA!, deja el boton en paz",Toast.LENGTH_LONG).show();
        }
        else if(counter==6){
            Toast.makeText(getActivity(),"¿No te cansas?",Toast.LENGTH_LONG).show();
        }
        else if(counter==7){
            Toast.makeText(getActivity(),"¿Cual es tu problema?",Toast.LENGTH_LONG).show();
        }

    }
}
