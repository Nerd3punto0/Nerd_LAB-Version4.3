package com.jhonlopera.nerd30;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CreditosTpFragment extends Fragment {


    DatabaseReference myRef;
    FirebaseDatabase database;
    String usuario, contador;
    int valorfinal;
    String jugadores [] = {"No hay jugador","No hay jugador","No hay jugador","No hay jugador","No hay jugador"};
    String puntajecon [] = {"0","0","0","0","0"};
    TextView tjugador1, tjugador2, tjugador3, tjugador4, tjugador5;

    public CreditosTpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_creditos_ct, container, false);
        tjugador1 = (TextView) view.findViewById(R.id.tjugador1);
        tjugador2 = (TextView) view.findViewById(R.id.tjugador2);
        tjugador3 = (TextView) view.findViewById(R.id.tjugador3);
        tjugador4 = (TextView) view.findViewById(R.id.tjugador4);
        tjugador5 = (TextView) view.findViewById(R.id.tjugador5);

        database = FirebaseDatabase.getInstance();
        Bundle bundle=getArguments();
        if(bundle!=null){
            usuario=bundle.getString("usuario");
        }

        myRef = database.getReference("Contadores");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contador=dataSnapshot.child("contador").getValue().toString();

                if(Integer.valueOf(contador)<=0){
                    Toast.makeText(getActivity(),"No hay usuarios",Toast.LENGTH_LONG).show();
                }else{
                    if(Integer.valueOf(contador)>5)
                        valorfinal=5;
                    else
                        valorfinal=Integer.valueOf(contador);

                    myRef = database.getReference("DatosDeUsuario");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(int i=0; i<valorfinal; i++) {
                                jugadores[i] = dataSnapshot.child("user" + String.valueOf(i)).child("nombre").getValue().toString();
                                puntajecon[i] = dataSnapshot.child("user" + String.valueOf(i)).child("puntajeConcentrese").getValue().toString();
                            }

                            tjugador1.setText("1. " + jugadores[0] + "  " + String.valueOf(puntajecon[0]));
                            tjugador2.setText("2. " + jugadores[1] + "  " + String.valueOf(puntajecon[1]));
                            tjugador3.setText("3. " + jugadores[2] + "  " + String.valueOf(puntajecon[2]));
                            tjugador4.setText("4. " + jugadores[3] + "  " + String.valueOf(puntajecon[3]));
                            tjugador5.setText("5. " + jugadores[4] + "  " + String.valueOf(puntajecon[4]));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
}
