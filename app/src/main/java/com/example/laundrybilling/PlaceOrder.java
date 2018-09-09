package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    Button itemTypeButton;

    String selectedItemTypeDisplayText;

    Boolean itemTypeSelected;
    Boolean subTypeSelected;
    Boolean quantitySelected;
    Boolean orderTypeSelected;

    RadioGroup radioGroupOrderType;

    TextView textDisplayQuantity;
    TextView textPrice;
    TextView textSubItem;
    TextView textItem;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        //mDatabase = FirebaseDatabase.getInstance().getReference();

        itemTypeButton = findViewById(R.id.buttonItemType);
        radioGroupOrderType = findViewById(R.id.radioBoxOrderType);
        textSubItem = findViewById(R.id.tbSubItem);
        itemTypeButton = findViewById(R.id.buttonItemType);
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

        if (itemTypeButton.getText() != null && itemTypeButton.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrder.this,
                    "Please enter Item", Toast.LENGTH_SHORT).show();
        } else {
            itemTypeSelected = Boolean.TRUE;
        }


        if (textSubItem.getText()!=null && textSubItem.getText().toString().toLowerCase().equals("")) {
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
                        if (itemTypeButton.getText().toString().equals(oldText)) {
                            //Do nothing
                        } else {
                            textDisplayQuantity.setText(R.string.initial_quantity);
                        }
                        dialogInterfaceItemType.dismiss();
                    }
                });
        AlertDialog dialogDisplayItemType = itemTypeBuilder.create();
        dialogDisplayItemType.show();

        /* AlertDialog for selection of Item Type - End */

    }

    public void onClickConfirmButton(View confirmButtonView) {

    }

}
