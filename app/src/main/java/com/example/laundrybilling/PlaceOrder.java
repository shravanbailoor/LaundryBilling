package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceOrder extends AppCompatActivity {

    String[] listItemType;
    String[] listSubType;

    String selectedItemTypeDisplayText;

    Boolean itemTypeSelected;
    Boolean subTypeSelected;
    Boolean quantitySelected;
    Boolean orderTypeSelected;

    RadioGroup radioGroupOrderType;

    TextView textDisplayQuantity;
    TextView textPrice;
    TextView textCustomerName;
    TextView textItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        radioGroupOrderType = findViewById(R.id.radioBoxOrderType);
        textCustomerName = findViewById(R.id.tbCustomerName);
        textItem = findViewById(R.id.tbItem);
        textPrice = findViewById(R.id.tbPrice);
        textDisplayQuantity = findViewById(R.id.textDisplayQuantity);

    }

    public void onClickQuantityDecreaseButton(View quantityButtonView) {
        /* Update counter textView on decrease in Quantity - Start */
        if (textDisplayQuantity.getText().toString().equals("1")) {
            Toast.makeText(PlaceOrder.this,
                    "'Quantity cannot be less than one'", Toast.LENGTH_SHORT).show();
        } else {
            Integer count = Integer.parseInt(textDisplayQuantity.getText().toString());
            count = count - 1;
            textDisplayQuantity.setText(String.valueOf(count));
        }
        /* Update counter textView on decrease in Quantity - End */
    }

    public void onClickQuantityIncreaseButton(View quantityButtonView) {
        /* Update counter textView on increase in Quantity - Start */
        Integer count = Integer.parseInt(textDisplayQuantity.getText().toString());
        count = count + 1;
        textDisplayQuantity.setText(String.valueOf(count));
        /* Update counter textView on increase in Quantity - End */
    }

    public void onClickAddBillItem(View addBillItemView) {
        /* Validation - Start */
        orderTypeSelected = Boolean.FALSE;
        itemTypeSelected = Boolean.FALSE;
        subTypeSelected = Boolean.FALSE;

        int selectedRadioButtonId = radioGroupOrderType.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(PlaceOrder.this,
                    "Select 'Wash', 'Iron' or 'Darning' option", Toast.LENGTH_SHORT).show();
        } else {
            orderTypeSelected = Boolean.TRUE;
        }

        if (textItem.getText() != null && textItem.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Please enter Item", Toast.LENGTH_SHORT).show();
        } else {
            itemTypeSelected = Boolean.TRUE;
        }


        if (textCustomerName.getText()!=null && textCustomerName.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Please enter Customer Name", Toast.LENGTH_SHORT).show();
        } else {
            subTypeSelected = Boolean.TRUE;
        }


        if (textPrice.getText()!=null && textPrice.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Please enter Price", Toast.LENGTH_SHORT).show();
        } else {
            subTypeSelected = Boolean.TRUE;
        }
        /* Validation - End */

        if (orderTypeSelected == Boolean.TRUE && itemTypeSelected == Boolean.TRUE &&
                subTypeSelected == Boolean.TRUE) {

            //Save the data. Send the data to the list screen; swipe the screen to check it
        }


    }

    public void onClickConfirmButton(View confirmButtonView) {
        //
    }

}
