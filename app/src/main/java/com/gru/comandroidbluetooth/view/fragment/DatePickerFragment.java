package com.gru.comandroidbluetooth.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

//Muestra el Dialog con la fecha

public class DatePickerFragment extends DialogFragment
{
    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener)
    {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener)
    {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int mes  = c.get(Calendar.MONTH);
        int dia  = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),listener,year,mes,dia);
    }

}
