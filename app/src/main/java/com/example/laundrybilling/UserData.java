package com.example.laundrybilling;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserData extends AppCompatActivity {

    EditText tbDeliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);
        tbDeliveryDate = findViewById(R.id.calDeliveryDate);

    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");


    }



    public void onClickCheckOut(View view) {
        Intent goToOrderScreen = new Intent(this, OrderData.class);
        startActivity(goToOrderScreen);
    }

    public void onClickReview(View view) {
        Intent goToPlaceOrderScreen = new Intent(this, PlaceOrderTabLayout.class);
        startActivity(goToPlaceOrderScreen);
    }
}
