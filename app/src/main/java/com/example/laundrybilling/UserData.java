package com.example.laundrybilling;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;

public class UserData extends AppCompatActivity {

    Button deliveryDateButton;
    Button deliveryTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);
        deliveryDateButton = findViewById(R.id.calDeliveryDate);
        deliveryTimeButton = findViewById(R.id.buttonSelectTime);
    }

    public void setDate(String date){
        deliveryDateButton.setText(date);
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    public void onClickDeliveryTime(View deliveryTimeView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(UserData.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String adjustedMinute;
                String adjustedHour;
                String timeConvention;

                if (selectedHour < 12) {
                    timeConvention = "AM";
                } else {
                    timeConvention = "PM";
                }

                if (selectedMinute < 10) {
                    adjustedMinute = "0" + selectedMinute;
                } else {
                    adjustedMinute = String.valueOf(selectedMinute);
                }

                if (selectedHour == 0) {
                    adjustedHour = "12";
                } else if (selectedHour > 0 && selectedHour < 10) {
                    adjustedHour = "0" + selectedHour;
                } else if (selectedHour == 12) {
                    adjustedHour = "12";
                } else if (selectedHour > 12 && selectedHour < 22) {
                    adjustedHour = "0" + (selectedHour - 12);
                } else if (selectedHour >= 22) {
                    adjustedHour = String.valueOf(selectedHour - 12);
                } else {
                    adjustedHour = String.valueOf(selectedHour);
                }

                deliveryTimeButton.setText(adjustedHour + ":" + adjustedMinute + " " + timeConvention);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void onClickCheckOut(View view) {
        AutoCompleteTextView customerNameText = findViewById(R.id.autoUserName);
        EditText phoneNumberText = findViewById(R.id.tbPhoneNumber);

        if (customerNameText.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(this, "Enter the Customer Name", Toast.LENGTH_SHORT).show();
        } else if (!(customerNameText.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, "Enter text for Customer Name", Toast.LENGTH_SHORT).show();
        } else if (phoneNumberText.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(this, "Enter the Phone Number", Toast.LENGTH_SHORT).show();
        } else if (deliveryDateButton.getText().toString().toLowerCase().equals("select date")) {
            Toast.makeText(this, "Select the Delivery Date", Toast.LENGTH_SHORT).show();
        } else {
            Intent goToOrderScreen = new Intent(this, OrderData.class);
            startActivity(goToOrderScreen);
        }
    }

    /*public void onClickReview(View view) {
        Intent goToShowListScreen = new Intent(this, ShowList.class);
        startActivity(goToShowListScreen);
    }*/
}
