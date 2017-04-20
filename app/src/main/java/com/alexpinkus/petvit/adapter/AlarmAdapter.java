package com.alexpinkus.petvit.adapter;

import android.animation.ValueAnimator;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.alexpinkus.petvit.R;
import com.alexpinkus.petvit.model.AlarmItem;
import com.alexpinkus.petvit.widget.DaysOfWeek;
import com.alexpinkus.petvit.widget.TextTime;
import com.alexpinkus.petvit.widget.Utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by AlexPinkus on 19-Jan-17.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{
    List<AlarmItem> mdata = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private boolean expansion=false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //Variables necesarias para la animación de expandir/Colapsar.
        private int originalHeight = 0;
        private boolean mIsViewExpanded = false;
        private LinearLayout yourCustomView;

        //Todos los elementos de nuestra fila.
        public CompoundButton onOff;
        public TextTime clock;
        public TextView title;
        public TextView mealsize;
        public CheckBox repeat;
        public LinearLayout repeatDays;
        public ImageButton expandbutton;
        public ImageButton deletebutton;
        public final CompoundButton[] dayButtons = new CompoundButton[7];

        public ViewHolder(final View itemView)
        {
            super(itemView);

            //Inicializamos cada elemento de nuestra vista
            onOff = (CompoundButton) itemView.findViewById(R.id.onoff);
            title = (TextView) itemView.findViewById(R.id.edit_label);
            mealsize = (TextView) itemView.findViewById(R.id.meal_size);
            clock = (TextTime) itemView.findViewById(R.id.digital_clock);
            repeat = (CheckBox) itemView.findViewById(R.id.repeat_onoff);
            repeatDays = (LinearLayout) itemView.findViewById(R.id.repeat_days);
            expandbutton= (ImageButton) itemView.findViewById(R.id.arrow);
            deletebutton= (ImageButton) itemView.findViewById(R.id.delete);
            //Esta es la vista de la parte que se colapsa/expande.
            yourCustomView= (LinearLayout) itemView.findViewById(R.id.collapselayout);

            // Build button for each day.
            final int firstDay = Utils.getZeroIndexedFirstDayOfWeek(context);
            for (int i = 0; i < DaysOfWeek.DAYS_IN_A_WEEK; i++)
            {
                final CompoundButton dayButton = (CompoundButton) inflater.inflate(
                        R.layout.day_button, repeatDays, false);// attachToRoot
                dayButton.setText(Utils.getShortWeekday(i, firstDay));
                dayButton.setContentDescription(Utils.getLongWeekday(i, firstDay));
                repeatDays.addView(dayButton);
                dayButtons[i] = dayButton;
            }



            //***HANDLERS***
            //Colocamos clicklisteners para cada elemento de nuestro item.
            //Las acciones son las representadas en las funciones de esta clase.
            //***Nota***
            //Esta es una forma de implementarlo que crea listeners para cada boton, otra manera
            //es usar el mismo listener de la fila y de ahí determinar quien fue el que envió
            //el click.
            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {clickDelete(getAdapterPosition(), itemView);}
            });
            clock.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {clickClock(getAdapterPosition(),clock);
                }
            });
            mealsize.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {clickGrams(getAdapterPosition(),mealsize);}
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {clickTitle(getAdapterPosition(),title);
                }
            });
            onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    SaveBoolean("Active"+getAdapterPosition(), checked);
                    mdata.get(getAdapterPosition()).setActive(checked);
                }
            });
            // Repeat checkbox handler
            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final boolean checked = ((CheckBox) view).isChecked();
                    if(checked) {repeatDays.setVisibility(View.VISIBLE);}
                    else    {repeatDays.setVisibility(View.GONE);}
                }
            });
            // Day buttons handler
            for (int i = 0; i < DaysOfWeek.DAYS_IN_A_WEEK; i++)
            {
                final int buttonIndex = i;
                dayButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickDayButtons(getAdapterPosition(),view,buttonIndex);}});
            }


            // El siguiente codigo es para expandir/contraer el item actual
            itemView.setOnClickListener(this);
            expandbutton.setOnClickListener(this);

            // If isViewExpanded == false then set the visibility
            // of whatever will be in the expanded to GONE
            if(mIsViewExpanded == false)
            {
                // Set Views to View.GONE and .setEnabled(false)
                yourCustomView.setVisibility(View.GONE);
                yourCustomView.setEnabled(false);
            }

        }

        //Todo el siguiente codigo permite que el item se expanda o contraiga.
        //On click para la fila.
        @Override
        public void onClick(final View view)
        {
            // do something when the button is clicked
            // If the originalHeight is 0 then find the height of the View being used
            // This would be the height of the cardview
            if (originalHeight == 0) {originalHeight = itemView.getHeight();}

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;

            //Si esta no esta expandida y no hay otra vista expandida entonces se expande.
            if (!mIsViewExpanded && !expansion)
            {
                expansion=true;
                yourCustomView.setVisibility(View.VISIBLE);
                yourCustomView.setEnabled(true);
                mIsViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 2.2)); // These values in this method can be changed to expand however much you like

                valueAnimator.setDuration(200);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        itemView.getLayoutParams().height = value.intValue();
                        itemView.requestLayout();
                    }
                });
                valueAnimator.start();
                //Esto es para que cargue los valores de los días.
                final DaysOfWeek prueba= new DaysOfWeek(mdata.get(getAdapterPosition()).getDaysOfWeek());
                if(prueba.isRepeating()) {
                    repeat.setChecked(true);
                    repeatDays.setVisibility(View.VISIBLE);
                    for (int i = 0; i < DaysOfWeek.DAYS_IN_A_WEEK; i++) {
                        if(prueba.getSetDays().contains(i+1)) {
                            dayButtons[i].setChecked(true);
                            dayButtons[i].setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                        }
                    }
                }

            }
            //Si hay expansión y es esta vista.
            else if(mIsViewExpanded && expansion)
            {
                expansion=false;
                mIsViewExpanded = false;
                valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 2.2), originalHeight);

                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                a.setDuration(200);
                // Set a listener to the animation and configure onAnimationEnd
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        yourCustomView.setVisibility(View.INVISIBLE);
                        yourCustomView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                // Set the animation on the custom view
                yourCustomView.startAnimation(a);

                valueAnimator.setDuration(200);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        Integer value = (Integer) animation.getAnimatedValue();
                        itemView.getLayoutParams().height = value.intValue();
                        itemView.requestLayout();
                    }
                });
                valueAnimator.start();
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlarmAdapter(Context context, List<AlarmItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mdata = data;
    }

    //Delete item at position
    public void delete(int position) {
        mdata.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mdata.size());
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = inflater.inflate(R.layout.alarm_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        AlarmItem current = mdata.get(position);

        // - replace the contents of the view with that element
        holder.title.setText(current.getTitle());
        holder.clock.setTime(current.getHour(),current.getMinute());
        holder.mealsize.setText(current.getGrams()+ " Grams");
        holder.onOff.setChecked(current.isActive());

        //Esto hace que siempre que se reinicialice la vista esten colapsados.
        holder.mIsViewExpanded=false;
        holder.yourCustomView.setVisibility(View.GONE);
        holder.yourCustomView.setEnabled(false);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    //Las siguientes son las diferentes funciones que se ejecutan con los clicks.
    private void clickClock(final int position, final TextTime clock)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                clock.setTime(selectedHour,selectedMinute);
                //Esto es para guardar la configuración.
                SaveInt("Hour"+position, selectedHour);
                SaveInt("Minute"+position, selectedMinute);
                //Actualizamos el item actual
                mdata.get(position).setHour(selectedHour);
                mdata.get(position).setMinute(selectedMinute);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void clickGrams(final int position, final TextView mealsize)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Select the meal size");
        alertDialogBuilder.setMessage("Grams: 50");

        LinearLayout linear=new LinearLayout(context);
        linear.setOrientation(LinearLayout.VERTICAL);
        SeekBar seek=new SeekBar(context);
        seek.setMax(150);
        linear.addView(seek);
        alertDialogBuilder.setView(linear);

        //Esta es una variable auxiliar que nos servirá para pasar el valor de la seekbar
        final TextView text=new TextView(context);
        text.setText("50");

        alertDialogBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        mealsize.setText(text.getText()+" Grams");
                        //Guardamos y actualizamos el item actual
                        SaveInt("Grams"+position, Integer.parseInt(text.getText().toString()));
                        mdata.get(position).setGrams(Integer.parseInt(text.getText().toString()));
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        //alertDialogBuilder
        final AlertDialog alertDialog = alertDialogBuilder.create();

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                progress = progress +50;
                text.setText(String.valueOf(progress));
                alertDialog.setMessage("Grams: "+String.valueOf(progress));
                //SaveInt("Grams"+getAdapterPosition(), progress, context);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        alertDialog.show();
    }

    private void clickTitle(final int position, final TextView title)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Label");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(input);

        // Set up the buttons
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title.setText(input.getText().toString());
                //Guardamos y actualizamos el item actual
                SaveString("Label"+position, title.getText().toString());
                mdata.get(position).setTitle(title.getText().toString());

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    private void clickDelete(final int position, final View itemView)
    {
        SharedPreferences sharedPref= context.getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        for (int i = position; i <mdata.size()-1; i++) {
            editor.putString("Label"+i, sharedPref.getString("Label"+(i+1), context.getString(R.string.label)));
            editor.putBoolean("Active"+i, sharedPref.getBoolean("Active"+(i+1), false));
            editor.putInt("Hour"+i, sharedPref.getInt("Hour"+(i+1), 0));
            editor.putInt("Minute"+i, sharedPref.getInt("Minute"+(i+1), 0));
            editor.putInt("Grams"+i, sharedPref.getInt("Grams"+(i+1), 50));
        }
        editor.commit();

        editor.remove("Label"+(mdata.size()-1));
        editor.remove("Active"+(mdata.size()-1));
        editor.remove("Hour"+(mdata.size()-1));
        editor.remove("Minute"+(mdata.size()-1));
        editor.remove("Grams"+(mdata.size()-1));
        editor.putInt("Items",(mdata.size()-1));
        editor.commit();

        expansion=false;
        //Regresamos a la altura normal.
        itemView.getLayoutParams().height=-2;
        delete(position);
    }

    private void clickDayButtons(final int position, final View itemView, final int buttonIndex)
    {
        final int firstDay = Utils.getZeroIndexedFirstDayOfWeek(context);
        final boolean isChecked = ((CompoundButton) itemView).isChecked();
        final DaysOfWeek mDays= new DaysOfWeek(mdata.get(position).getDaysOfWeek());
        if(isChecked) {
            mDays.setDaysOfWeek(true,buttonIndex+1);

            Toast.makeText(context, mDays.toString(context,firstDay), Toast.LENGTH_SHORT).show();
            //Aqui estamos guardando.
            SaveInt("DayOfWeek"+position, mDays.getBitSet());
            mdata.get(position).setDaysOfWeek(mDays.getBitSet());

            ((CompoundButton) itemView).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        else
        {
            mDays.setDaysOfWeek(false,buttonIndex+1);
            SaveInt("DayOfWeek"+position, mDays.getBitSet());
            mdata.get(position).setDaysOfWeek(mDays.getBitSet());
            ((CompoundButton) itemView).setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }
    }

    public void SaveString(String A, String B)
    {
        SharedPreferences sharedPref= context.getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putString(A, B);
        editor.commit();
    }

    public void SaveInt(String A, int B)
    {
        SharedPreferences sharedPref= context.getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt(A, B);
        editor.commit();
    }

    public void SaveBoolean(String A, boolean B)
    {
        SharedPreferences sharedPref= context.getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putBoolean(A, B);
        editor.commit();
    }

}



