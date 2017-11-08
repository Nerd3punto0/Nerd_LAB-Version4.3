package com.jhonlopera.nerd30;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class MenuPrincipalFragment extends Fragment implements View.OnClickListener {

    ImageButton Cuatroimagenes,Concentrese,Topo;
    OpenInterface openInterface;

    public MenuPrincipalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_menu_principal, container, false);
        Cuatroimagenes= (ImageButton) view.findViewById(R.id.b4imagenes);
        Cuatroimagenes.setOnClickListener(this);

        Concentrese=(ImageButton) view.findViewById(R.id.bconcentrese);
        Concentrese.setOnClickListener(this);

        Topo=(ImageButton) view.findViewById(R.id.btopo);
        Topo.setOnClickListener(this);

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        openInterface =(OpenInterface) activity;


    }

    @Override
    public void onClick(View v) {

        if (v==Cuatroimagenes){
            openInterface.OpenCuatroImagenesMenu();
        }else if(v==Concentrese){
            openInterface.OpenConcentreseMenu();
        }else if(v==Topo){
            openInterface.OpenTopoMenu();
        }

    }
}
