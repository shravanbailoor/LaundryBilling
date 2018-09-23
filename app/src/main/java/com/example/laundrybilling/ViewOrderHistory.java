package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewOrderHistory extends AppCompatActivity {

    public static final String URL="https://spreadsheets.google.com/feeds/list/1i1chsepnCq2w2AXi14CIpuFolqbytd1COJXyXfyMi20/1/public/values?alt=json";

    RecyclerView recyclerView;
    ArrayList<String> addedItemDetailArray;
    HashMap<String, ArrayList<Integer>> mapOrderNumRowId = new HashMap<>();

    JSONObject jsonResponse;
    boolean checkResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_history);

        recyclerView = findViewById(R.id.recyclerViewShowHistory);
        addedItemDetailArray = new ArrayList<>();
        GetDataTask getDataTask = new GetDataTask();
        getDataTask.execute();
        //boolean checkResponse = getDataTask.get();
        while(!checkResponse){
            try {
                Thread.sleep(1000);
                // Do some stuff
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(checkResponse){
            checkResponse = false;
        }
        /*List<String> keysToDisplay = new ArrayList<>();
        keysToDisplay.add("gsx$ordernumber");
        keysToDisplay.add("gsx$customername");
        keysToDisplay.add("gsx$phonenumber");
        keysToDisplay.add("gsx$expecteddeliverydate");*/
        final HashMap<String, OrderDetails> orderList = new HashMap<>();
        String[] keys = {"gsx$ordernumber",
                "gsx$customername",
                "gsx$phonenumber",
                "gsx$itemworktype",
                "gsx$itemtype",
                "gsx$itemsubtype",
                "gsx$price",
                "gsx$quantity",
                "gsx$itemtotalprice",
                "gsx$ordertotalcost",
                "gsx$expecteddeliverydate",
                "gsx$comments",
                "gsx$ordereddate"};
        JSONArray jsonTemp = new JSONArray();
        try {

                if(jsonResponse.has("feed")) {
                    jsonTemp = jsonResponse.getJSONObject("feed").getJSONArray("entry");
                }

            for (int i = 0 ; i<jsonTemp.length();i++) {
                JSONObject jsonRow = jsonTemp.getJSONObject(i);
                HashMap<String, String> tempMap = new HashMap<>();

                for (String key:
                     keys) {
                    if(jsonRow.has(key)){
                        tempMap.put(key,String.valueOf(jsonRow.getJSONObject(key).get("$t")));
                    }

                }
                String appendInfo = "";
                if(orderList.containsKey(tempMap.get("gsx$ordernumber").trim())){
                    OrderDetails tempOrderObj = orderList.get(tempMap.get("gsx$ordernumber").trim());
                    List<Items> listItem = tempOrderObj.getItems();
                    Items item = new Items(tempMap.get("gsx$itemworktype"),tempMap.get("gsx$itemtype"),tempMap.get("gsx$itemsubtype"),
                            Double.parseDouble(tempMap.get("gsx$price")),Integer.valueOf(tempMap.get("gsx$quantity")),Double.parseDouble(tempMap.get("gsx$itemtotalprice")));
                    listItem.add(item);
                    tempOrderObj.setItems(listItem);
                    orderList.put(tempMap.get("gsx$ordernumber").trim(),tempOrderObj);
                    ArrayList<Integer> tempListRow = mapOrderNumRowId.get(tempMap.get("gsx$ordernumber").trim());
                    tempListRow.add(i);
                    mapOrderNumRowId.put(tempMap.get("gsx$ordernumber").trim(),tempListRow);
                }else{

                    List<Items> listItem = new ArrayList<>();
                    Items item = new Items(tempMap.get("gsx$itemworktype"),tempMap.get("gsx$itemtype"),tempMap.get("gsx$itemsubtype"),
                            Double.parseDouble(tempMap.get("gsx$price")),Integer.valueOf(tempMap.get("gsx$quantity")),Double.parseDouble(tempMap.get("gsx$itemtotalprice")));
                    listItem.add(item);

                    OrderDetails tempOrderObj = new OrderDetails(tempMap.get("gsx$ordernumber").trim(),tempMap.get("gsx$customername"),
                            Long.valueOf(tempMap.get("gsx$phonenumber")),listItem,Double.parseDouble(String.valueOf(tempMap.get("gsx$ordertotalcost"))),tempMap.get("gsx$expecteddeliverydate"),tempMap.get("gsx$comments"));

                    orderList.put(tempMap.get("gsx$ordernumber").trim(),tempOrderObj);

                    appendInfo = tempOrderObj.getOrderNumber()+" | " + tempOrderObj.getCustomerName()+" | "+ tempOrderObj.getPhoneNumber() + " | " + tempOrderObj.getDeliveryDate();
                    addedItemDetailArray.add(appendInfo);
                    ArrayList<Integer> tempListRow = new ArrayList<>();
                    tempListRow.add(i);
                    mapOrderNumRowId.put(tempMap.get("gsx$ordernumber").trim(),tempListRow);
                }
                /*Iterator<String> rowKeys = jsonRow.keys();
                for (Iterator<String> it = rowKeys; it.hasNext(); ) {
                    String key = it.next();

                        appendInfo = appendInfo + "|" + jsonRow.getJSONObject(key).get("$t");


                }*/


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItemDetailArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView,new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*Toast.makeText(ShowList.this, "Single Click on position :" + position,
                        Toast.LENGTH_SHORT).show();*/
                String strItemDetail = addedItemDetailArray.get(position);
                String[] arrItemDetail = strItemDetail.split("\\|");

                OrderDetails orderDetails = orderList.get(arrItemDetail[0].trim());
                orderDetails.getOrderNumber();
                Intent goToUserDataScreen = new Intent(ViewOrderHistory.this, OrderHistoryDetailsActivity.class);
                goToUserDataScreen.putExtra("orderDetails", (Serializable) orderDetails);
                goToUserDataScreen.putIntegerArrayListExtra("mapOrderNumRows", mapOrderNumRowId.get(arrItemDetail[0].trim()));
                startActivity(goToUserDataScreen);

            }

            @Override
            public void onLongClick(View view, final int position) {
                /*                *//*Toast.makeText(ShowList.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();*//*

                AlertDialog alertbox = new AlertDialog.Builder(ViewOrderHistory.this)
                        .setMessage("Do you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                addedItemDetailArray.remove(position);

                                //Float priceToBeDeducted = Float.parseFloat(addedItemPriceArray.get(position));
                                //Float updatedAmount = totalAmount - priceToBeDeducted;
                                *//*displayAmountTextView.setText(displayAmountTextView.getText().charAt(0) +
                                        String.valueOf(updatedAmount));
                                *//*
                                //addedItemPriceArray.remove(position);
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();
            */}
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
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


    //AsyncTask to send data as a http POST request
    private class GetDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type

                Request request = new Request.Builder()
                        .url(URL)
                        .get()
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();

                jsonResponse = new JSONObject(response.body().string());
                checkResponse = true;
            } catch(Exception ex){
                result = false;
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            /*Toast.makeText( , result?"Message successfully sent!":"There was some error in sending message. Please try again after some time.",Toast.LENGTH_LONG).show();*/
        }

    }
}
