package com.jhonlopera.nerd30;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CreditosTpFragment extends Fragment {


    String jugadores [] = new String[5];
    String puntajetp[] = new String[5];
    TextView tjugador1, tjugador2, tjugador3, tjugador4, tjugador5;

    public CreditosTpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle=getArguments();
        if(bundle!=null){
        jugadores=bundle.getStringArray("jugadores");
        puntajetp=bundle.getStringArray("puntajetp");}
        //Toast.makeText(getApplicationContext(),"nombre: "+usuario,Toast.LENGTH_LONG).show();
        View view= inflater.inflate(R.layout.fragment_creditos_tp, container, false);
        tjugador1 = (TextView) view.findViewById(R.id.tjugador1);
        tjugador2 = (TextView) view.findViewById(R.id.tjugador2);
        tjugador3 = (TextView) view.findViewById(R.id.tjugador3);
        tjugador4 = (TextView) view.findViewById(R.id.tjugador4);
        tjugador5 = (TextView) view.findViewById(R.id.tjugador5);
        tjugador1.setText("1. " + jugadores[0] + "  " + String.valueOf(puntajetp[0]));
        tjugador2.setText("2. " + jugadores[1] + "  " + String.valueOf(puntajetp[1]));
        tjugador3.setText("3. " + jugadores[2] + "  " + String.valueOf(puntajetp[2]));
        tjugador4.setText("4. " + jugadores[3] + "  " + String.valueOf(puntajetp[3]));
        tjugador5.setText("5. " + jugadores[4] + "  " + String.valueOf(puntajetp[4]));
        return view;
    }
}
