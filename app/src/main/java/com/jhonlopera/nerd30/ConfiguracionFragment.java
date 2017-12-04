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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class ConfiguracionFragment extends Fragment implements View.OnClickListener {
    private LinearLayout editarnombre,linearcambiarnombre;
    OpenInterface openInterface;
    private EditText nuevonombre;
    Button confirmar,cambiarnombre,eliminar,cancelar;
    private String nombreR,foto;
    private TextView tvnombre;
    private ImageView imagen_perfil;
    private String nombre;
    private RadioButton activarsonido,desactivarsonido;
    private int estadosonido;
    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_configuracion, container, false);
        Bundle bundle= getArguments();
        nombreR=bundle.getString("nombre");
        foto=bundle.getString("foto");
        estadosonido=bundle.getInt("estadosonido");
        imagen_perfil=(ImageView) view.findViewById(R.id.fotodeperfilconf);
        editarnombre=(LinearLayout) view.findViewById(R.id.lineareditarnombre);
        editarnombre.setVisibility(View.GONE);
        nuevonombre=(EditText) view.findViewById(R.id.enuevonombre);
        tvnombre=(TextView) view.findViewById(R.id.tnombreconf);
        tvnombre.setText(nombreR);
        confirmar=(Button) view.findViewById(R.id.btconfirmar);
        cambiarnombre=(Button) view.findViewById(R.id.btcambiarnombre);
        eliminar=(Button) view.findViewById(R.id.beliminar);
        cancelar=(Button) view.findViewById(R.id.btcancelar);
        linearcambiarnombre=(LinearLayout) view.findViewById(R.id.linearcambiarnombre);
        activarsonido=(RadioButton) view.findViewById(R.id.activarsonido);
        desactivarsonido=(RadioButton) view.findViewById(R.id.desactivarsonido);
        cargarfoto(foto);

        if(estadosonido==1){
            activarsonido.setChecked(true);
        }else
            {
                desactivarsonido.setChecked(true);
            }
        confirmar.setOnClickListener(this);
        cambiarnombre.setOnClickListener(this);
        eliminar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        activarsonido.setOnClickListener(this);
        desactivarsonido.setOnClickListener(this);

        return view;
    }

    private void cargarfoto(String foto) {
        if (foto!=null){
            try {
                loadImageFromUrl(foto);

            }catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imagen_perfil.setImageDrawable(getResources().getDrawable(R.drawable.perfil5));
        }
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

    private void loadImageFromUrl(String foto) {
        Glide.with(getActivity()).load(foto).transform(new CircleTransform(getContext())).into(imagen_perfil);
    }


    @Override
    public void onClick(View v) {

        if (v==activarsonido){
            estadosonido=1;
            openInterface.estadomusica(estadosonido);
        }
        if(v==desactivarsonido){
            estadosonido=0;
            openInterface.estadomusica(estadosonido);
        }

        if (v==cambiarnombre){
            linearcambiarnombre.setVisibility(View.GONE);
            editarnombre.setVisibility(View.VISIBLE);
        }
        if(v==cancelar){
            linearcambiarnombre.setVisibility(View.VISIBLE);
            editarnombre.setVisibility(View.GONE);
        }
        if(v==confirmar){

            if (TextUtils.isEmpty((nuevonombre.getText().toString()))) {
                nuevonombre.setError("Ingrese nuevo nombre");
            }
            else{
                linearcambiarnombre.setVisibility(View.VISIBLE);
                nombre=nuevonombre.getText().toString();
                openInterface.cambiarnombre(nombre);
                editarnombre.setVisibility(View.GONE);
                tvnombre.setText(nombre);
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
