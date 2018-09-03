package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    int radioButtonID;
    String selectedOrderTypeText;

    RadioButton selectedOrderType;
    RadioGroup itemTypeRadioGroup;

    TextView itemTypeTextView;
    Button itemTypeButton;

    TextView subTypeTextView;
    Button subTypeButton;

    TextView quantityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        /* AlertDialog for selection of Item Type - Start */
        listItemType = getResources().getStringArray(R.array.itemType);

        Button itemTypeButton = (Button) findViewById(R.id.buttonItemType);
        final TextView itemTypeDisplayTextView = (TextView) findViewById(R.id.textDisplayItem);
        itemTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemTypeView) {
                AlertDialog.Builder itemTypeBuilder = new AlertDialog.Builder(PlaceOrder.this);
                itemTypeBuilder.setTitle("Choose an item");
                itemTypeBuilder.setSingleChoiceItems(listItemType,
                        -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterfaceItemType, int i) {
                        itemTypeDisplayTextView.setText(listItemType[i]);
                        dialogInterfaceItemType.dismiss();
                    }
                });
                AlertDialog dialogDisplayItemType = itemTypeBuilder.create();
                dialogDisplayItemType.show();
            }
        });
        /* AlertDialog for selection of Item Type - End */

        /* AlertDialog for selection of Sub Type - Start */
        if (selectedOrderTypeText == "Shirt"){
            listSubType = getResources().getStringArray(R.array.subType_Shirt);
        }
        else if (selectedOrderTypeText == "Pant"){
            listSubType = getResources().getStringArray(R.array.subType_Pant);
        }
        else if (selectedOrderTypeText == "Saree"){
            listSubType = getResources().getStringArray(R.array.subType_Saree);
        }
        else{
            Toast.makeText(PlaceOrder.this,
                    "Select 'Wash, Iron or Darning' option",Toast.LENGTH_SHORT).show();
        }
        /* AlertDialog for selection of Sub Type - End */

    }

    public void onChangingOrderTypeRadioButton(View orderTypeRadioButtonView) {
        /* Check if order type is selected - Start */
        itemTypeRadioGroup = (RadioGroup) findViewById(R.id.radioBoxOrderType);
        radioButtonID = itemTypeRadioGroup.getCheckedRadioButtonId();

        if(radioButtonID!=-1){
            selectedOrderType = (RadioButton) findViewById(radioButtonID);
            selectedOrderTypeText = selectedOrderType.getText().toString();
        }
        else{
            Toast.makeText(PlaceOrder.this,
                    "Select 'Wash, Iron or Darning' option",Toast.LENGTH_SHORT).show();
        }
        /* Check if order type is selected - End */
    }

}
