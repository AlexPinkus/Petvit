package com.alexpinkus.petvit.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexpinkus.petvit.R;
import com.alexpinkus.petvit.db.Pet;
import com.alexpinkus.petvit.db.Querys;
import com.alexpinkus.petvit.db.Raza;
import com.bumptech.glide.Glide;

import java.util.List;


public class InformacionFragment extends Fragment {

 // ****************** INICIAMOS PROCESO ***************************
    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private Querys querys;
    private List<Raza> razas;
    private Pet myPet;
    private String fotoUri;
    private String nameRaza;
    private String historia;
    private String esperanza;
    private String altura;
    private String peso;
    private String origen;
    private String descripcion;
    private String tipo;
    private TextView txtHistoria;
    private TextView txtDescripcion;
    private TextView txtEdad;
    private TextView txtPeso;
    private TextView txtAltura;
    private TextView txtOrigen;
    private TextView txtTipo;

    public InformacionFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_informacion, container, false);
        mCollapsingView = (CollapsingToolbarLayout)rootView.findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView)rootView.findViewById(R.id.iv_avatar);
        txtAltura = (TextView)rootView.findViewById(R.id.detailAlturaEdit);
        txtDescripcion = (TextView)rootView.findViewById(R.id.detailDescriptionEdit);
        txtEdad = (TextView)rootView.findViewById(R.id.detailEsperanzaEdit);
        txtOrigen = (TextView)rootView.findViewById(R.id.detailOrigenEdit);
        txtPeso = (TextView)rootView.findViewById(R.id.detailPesoEdit);
        txtHistoria = (TextView)rootView.findViewById(R.id.detailHistoriaEdit);
        txtTipo = (TextView)rootView.findViewById(R.id.detailTipoEdit);

        querys = new Querys(getActivity().getApplicationContext());
        myPet = querys.getMyPet();

        for (Raza raza : querys.getAllRazas()) {
            if (raza.getId() == myPet.getRazaId()) {
                fotoUri = raza.getFoto();
                nameRaza = raza.getNombre_raza();
                origen = raza.getOrigen();
                historia = raza.getHistoria();
                esperanza = raza.getEsperanza();
                descripcion = raza.getDescripcion();
                altura = raza.getAltura_adulto();
                peso = raza.getPeso_adulto();
                tipo = raza.getTipo_raza();
            }
        }

        txtDescripcion.setText(descripcion);
        txtHistoria.setText(historia);
        txtPeso.setText(peso);
        txtAltura.setText(altura);
        txtEdad.setText(esperanza);
        txtOrigen.setText(origen);
        txtTipo.setText(tipo);

        mCollapsingView.setTitle(nameRaza);
        mCollapsingView.setCollapsedTitleTextColor(Color.BLACK);
        mCollapsingView.setExpandedTitleColor(Color.WHITE);
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/" + fotoUri))
                .centerCrop()
                .into(mAvatar);

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
