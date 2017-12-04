package com.jhonlopera.nerd30;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class CreditosCPULFragment extends Fragment {
    DatabaseReference myRef;
    FirebaseDatabase database;
    private ArrayList<PuntajeJuego> puntajesCPUI,ptaux;
    private ListView lista;
    public CreditosCPULFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_creditos_cpul, container, false);
        ptaux=new ArrayList<PuntajeJuego>();
        puntajesCPUI=new ArrayList<PuntajeJuego>();
        lista=(ListView) view.findViewById(R.id.puntajecpui);
        final Adapter adapter=new Adapter(getActivity(),puntajesCPUI);
        lista.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        myRef=FirebaseDatabase.getInstance().getReference();

        myRef.child("DatosDeUsuario").orderByChild("puntaje4imagenes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                ptaux.clear();
                puntajesCPUI.clear();
                int cont=1;
                while (items.hasNext()){
                    DataSnapshot item=items.next();
                    String nombre,puntaje;
                    nombre=item.child("nombre").getValue().toString();
                    puntaje=item.child("puntaje4imagenes").getValue().toString();
                    if(!nombre.equals(" ")){
                        PuntajeJuego entrada=new PuntajeJuego(nombre,puntaje,String.valueOf(cont));
                        cont++;
                        ptaux.add(entrada);
                    }
                }
                for (int i=ptaux.size()-1;i>=0;i--){
                    ptaux.get(i).setId(String.valueOf(ptaux.size()-i));
                    puntajesCPUI.add(ptaux.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    class Adapter extends ArrayAdapter<PuntajeJuego> {

        public Adapter(@NonNull Context context, ArrayList<PuntajeJuego> puntajesCPUI) {
            super(context, R.layout.puntaje_list,puntajesCPUI);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(getContext());
            View view1=inflater.inflate(R.layout.puntaje_list,null);
            PuntajeJuego jugadorCPUI=getItem(position);
            TextView  pocision=(TextView) view1.findViewById(R.id.tvposicion);
            pocision.setText(jugadorCPUI.getId());
            TextView  tName=(TextView) view1.findViewById(R.id.tvnombrejugador);
            tName.setText(jugadorCPUI.getName());
            TextView  puntajetp=(TextView) view1.findViewById(R.id.tvpuntajejugador);
            puntajetp.setText("Puntaje: "+String.valueOf(jugadorCPUI.getPuntaje()));
            return view1;
        }
    }
}
