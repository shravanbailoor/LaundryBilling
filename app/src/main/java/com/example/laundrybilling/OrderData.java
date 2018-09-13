package com.example.laundrybilling;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderData extends AppCompatActivity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/e/1FAIpQLSfnPibdSt5cVxLHuqTFbj7jVD5gBs3CeB9hJ8m5-2IC-SUVnw/formResponse";
    //input element ids found from the live form page
    public static final String ORDERNUMBER_KEY="entry.1243630790";
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

    TextView orderNumber;
    EditText comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_data);

        orderNumber = findViewById(R.id.textViewDisplayOrderNumber);
        comments = findViewById(R.id.tvComments);

        if(orderNumber.getText().toString().equalsIgnoreCase("")) {
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
            orderNumber.setText(newKey);
        }
    }

    public void onClickPrint(View view) {
        Intent goToShowList = getIntent();
        OrderDetails orderDetails = (OrderDetails) goToShowList.getSerializableExtra("orderDetails");
        orderDetails.setOrderNumber(orderNumber.getText().toString());
        orderDetails.setOrderComments(comments.getText().toString());

        sendSMS("5554","This is a test message");
        //Create an object for PostDataTask AsyncTask
        PostDataTask postDataTask = new PostDataTask();

        for (Items item: orderDetails.getItems()
             ) {
            //execute asynctask
            postDataTask.execute(URL,orderDetails.getOrderNumber(),
                    orderDetails.getCustomerName(),item.getTypeWork(),
                    item.getItemType(),item.getItemSubType(),String.valueOf(item.getPrice()),
                    String.valueOf(item.getQuantity()),String.valueOf(item.getItemTotalPrice()),String.valueOf(orderDetails.getTotalCost()),
                    orderDetails.getDeliveryDate(), orderDetails.getOrderComments());

        }

    }

    //Send SMS functionality - Start - 12092018
    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(OrderData.this, SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(OrderData.this, SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...",Toast.LENGTH_SHORT).show();
        }

    }

    public class SmsDeliveredReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(OrderData.this, "SMS delivered", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(OrderData.this, "SMS not delivered", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public class SmsSentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(OrderData.this, "SMS Sent", Toast.LENGTH_SHORT).show();

                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(OrderData.this, "SMS generic failure", Toast.LENGTH_SHORT)
                            .show();

                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(OrderData.this, "SMS no service", Toast.LENGTH_SHORT)
                            .show();

                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(OrderData.this, "SMS null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(OrderData.this, "SMS radio off", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    //Send SMS functionality - End - 12092018

    //AsyncTask to send data as a http POST request
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
                        "&" + ORDERNUMBER_KEY+"=" + URLEncoder.encode(orderNumber,"UTF-8") +
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
}
