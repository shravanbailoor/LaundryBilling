package com.example.laundrybilling;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final MediaType APPLICATION_JSON
            = MediaType.parse("application/json; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/u/1/d/e/1FAIpQLScv7ysGAWZnPPQ5nb-E6rRmRUZFP1rI2fAx6_60hwsW7Cpg1Q/formResponse";

    public static final String DELETE_URL = "https://sheets.googleapis.com/v4/spreadsheets/1i1chsepnCq2w2AXi14CIpuFolqbytd1COJXyXfyMi20:batchUpdate";
    //input element ids found from the live form page
    public static final String ORDERNUMBER_KEY="entry.146066368";
    public static final String CUSTOMERNAME_KEY="entry.1302306441";
    public static final String PHONENUMBER_KEY="entry.1863831328";
    public static final String WORKTYPE_KEY="entry.761737797";
    public static final String ITEMTYPE_KEY="entry.1428843208";
    public static final String ITEMSUBTYPE_KEY="entry.319783465";
    public static final String PRICE_KEY="entry.1202663930";
    public static final String QUANTITY_KEY="entry.550504081";
    public static final String ITEMTOTALPRICE_KEY="entry.1922893847";
    public static final String ORDERTOTALPRICE_KEY="entry.161913928";
    public static final String DELIVERYDATE_KEY="entry.2095931121";
    public static final String COMMENTS_KEY="entry.702541209";
    public static final String ORDEREDDATE_KEY="entry.1582298510";


    RecyclerView recyclerView;

    TextView orderNumber;
    TextView customerName;
    TextView deliveryDate;
    TextView phoneNumber;
    TextView totalCost;

    Button btnOrderCompleted;

    OrderDetails orderDetails ;

    ArrayList<String> addedItemDetailArray;
    ArrayList<Integer> listRowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_details);
        addedItemDetailArray = new ArrayList<>();
        orderNumber = findViewById(R.id.textViewHisDisplayOrderNumber);
        customerName = findViewById(R.id.textViewHisCustomerName);
        deliveryDate = findViewById(R.id.textViewHisDeliveryDate);
        phoneNumber = findViewById(R.id.textViewHisPhoneNumber);
        totalCost = findViewById(R.id.textViewHisDisplayAmount);
        btnOrderCompleted = findViewById(R.id.buttonCompleted);

        Intent goToShowList = getIntent();
        orderDetails = (OrderDetails) goToShowList.getSerializableExtra("orderDetails");
        listRowId = goToShowList.getIntegerArrayListExtra("mapOrderNumRows");
        orderNumber.setText(orderDetails.getOrderNumber());
        customerName.setText(orderDetails.getCustomerName());
        deliveryDate.setText(orderDetails.getDeliveryDate());
        phoneNumber.setText(String.valueOf(orderDetails.getPhoneNumber()));
        totalCost.setText(totalCost.getText() + String.valueOf(orderDetails.getTotalCost()));

        List<Items> listItems = orderDetails.getItems();
        for (Items item:
             listItems) {
            String itemDetails = "";
            itemDetails = item.getTypeWork() + " | " + item.getItemType() + " | " + item.getItemSubType() + " | " + item.getQuantity() + " | " + item.getPrice();
            addedItemDetailArray.add(itemDetails);
        }

        recyclerView = findViewById(R.id.recyclerViewHisShowItems);

        /* addedItems = new ArrayList<>();
        addedItems.add("India");
        addedItems.add("Germany");
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItems); */

        final RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItemDetailArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickCompleted(View view) {

        OrderHistoryDetailsActivity.PostDataTask postDataTask = new OrderHistoryDetailsActivity.PostDataTask();

        for (Items item: orderDetails.getItems()
                ) {
            //execute asynctask
            postDataTask = new OrderHistoryDetailsActivity.PostDataTask();
            postDataTask.execute(orderDetails.getOrderNumber(),
                    orderDetails.getCustomerName(), String.valueOf(orderDetails.getPhoneNumber()), item.getTypeWork(),
                    item.getItemType(), item.getItemSubType(), String.valueOf(item.getPrice()),
                    String.valueOf(item.getQuantity()), String.valueOf(item.getItemTotalPrice()), String.valueOf(orderDetails.getTotalCost()),
                    orderDetails.getDeliveryDate(), orderDetails.getOrderComments());


        }

        DeleteDataTask deleteDataTask ;
        for(Integer intRow: listRowId){
            deleteDataTask = new DeleteDataTask();
            deleteDataTask.execute(String.valueOf(intRow));
        }

    }
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String orderNumber = contactData[0];
            String customerName = contactData[1];
            String phoneNumber = contactData[2];
            String workType = contactData[3];
            String itemType = contactData[4];
            String itemSubType = contactData[5];
            Double price = Double.valueOf(contactData[6]);
            int quantity = Integer.valueOf(contactData[7]);
            Double itemPriceTotal = Double.valueOf(contactData[8]);
            Double orderTotalCost = Double.valueOf(contactData[9]);
            String deliveryDate = contactData[10];
            String comments = contactData[11];
            String orderedDate = String.valueOf(Calendar.getInstance().getTime());
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = ORDERNUMBER_KEY+"=" + URLEncoder.encode(orderNumber,"UTF-8") +
                        "&" + CUSTOMERNAME_KEY + "=" + URLEncoder.encode(customerName,"UTF-8") +
                        "&" + PHONENUMBER_KEY + "=" + URLEncoder.encode(phoneNumber,"UTF-8") +
                        "&" + WORKTYPE_KEY + "=" + URLEncoder.encode(workType,"UTF-8") +
                        "&" + ITEMTYPE_KEY + "=" + URLEncoder.encode(itemType,"UTF-8") +
                        "&" + ITEMSUBTYPE_KEY+"=" + URLEncoder.encode(itemSubType,"UTF-8") +
                        "&" + PRICE_KEY + "=" + price +
                        "&" + QUANTITY_KEY + "=" + quantity +
                        "&" + ITEMTOTALPRICE_KEY +"=" + itemPriceTotal +
                        "&" + ORDERTOTALPRICE_KEY + "=" + orderTotalCost +
                        "&" + DELIVERYDATE_KEY + "=" + URLEncoder.encode(deliveryDate,"UTF-8") +
                        "&" + COMMENTS_KEY+"=" + URLEncoder.encode(comments,"UTF-8") +
                        "&" + ORDEREDDATE_KEY + "=" + URLEncoder.encode(orderedDate,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }

            /*
            //If you want to use HttpRequest class from http://stackoverflow.com/a/2253280/1261816
            try {
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.sendPost(url, postBody);
		}catch (Exception exception){
			result = false;
		}
            */

            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            /*Toast.makeText( , result?"Message successfully sent!":"There was some error in sending message. Please try again after some time.",Toast.LENGTH_LONG).show();*/
        }

    }


    private class DeleteDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String postBody="";

            //all values must be URL encoded to make sure that special characters like & | ",etc.
            //do not cause problems
            postBody = "{\n" +
                    "  \"requests\": [\n" +
                    "    {\n" +
                    "      \"deleteDimension\": {\n" +
                    "        \"range\": {\n" +
                    "          \"sheetId\": sheetId,\n" +
                    "          \"dimension\": \"ROWS\",\n" +
                    "          \"startIndex\": "+contactData[0]+",\n" +
                    "          \"endIndex\": "+contactData[0]+"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }]}";


            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(APPLICATION_JSON, postBody);
                Request request = new Request.Builder()
                        .url(DELETE_URL)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
                String check = response.body().string();
            }catch (IOException exception){
                result=false;
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
