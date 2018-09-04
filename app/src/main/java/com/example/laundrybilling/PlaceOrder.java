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

    Button itemTypeButton;
    Button subTypeButton;

    String selectedItemTypeDisplayText;

    Boolean itemTypeSelected;
    Boolean subTypeSelected;
    Boolean quantitySelected;
    Boolean orderTypeSelected;

    RadioGroup radioGroupOrderType;

    TextView textDisplayQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        radioGroupOrderType = findViewById(R.id.radioBoxOrderType);
        itemTypeButton = findViewById(R.id.buttonItemType);
        subTypeButton = findViewById(R.id.buttonSubType);
        textDisplayQuantity = findViewById(R.id.textDisplayQuantity);

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
                            subTypeButton.setText(R.string.tap_to_select_name);
                            textDisplayQuantity.setText(R.string.initial_quantity);
                        }
                        dialogInterfaceItemType.dismiss();
                    }
                });
        AlertDialog dialogDisplayItemType = itemTypeBuilder.create();
        dialogDisplayItemType.show();

        /* AlertDialog for selection of Item Type - End */

    }

    public void onClickSubTypeButton(View subTypeButtonView) {
        final String oldText = subTypeButton.getText().toString();

        /* AlertDialog for selection of Sub Type - Start */
        selectedItemTypeDisplayText = itemTypeButton.getText().toString();

        itemTypeSelected = Boolean.FALSE;

        switch (selectedItemTypeDisplayText) {
            case "Shirt":
                listSubType = getResources().getStringArray(R.array.subType_Shirt);
                break;

            case "Pant":
                listSubType = getResources().getStringArray(R.array.subType_Pant);
                break;

            case "Saree":
                listSubType = getResources().getStringArray(R.array.subType_Saree);
                break;

            default:
                itemTypeSelected = Boolean.TRUE;
        }

        if (itemTypeSelected == Boolean.FALSE) {
            AlertDialog.Builder subTypeBuilder = new AlertDialog.Builder(PlaceOrder.this);
            subTypeBuilder.setTitle("Select the sub type");
            subTypeBuilder.setSingleChoiceItems(listSubType,
                    -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterfaceSubType, int i) {
                            subTypeButton.setText(listSubType[i]);
                            if (subTypeButton.getText().toString().equals(oldText)) {
                                //Do nothing
                            } else {
                                textDisplayQuantity.setText(R.string.initial_quantity);
                            }
                            dialogInterfaceSubType.dismiss();
                        }
                    });
            AlertDialog dialogDisplaySubType = subTypeBuilder.create();
            dialogDisplaySubType.show();

        } else {
            Toast.makeText(PlaceOrder.this,
                    "Select an 'Item'", Toast.LENGTH_SHORT).show();
        }

        /* AlertDialog for selection of Sub Type - End */

        /* Restore the text to default if there is a change in sub type - Start */

        /* Restore the text to default if there is a change in sub type - End */
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
        if (selectedRadioButtonId != -1) {
            Toast.makeText(PlaceOrder.this,
                    "Select 'Wash', 'Iron' or 'Darning' option", Toast.LENGTH_SHORT).show();
        } else {
            orderTypeSelected = Boolean.TRUE;
        }

        if (itemTypeButton.getText().toString().toLowerCase().equals("tap to select")) {
            Toast.makeText(PlaceOrder.this,
                    "Select an 'Item'", Toast.LENGTH_SHORT).show();
        } else {
            itemTypeSelected = Boolean.TRUE;
        }

        if (subTypeButton.getText().toString().toLowerCase().equals("tap to select")) {
            Toast.makeText(PlaceOrder.this,
                    "Choose the 'Sub Type'", Toast.LENGTH_SHORT).show();
        } else {
            subTypeSelected = Boolean.TRUE;
        }

        /* Validation - End */
    }

}
