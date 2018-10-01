package com.gru.comandroidbluetooth.view.fragment;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePckrDialog extends DialogFragment
{
    private TimePickerDialog.OnTimeSetListener listener;

    public static TimePckrDialog newInstancia(TimePickerDialog.OnTimeSetListener listener)
    {
        TimePckrDialog dialog = new TimePckrDialog();
        dialog.setListener(listener);
        return dialog;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener)
    {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hs = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),listener,hs,min,true);
    }

}
