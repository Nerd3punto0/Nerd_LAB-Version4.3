package com.jhonlopera.nerd30;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Menu4imagenesFragment extends Fragment implements View.OnClickListener {

    ImageButton jugar;
    OpenInterface openInterface;
    public Menu4imagenesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu4imagenes, container, false);
        jugar=(ImageButton) view.findViewById(R.id.b4imagenesplay);
        jugar.setOnClickListener(this);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openInterface =(OpenInterface) activity;
    }

    @Override
    public void onClick(View v) {
        openInterface.OpenCuantroImagenes();
    }
}
