package com.alexpinkus.petvit.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexpinkus.petvit.R;
import com.alexpinkus.petvit.adapter.AlarmAdapter;
import com.alexpinkus.petvit.model.AlarmItem;

import java.util.ArrayList;
import java.util.List;

import static com.alexpinkus.petvit.activity.MainActivity.data;

/**
 * Created by AlexP on 19-Jan-17.
 */

public class AlarmFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton fab;
    private static final String TAG = "MyActivity";



    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        //Inicializamos todas las vistas.
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.horarios_recycler_view);

        fab = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref= getActivity().getSharedPreferences("mypref", 0);
                //Limito a un numero maximo de alarmas 5 en este caso.
                if(sharedPref.getInt("Items",0)<5) {
                    sharedPref.edit().putInt("Items", sharedPref.getInt("Items", 0) + 1).commit();
                    AlarmItem alarm = new AlarmItem();
                    alarm.setTitle(getString(R.string.label));
                    alarm.setHour(0);
                    alarm.setMinute(0);
                    alarm.setGrams(50);
                    alarm.setActive(false);
                    alarm.setDaysOfWeek(0);
                    data.add(alarm);
                    mAdapter.notifyItemInserted(data.size() - 1);
                }
            }
        });


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AlarmAdapter(getActivity(), data);

        /*
        alarmAdapter.onRestoreInstanceState(savedInstanceState);
        */
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

/*
    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //((AlarmAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        for (int i = 0; i <data.size() ; i++) {
            editor.putString("Label"+i, data.get(i).getTitle());
            editor.putBoolean("Active"+i, data.get(i).isActive());
            editor.putInt("Hour"+i, data.get(i).getHour());
            editor.putInt("Minute"+i, data.get(i).getMinute());
            editor.putInt("Grams"+i, data.get(i).getGrams());
        }
        editor.commit();
        editor
    }
*/

    @Override
    public void onPause()
    {
        super.onPause();
        //Si salimos de esta pantalla hay que guardar to do.
        SharedPreferences sharedPref= getActivity().getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        for (int i = 0; i <data.size() ; i++) {
            editor.putString("Label"+i, data.get(i).getTitle());
            editor.putBoolean("Active"+i, data.get(i).isActive());
            editor.putInt("Hour"+i, data.get(i).getHour());
            editor.putInt("Minute"+i, data.get(i).getMinute());
            editor.putInt("Grams"+i, data.get(i).getGrams());
            editor.putInt("DayOfWeek"+i, data.get(i).getDaysOfWeek());
        }
        editor.commit();




        String messageData="200;";
        char activeAlarms='0';
        for (int i = 0; i <data.size() ; i++) {
            if(data.get(i).isActive()) {
                activeAlarms++;
                messageData+=""
                        +data.get(i).getHour()+";"
                        +data.get(i).getMinute()+";"
                        +data.get(i).getGrams()+";"
                        +data.get(i).getDaysOfWeek()+";";
            }
        }
        char[] messageChars = messageData.toCharArray();
        messageChars[2] = activeAlarms;
        messageData = String.valueOf(messageChars);
        Toast.makeText(getActivity(), messageData, Toast.LENGTH_LONG).show();
        Log.d(TAG, messageData);
        //((MainActivity)getActivity()).publishMessage(messageData);
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
