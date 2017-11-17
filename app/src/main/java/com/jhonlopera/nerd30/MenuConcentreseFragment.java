package com.jhonlopera.nerd30;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class MenuConcentreseFragment extends Fragment implements View.OnClickListener {

    ImageButton jugar;
    OpenInterface openInterface;

    public MenuConcentreseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu_concentrese, container, false);
        jugar=(ImageButton) view.findViewById(R.id.imbconcentrese);
        jugar.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openInterface =(OpenInterface) activity;
    }

    @Override
    public void onClick(View view) {
        openInterface.OpenConcentrese();
    }
}
