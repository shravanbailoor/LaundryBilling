package com.example.laundrybilling;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

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

    TextView orderNumber;
    EditText comments;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private static boolean checkOrderNumber = false;

    boolean flagPrint;
    boolean flagSms;
    boolean flagSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_data);

        orderNumber = findViewById(R.id.textViewDisplayOrderNumber);
        comments = findViewById(R.id.tvComments);


            orderNumber.setText(MainActivity.orderNumber);

    }

    public void onClickPrint(View view) {
        checkOrderNumber = false;
        Intent goToShowList = getIntent();
        OrderDetails orderDetails = (OrderDetails) goToShowList.getSerializableExtra("orderDetails");
        orderDetails.setOrderNumber(orderNumber.getText().toString());
        orderDetails.setOrderComments(comments.getText().toString());

        PostDataTask postDataTask ;
        int count = 1;
        for (Items item: orderDetails.getItems()
                ) {
            //execute asynctask
            String isLast = "Not last one";
            if(count == orderDetails.getItems().size()){
                isLast = "Last";
            }
            postDataTask = new PostDataTask();
            postDataTask.execute(orderDetails.getOrderNumber(),
                    orderDetails.getCustomerName(), String.valueOf(orderDetails.getPhoneNumber()), item.getTypeWork(),
                    item.getItemType(), item.getItemSubType(), String.valueOf(item.getPrice()),
                    String.valueOf(item.getQuantity()), String.valueOf(item.getItemTotalPrice()), String.valueOf(orderDetails.getTotalCost()),
                    orderDetails.getDeliveryDate(), orderDetails.getOrderComments(),isLast);
            count++;
        }
        if(flagSave) {
            try {
                openBluetooth();
                sendData();
                closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this, "Some problem occured while saving the data to Database. Please check internet connection.", Toast.LENGTH_LONG);
        }
        if(flagPrint) {
            sendSMS(String.valueOf(orderDetails.getPhoneNumber()), "This is a test message");
        }else{
            Toast.makeText(this, "Some problem occured while printing. Please check bluetooth connection.", Toast.LENGTH_LONG);
        }
        //Create an object for PostDataTask AsyncTask
        if(flagSms){
            MainActivity.orderNumber = "";
            Intent goToMainScreen = new Intent(this, MainActivity.class);
            startActivity(goToMainScreen);

        }



    }

    // open bluetooth connection

    public void openBluetooth() {
        try {
            findBT();
            openBT();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // this will find a bluetooth printer device
    void findBT() {

        try {
            PackageManager pm = this.getPackageManager();
            boolean hasBluetooth = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                //myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("RPP300")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            //myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            //myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Send data typed by the user to be printed

    void sendData() throws IOException {
        try {

            // the text typed by the user
            String msg = "Content to print";
            msg += "\n";

            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent
           // myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Send SMS functionality - Start - 12092018
    private void sendSMS(String phoneNumber, String message) {
        phoneNumber = "0" + phoneNumber;
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
            String isLast = contactData[12];
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
                if(isLast.equalsIgnoreCase("Last")){
                    response.body().toString();
                }
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
