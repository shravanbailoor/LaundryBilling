package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ShowList extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<String> addedItemDetailArray;
    ArrayList<String> addedItemPriceArray;

    Float totalAmount;
    TextView displayAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        Intent goToShowList = getIntent();
        addedItemDetailArray = goToShowList.getStringArrayListExtra("addedItemDetail");
        addedItemPriceArray = goToShowList.getStringArrayListExtra("addedItemPrice");

        for (int i = 0; i < addedItemPriceArray.size(); i++) {
            String var = addedItemPriceArray.get(i);
            Float floatAmount = Float.parseFloat(var);

            if (totalAmount == null) {
                totalAmount = floatAmount;
            } else {
                totalAmount = totalAmount + floatAmount;
            }
        }

        displayAmountTextView = findViewById(R.id.textViewDisplayAmount);
        displayAmountTextView.setText(displayAmountTextView.getText().charAt(0) +
                String.valueOf(totalAmount));

        recyclerView = findViewById(R.id.recyclerViewShowList);

        /* addedItems = new ArrayList<>();
        addedItems.add("India");
        addedItems.add("Germany");
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItems); */

        final RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItemDetailArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*Toast.makeText(ShowList.this, "Single Click on position :" + position,
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onLongClick(View view, final int position) {
                /*Toast.makeText(ShowList.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();*/

                AlertDialog alertbox = new AlertDialog.Builder(ShowList.this)
                        .setMessage("Do you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                addedItemDetailArray.remove(position);

                                Float priceToBeDeducted = Float.parseFloat(addedItemPriceArray.get(position));
                                Float updatedAmount = totalAmount - priceToBeDeducted;
                                displayAmountTextView.setText(displayAmountTextView.getText().charAt(0) +
                                        String.valueOf(updatedAmount));
                                addedItemPriceArray.remove(position);
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();
            }
        }));
    }

    public void onClickConfirmItems(View viewConfirmItems) {
        List<Items> itemsList = new ArrayList<>();
         Items item ;
        for (String strItem:
             addedItemDetailArray) {
            String[] arrItemDetails = strItem.split("\\|");


            if(arrItemDetails.length>0){
                String typeWork = arrItemDetails[1];
                String[] arrItemTypes = arrItemDetails[2].split(" - ");
                String itemType = arrItemTypes[0].trim();
                String itemSubType = arrItemTypes[1].trim();
                double price = Double.valueOf(arrItemDetails[3].replace("Rs.","").trim());
                int quantity = Integer.valueOf(arrItemDetails[4].replace("x","").trim());
                double itemTotalPrice = price * quantity;
                item = new Items(typeWork, itemType,itemSubType,price,quantity
                ,itemTotalPrice);
                itemsList.add(item);
            }
        }


        OrderDetails orderDetails = new OrderDetails(null, null, -1, itemsList, totalAmount,
                null,null);

        Intent goToUserDataScreen = new Intent(this, UserData.class);
        goToUserDataScreen.putExtra("orderDetails", (Serializable) orderDetails);

        startActivity(goToUserDataScreen);
        //finish();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public void onBackPressed(){
        Intent goToPlaceOrderScreen = new Intent(this, PlaceOrder.class);
        goToPlaceOrderScreen.putExtra("addedItemDetail", addedItemDetailArray);
        goToPlaceOrderScreen.putExtra("addedItemPrice", addedItemPriceArray);
        startActivity(goToPlaceOrderScreen);
        super.onBackPressed();
        finish();
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView,
                                     final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && clicklistener != null) {
                                clicklistener.onLongClick(child,
                                        recycleView.getChildAdapterPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}