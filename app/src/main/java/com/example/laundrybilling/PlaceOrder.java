package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.google.firebase.database.DatabaseReference;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaceOrder extends AppCompatActivity {

    String[] listItemType;
    String[] listSubType;

    Button itemTypeButton;

    String selectedItemTypeDisplayText;

    Boolean itemTypeSelected;
    Boolean subTypeSelected;
    Boolean quantitySelected;
    Boolean orderTypeSelected;
    Boolean allChecksPassed;

    RadioGroup radioGroupOrderType;

    TextView textDisplayQuantity;
    TextView textPrice;
    TextView textSubItem;
    TextView textItem;
    String showListString;
    ArrayList showListArray;

    String totalItemPrice;
    ArrayList itemPriceArray;

    //private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        //mDatabase = FirebaseDatabase.getInstance().getReference();

        itemTypeButton = findViewById(R.id.buttonItemType);
        radioGroupOrderType = findViewById(R.id.radioGroupOrderType);
        textSubItem = findViewById(R.id.textBoxSubItem);
        itemTypeButton = findViewById(R.id.buttonItemType);
        textPrice = findViewById(R.id.textBoxPrice);
        textDisplayQuantity = findViewById(R.id.textViewDisplayQuantity);

        allChecksPassed = Boolean.FALSE;

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

        if (itemTypeButton.getText() != null &&
                itemTypeButton.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Select an Item", Toast.LENGTH_SHORT).show();
        } else {
            itemTypeSelected = Boolean.TRUE;
        }


        if (textSubItem.getText() != null &&
                textSubItem.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Enter the Sub Type", Toast.LENGTH_SHORT).show();
        } else {
            subTypeSelected = Boolean.TRUE;
        }


        if (textPrice.getText()!=null && textPrice.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Enter the Price", Toast.LENGTH_SHORT).show();
        } else {
            subTypeSelected = Boolean.TRUE;
        }
        /* Validation - End */

        if (orderTypeSelected == Boolean.TRUE && itemTypeSelected == Boolean.TRUE &&
                subTypeSelected == Boolean.TRUE) {

            allChecksPassed = Boolean.TRUE;

            int selectedOrderType = radioGroupOrderType.getCheckedRadioButtonId();
            String orderTypeCode = null;

            RadioButton radioButtonOrderType = findViewById(selectedOrderType);

            CharSequence radioButtonText = radioButtonOrderType.getText();

            if (radioButtonText.equals("Wash")) {
                orderTypeCode = "W";
            } else if (radioButtonText.equals("Iron")) {
                orderTypeCode = "I";
            } else if (radioButtonText.equals("Darning")) {
                orderTypeCode = "D";
            }

            if (showListArray == null) {
                showListArray = new ArrayList<>();
            }

            Integer intPrice = Integer.parseInt(textPrice.getText().toString());
            Integer intQuantity = Integer.parseInt(textDisplayQuantity.getText().toString());
            Float floatMultiplyValue = (float) intPrice * intQuantity;
            totalItemPrice = String.valueOf(floatMultiplyValue);

            showListArray.add("| " + orderTypeCode + " | " + itemTypeButton.getText() + " - " +
                    textSubItem.getText() + " | Rs. " + textPrice.getText() +
                    " | x" + textDisplayQuantity.getText() + " | --- Rs. " + totalItemPrice);

            if (itemPriceArray == null) {
                itemPriceArray = new ArrayList<>();
            }
            itemPriceArray.add(totalItemPrice);

            Toast.makeText(PlaceOrder.this,
                    "Item Added", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickItemTypeButton(View itemTypeButtonView) {
        final String oldText = itemTypeButton.getText().toString();

        /* AlertDialog for selection of Item Type - Start */
        listItemType = getResources().getStringArray(R.array.itemType);

        AlertDialog.Builder itemTypeBuilder = new AlertDialog.Builder(PlaceOrder.this);
        itemTypeBuilder.setTitle("Choose an item");
        itemTypeBuilder.setSingleChoiceItems(listItemType,
                -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterfaceItemType, int i) {
                        itemTypeButton.setText(listItemType[i]);
                        if (!(itemTypeButton.getText().toString().equals(oldText))) {
                            textSubItem.setText("");
                            textPrice.setText("");
                            textDisplayQuantity.setText(R.string.initial_quantity);
                        }
                        dialogInterfaceItemType.dismiss();
                    }
                });
        AlertDialog dialogDisplayItemType = itemTypeBuilder.create();
        dialogDisplayItemType.show();

        /* AlertDialog for selection of Item Type - End */

    }

    public void onClickShowListButton(View showListButtonView) {
        if (allChecksPassed == Boolean.TRUE) {
            Intent goToListScreen = new Intent(this, ShowList.class);
            //goToListScreen.putExtra("addedItemDetail",showListString);
            goToListScreen.putExtra("addedItemDetail", showListArray);
            goToListScreen.putExtra("addedItemPrice", itemPriceArray);
            startActivity(goToListScreen);
        } else {
            Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
