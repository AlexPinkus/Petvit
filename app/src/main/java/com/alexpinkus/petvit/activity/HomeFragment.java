package com.alexpinkus.petvit.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.alexpinkus.petvit.R;
import com.alexpinkus.petvit.db.Pet;
import com.alexpinkus.petvit.db.Querys;
import com.alexpinkus.petvit.db.Raza;
import com.alexpinkus.petvit.model.AlarmItem;
import com.alexpinkus.petvit.widget.TextTime;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.net.URI;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.alexpinkus.petvit.activity.MainActivity.data;


/**
 * Created by AlexP on 19-Jan-17.
 */

public class HomeFragment extends Fragment{

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString, aux;
    CircularImageView circularImageView;

    Uri selectedImage;
    EditText editName;
    TextView editAge;
    TextView editWeight;
    private ImageButton btnCamera;
    private ImageButton btnGallery;
    private ImageButton btnCambiarPeso;
    private ImageButton btnCambiarEdad;
    CircularImageView circularImageViewDialog;
    Pet myPet;
    TextView txtPetName, txtPetAge, txtPetWeight, txtPetRaza;
    Spinner spinnerRazas;
    private int razaId;
    private int auxQty;

    private Querys querys;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //Codigo para manejar la imagen del perrito.
        //Inicializo la vista.
        txtPetName = (TextView)rootView.findViewById(R.id.tagPetName);
        txtPetRaza = (TextView)rootView.findViewById(R.id.editPetRaza);
        txtPetWeight = (TextView)rootView.findViewById(R.id.editPetWeight);
        txtPetAge = (TextView)rootView.findViewById(R.id.editPetAge);

        circularImageView = (CircularImageView)rootView.findViewById(R.id.circularimage_dog);

        querys = new Querys(getActivity().getApplicationContext());
        myPet = querys.getMyPet();

        txtPetName.setText(myPet.getNombre());
        txtPetAge.setText(Integer.toString(myPet.getEdad()) + " año(s)");
        txtPetWeight.setText(Integer.toString(myPet.getPeso()) + " kg");

        for (Raza raza : querys.getAllRazas()) {
            if (raza.getId() == myPet.getRazaId()) {
                aux = raza.getNombre_raza();
            }
        }

        txtPetRaza.setText(aux);

        //Recupero la dirección de la imagen guardada.
        SharedPreferences sharedPref= getActivity().getSharedPreferences("mypref", 0);
        String filepath=sharedPref.getString("ImageDog","");
        //Convierto la direccion en un archivo
        File file = new File(filepath);
        if(file.exists())
        {
            //Si la imagen existe simplemente la coloco.
            selectedImage =Uri.parse(filepath);
            circularImageView.setImageURI(selectedImage);
        }
        else
        {
            //La imagen seleccionada ha sido borrada del celular.
            Toast.makeText(getContext(), "No Existo",
                    Toast.LENGTH_LONG).show();
            //Coloco la imagen de default
            circularImageView.setImageDrawable(getResources().getDrawable(R.drawable.dog));
        }
        /* Este codigo es para cambiar otros parámetros de la imagen.
        // Set Border
        circularImageView.setBorderColor(getResources().getColor(R.color.colorAccent));
        circularImageView.setBorderWidth(5);
        // Add Shadow with default param
        circularImageView.addShadow();
        // or with custom param
        circularImageView.setShadowRadius(10);
        circularImageView.setShadowColor(R.color.colorPrimary);
        */

        // Listener para cambiar la imagen.
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_pet, null);
                builder.setView(view);

                editName = (EditText)view.findViewById(R.id.editName);
                editAge = (TextView)view.findViewById(R.id.editEdad);
                editWeight = (TextView)view.findViewById(R.id.editWeight);
                btnCamera = (ImageButton)view.findViewById(R.id.cameraBtn);
                btnGallery = (ImageButton)view.findViewById(R.id.galleryBtn);

                spinnerRazas = (Spinner)view.findViewById(R.id.spinnerRazas);

                circularImageViewDialog = (CircularImageView) view.findViewById(R.id.circularimage_dogDialog);
                editAge.setText(Integer.toString(myPet.getEdad()));
                editWeight.setText(Integer.toString(myPet.getPeso()));
                editName.setText(myPet.getNombre());

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
                spinnerRazas.setAdapter(spinnerAdapter);

                for (Raza raza: querys.getAllRazas()){
                    spinnerAdapter.add(raza.getNombre_raza());
                }

                // obtenemos la raza actual de nuestra mascota
                if (!aux.equals(null)) {
                    int spinnerPosition = spinnerAdapter.getPosition(aux);
                    spinnerRazas.setSelection(spinnerPosition);
                }

                SharedPreferences sharedPref= getActivity().getSharedPreferences("mypref", 0);
                String filepath=sharedPref.getString("ImageDog","");
                //Convierto la direccion en un archivo
                File file = new File(filepath);
                if(file.exists())
                {
                    //Si la imagen existe simplemente la coloco.
                    selectedImage =Uri.parse(filepath);
                    circularImageViewDialog.setImageURI(selectedImage);
                }
                else
                {
                    //La imagen seleccionada ha sido borrada del celular.
                    Toast.makeText(getContext(), "No Existo",
                            Toast.LENGTH_LONG).show();
                    //Coloco la imagen de default
                    circularImageViewDialog.setImageDrawable(getResources().getDrawable(R.drawable.dog));
                }

                editWeight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPicker numberPicker = new NumberPicker(getActivity());
                        numberPicker.setMaxValue(10);
                        numberPicker.setMinValue(1);

                        // LA VARIABLE AUXQTY NOS PERMITIRA OBTENER EL VALOR MINIMO SI EL NUMBER PICKER NUNCA ES CAMBIADO
                        auxQty = myPet.getPeso();
                        NumberPicker.OnValueChangeListener myValue = new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                auxQty = newVal;
                                //  txtAge.setText("" + newVal);
                            }
                        };
                        numberPicker.setOnValueChangedListener(myValue);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(numberPicker);
                        builder.setTitle("Peso en kg");
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                editWeight.setText(Integer.toString(auxQty));
                                Toast.makeText(getActivity(), Integer.toString(auxQty),
                                        Toast.LENGTH_SHORT).show();


                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                });

                editAge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPicker numberPicker = new NumberPicker(getActivity());
                        numberPicker.setMaxValue(10);
                        numberPicker.setMinValue(1);

                        // LA VARIABLE AUXQTY NOS PERMITIRA OBTENER EL VALOR MINIMO SI EL NUMBER PICKER NUNCA ES CAMBIADO
                        auxQty = myPet.getEdad();
                        NumberPicker.OnValueChangeListener myValue = new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                auxQty = newVal;
                                //  txtAge.setText("" + newVal);
                            }
                        };
                        numberPicker.setOnValueChangedListener(myValue);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(numberPicker);

                       // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Material_Light_Dialog_NoActionBar).setView(numberPicker);

                        builder.setTitle("Edad en años");
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                editAge.setText(Integer.toString(auxQty));
                                Toast.makeText(getActivity(), Integer.toString(auxQty),
                                        Toast.LENGTH_SHORT).show();


                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        builder.show();
                    }
                });

                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create intent to Open Image applications like Gallery, Google Photos
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                        ((MainActivity)getActivity()).triggerNotification("Imagen seleccionada");
                    }
                });

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String raza = spinnerRazas.getSelectedItem().toString();

                        for (Raza searchRaza: querys.getAllRazas()){
                            if (searchRaza.getNombre_raza().equals(raza)) {
                                razaId = searchRaza.getId();
                            }
                        }

                        Pet pet = new Pet(myPet.getId(), editName.getText().toString(), Integer.parseInt(editAge.getText().toString()),
                                Integer.parseInt(editWeight.getText().toString()), razaId, null);
                        querys.updatePet(pet, Integer.toString(myPet.getId()));

                        myPet = querys.getMyPet();

                        txtPetName.setText(myPet.getNombre());
                        txtPetAge.setText(Integer.toString(myPet.getEdad()) + " año(s)");
                        txtPetWeight.setText(Integer.toString(myPet.getPeso()) + " kg");

                        for (Raza raza_ : querys.getAllRazas()) {
                            if (raza_.getId() == myPet.getRazaId()) {
                                aux = raza_.getNombre_raza();
                            }
                        }
                        txtPetRaza.setText(aux);

                        Toast.makeText(getActivity(), "¡Modificación exitosa!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



                AlertDialog dialog = builder.create();
                dialog.show();
                /*
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                ((MainActivity)getActivity()).triggerNotification("Imagen seleccionada");
                */
            }
        });


        //Colocamos el progreso de las barras.
        ProgressBar progressBarBattery = (ProgressBar) rootView.findViewById(R.id.circle_progress_bar);
        progressBarBattery.setProgress(75);

        ProgressBar progressBarContainer = (ProgressBar) rootView.findViewById(R.id.circle_progress_bar2);
        progressBarContainer.setProgress(50);

        //Progreso en texto
        TextView battery_text = (TextView) rootView.findViewById(R.id.textView_battery);
        battery_text.setText(getString(R.string.progressBar_Battery)+"\r\n"+progressBarBattery.getProgress());

        TextView container_text = (TextView) rootView.findViewById(R.id.textView_container);
        container_text.setText(getString(R.string.progressBar_Container)+"\r\n"+progressBarContainer.getProgress());


        TextTime prevClock= (TextTime) rootView.findViewById(R.id.prev_clock);
        TextTime nextClock= (TextTime) rootView.findViewById(R.id.next_clock);

        //Obtenemos el más anterior y el siguiente.
        Calendar mcurrentTime = Calendar.getInstance();

        int dia=0;
        dia=mcurrentTime.get(Calendar.DAY_OF_WEEK);
        int currentHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = mcurrentTime.get(Calendar.MINUTE);


        Calendar next = Calendar.getInstance();
        Calendar prev = Calendar.getInstance();
        next.roll(Calendar.DAY_OF_WEEK, true);
        /*
        next.set(Calendar.HOUR_OF_DAY,23);
        next.set(Calendar.MINUTE,59);
        */
        prev.set(Calendar.HOUR_OF_DAY,0);
        prev.set(Calendar.MINUTE,0);

        int hello=0;
        prev.get(Calendar.DAY_OF_WEEK);

        //9.26  12.40  13.18  15.37 18.39
        for (AlarmItem item : data) {
            Calendar alarm= Calendar.getInstance();
            alarm.set(Calendar.HOUR_OF_DAY, item.getHour());
            alarm.set(Calendar.MINUTE, item.getMinute());

            //alarm.roll(Calendar.DAY_OF_WEEK, true);
            if(mcurrentTime.after(alarm))
            {
                alarm.roll(Calendar.DAY_OF_WEEK, true);
            }
            if(next.after(alarm))
            {
                next.set(Calendar.HOUR_OF_DAY, item.getHour());
                next.set(Calendar.MINUTE, item.getMinute());
                next.set(Calendar.DAY_OF_WEEK, alarm.get(Calendar.DAY_OF_WEEK));
            }
            /*
            if(mcurrentTime.after(alarm) &&  prev.before(alarm)) {
                prev.set(Calendar.HOUR_OF_DAY, item.getHour());
                prev.set(Calendar.MINUTE, item.getMinute());
            }*/

        }
        for (AlarmItem item : data) {
            Calendar alarm= Calendar.getInstance();
            alarm.set(Calendar.HOUR_OF_DAY, item.getHour());
            alarm.set(Calendar.MINUTE, item.getMinute());

            //alarm.roll(Calendar.DAY_OF_WEEK, true);
            if(mcurrentTime.before(alarm))
            {
                alarm.roll(Calendar.DAY_OF_WEEK, false);
            }
            if(prev.before(alarm))
            {
                prev.set(Calendar.HOUR_OF_DAY, item.getHour());
                prev.set(Calendar.MINUTE, item.getMinute());
                prev.set(Calendar.DAY_OF_WEEK, alarm.get(Calendar.DAY_OF_WEEK));
            }


        }
        prevClock.setTime(prev.get(Calendar.HOUR_OF_DAY), prev.get(Calendar.MINUTE));
        nextClock.setTime(next.get(Calendar.HOUR_OF_DAY), next.get(Calendar.MINUTE));

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data


                Uri selectedImage = data.getData();
                //circularImageView.setImageURI(selectedImage);

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);

                //Guardamos la direccion de la imagem
                SharedPreferences sharedPref= getActivity().getSharedPreferences("mypref", 0);
                sharedPref.edit().putString("ImageDog", imgDecodableString).commit();
                cursor.close();
                //ImageView imgView = (ImageView) getActivity().findViewById(R.id.circularimage_dog);
                // Set the Image in ImageView after decoding the String
                circularImageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));


                Toast.makeText(this.getContext(), "Imagen seleccionada",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
