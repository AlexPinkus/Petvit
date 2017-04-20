package com.alexpinkus.petvit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexpinkus.petvit.R;
import com.alexpinkus.petvit.db.Porciones;
import com.alexpinkus.petvit.db.Querys;


public class DietFragment extends Fragment {

    private TextView txtDiet;
    private Querys querys;
    private Porciones myPorcion;

    public DietFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_diet, container, false);

        txtDiet = (TextView)rootView.findViewById(R.id.dietaSugeridaEdit);

        querys = new Querys(getActivity().getApplicationContext());
        myPorcion = querys.getMyPorcion();

        String toDiet = "Se recomienda que para su mascota deacuerdo con la base de datos reciba una porción de "
                + Integer.toString(myPorcion.getGramos()) + " gramos al día.";

        txtDiet.setText(toDiet);

        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
