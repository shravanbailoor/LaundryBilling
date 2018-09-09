package com.example.laundrybilling;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserData extends AppCompatActivity {

    Button tbDeliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);
        tbDeliveryDate = findViewById(R.id.calDeliveryDate);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //DETERMINE WHO STARTED THIS ACTIVITY
        if(this.getIntent().getExtras() != null) {
            final String sender = this.getIntent().getExtras().getString("SENDER_KEY");

            //IF ITS THE FRAGMENT THEN RECEIVE DATA
            if (sender != null) {
                Intent i = getIntent();
                String year = i.getStringExtra("YEAR_KEY");

                //SET DATA TO TEXTVIEWS

                tbDeliveryDate.setText(String.valueOf(year));
                Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();

            }
        }
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
