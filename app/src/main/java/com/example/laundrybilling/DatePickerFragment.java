package com.example.laundrybilling;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment{

    static String date = "";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Toast.makeText(getActivity(), "selected date is " + view.getYear() +
                                " / " + (view.getMonth()+1) +
                                " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
                        date = view.getYear() +
                                "/" + (view.getMonth()+1) +
                                "/" + view.getDayOfMonth();

                    }

                };
}
