package com.example.laundrybilling;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static String orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /* Proceed to PlaceOrder screen on tapping 'Place Order' button */
    public void placeOrder(View placeOrderView) {
        if(null == orderNumber || orderNumber == "") {
            SharedPreferences sp = getSharedPreferences("key_code", MODE_PRIVATE);
            int code = sp.getInt("code", 0);

            if (code <= 0) {
                code = 1; //--set default start value--
            } else {
                code++; //--or just increment it--
            }

            sp.edit().putInt("code", code).commit(); //--save new value--

            //--use code variable now--
            String newKey = "A-" + code;
            orderNumber = newKey;
        }
        Intent goToPlaceOrderScreen = new Intent(this, PlaceOrder.class);
        startActivity(goToPlaceOrderScreen);
    }

    /* Proceed to ViewOrderHistory screen on tapping 'View Order History' button */
    public void viewOrderHistory(View viewOrderHistoryView) {
        Intent goToViewOrderHistoryScreen = new Intent(this, ViewOrderHistory.class);
        startActivity(goToViewOrderHistoryScreen);
    }

}
