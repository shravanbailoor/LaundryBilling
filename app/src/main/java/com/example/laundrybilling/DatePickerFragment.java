package com.example.laundrybilling;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

                        int formatDay = view.getDayOfMonth();
                        int formatMonth = (view.getMonth() + 1);
                        String stringDay;
                        String stringMonth;

                        if (formatDay < 10) {

                            stringDay = "0" + formatDay;
                        } else {
                            stringDay = "" + formatDay;
                        }

                        if (formatMonth < 10) {

                            stringMonth = "0" + formatMonth;
                        } else {
                            stringMonth = "" + formatMonth;
                        }

                        String date = stringDay +
                                "/" + stringMonth +
                                "/" + view.getYear();
                        //Intent goToPlaceOrderScreen = getActivity().getIntent();
                        Intent goToPlaceOrderScreen = new Intent(getActivity().getBaseContext(),
                                UserData.class);
                        //startActivity(goToPlaceOrderScreen);
                        goToPlaceOrderScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        goToPlaceOrderScreen.setAction(Intent.ACTION_MAIN);
                        goToPlaceOrderScreen.addCategory(Intent.CATEGORY_LAUNCHER);

                        UserData user = (UserData)getActivity();
                        user.setDate(date);

                    }

                };
}
