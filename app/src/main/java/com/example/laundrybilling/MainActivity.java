package com.example.laundrybilling;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Proceed to PlaceOrder screen on tapping 'Place Order' button */
    public void placeOrder(View placeOrderView) {
        Intent goToPlaceOrderScreen = new Intent(this, PlaceOrder.class);
        startActivity(goToPlaceOrderScreen);
    }

    /* Proceed to ViewOrderHistory screen on tapping 'View Order History' button */
    public void viewOrderHistory(View viewOrderHistoryView) {
        Intent goToViewOrderHistoryScreen = new Intent(this, ViewOrderHistory.class);
        startActivity(goToViewOrderHistoryScreen);
    }

}
