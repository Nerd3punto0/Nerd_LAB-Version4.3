package com.jhonlopera.nerd30;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class MenuTopoFragment extends Fragment implements View.OnClickListener {

    ImageButton jugar;
    OpenInterface openInterface;

    public MenuTopoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_menu_topo, container, false);
        jugar=(ImageButton) view.findViewById(R.id.imbaplasta);
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
        openInterface.OpenTopo();
    }
}
