package com.jhonlopera.nerd30;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MenuTopoFragment extends Fragment {

    public MenuTopoFragment() {
        // Required empty public constructor
    }

    public static MenuTopoFragment newInstance(String param1, String param2) {
        MenuTopoFragment fragment = new MenuTopoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu_topo, container, false);
    }

}
