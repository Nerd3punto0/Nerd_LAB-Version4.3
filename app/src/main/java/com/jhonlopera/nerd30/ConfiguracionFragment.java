package com.jhonlopera.nerd30;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ConfiguracionFragment extends Fragment implements View.OnClickListener {
    private LinearLayout editarnombre,lineareliminarc;
    OpenInterface openInterface;
    private EditText nuevonombre;
    Button confirmar,cambiarnombre,eliminar,cancelar,confirmareliminar;
    private String nombre;
    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_configuracion, container, false);
        editarnombre=(LinearLayout) view.findViewById(R.id.lineareditarnombre);
        editarnombre.setVisibility(View.GONE);
        nuevonombre=(EditText) view.findViewById(R.id.enuevonombre);
        confirmar=(Button) view.findViewById(R.id.btconfirmar);
        cambiarnombre=(Button) view.findViewById(R.id.btcambiarnombre);
        eliminar=(Button) view.findViewById(R.id.beliminar);
        cancelar=(Button) view.findViewById(R.id.btcancelar);
        lineareliminarc=(LinearLayout) view.findViewById(R.id.lineareliminar);
        confirmareliminar=(Button) view.findViewById(R.id.bconfirmareliminar);

        confirmar.setOnClickListener(this);
        cambiarnombre.setOnClickListener(this);
        eliminar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        confirmareliminar.setOnClickListener(this);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            openInterface = (OpenInterface) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+"must implemented comunicator");
        }
    }

    @Override
    public void onClick(View v) {

        if (v==cambiarnombre){
            lineareliminarc.setVisibility(View.GONE);
            editarnombre.setVisibility(View.VISIBLE);
        }
        if(v==cancelar){
            editarnombre.setVisibility(View.GONE);
        }
        if(v==confirmar){

            if (TextUtils.isEmpty((nuevonombre.getText().toString()))) {
                nuevonombre.setError("Ingrese nuevo nombre");
            }
            else{
                nombre=nuevonombre.getText().toString();
                openInterface.cambiarnombre(nombre);
                editarnombre.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Cambio exitoso",Toast.LENGTH_SHORT).show();
            }
        }if(v==eliminar){

            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("Advertencia");
            builder.setMessage("¿Estas seguro que deseas eliminar tu cuenta?\nSe perderan todos tus datos");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openInterface.eliminardatos();
                    Toast.makeText(getContext(),"Usuario eliminado",Toast.LENGTH_SHORT).show();
                    openInterface.cerrarSesion();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            AlertDialog dialog= builder.create();
            dialog.show();
            }



    }
}
