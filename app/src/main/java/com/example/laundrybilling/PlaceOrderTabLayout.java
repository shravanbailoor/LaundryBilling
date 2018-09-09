package com.example.laundrybilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceOrderTabLayout extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    String[] listItemType;

    Boolean itemTypeSelected;
    Boolean subTypeSelected;
    Boolean quantitySelected;
    Boolean orderTypeSelected;

    RadioGroup radioGroupOrderType;

    TextView textDisplayQuantity;
    TextView textPrice;
    TextView textSubItem;
    TextView textItem;

    Button itemTypeButton;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order_tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                rootView = inflater.inflate(R.layout.activity_place_order, container, false);



            }else {
                rootView = inflater.inflate(R.layout.place_order_summary, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            }
            return rootView;
        }
    }


    public void OnClickItemTypeButton(View itemTypeButtonView) {


        final String oldText = itemTypeButton.getText().toString();

        /* AlertDialog for selection of Item Type - Start */
        listItemType = getResources().getStringArray(R.array.itemType);

        AlertDialog.Builder itemTypeBuilder = new AlertDialog.Builder(PlaceOrderTabLayout.this);
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

    public void onClickAddBillItem(View addBillItemView) {
        /* Validation - Start */

        orderTypeSelected = Boolean.FALSE;
        itemTypeSelected = Boolean.FALSE;
        subTypeSelected = Boolean.FALSE;
        if(radioGroupOrderType == null) {
            radioGroupOrderType = findViewById(R.id.radioBoxOrderType);
        }
        if(itemTypeButton == null) {
            itemTypeButton= findViewById(R.id.buttonItemType);
        }
        if(textPrice == null) {
            textPrice = findViewById(R.id.tbPrice);
        }
        if(textDisplayQuantity == null) {
            textDisplayQuantity = findViewById(R.id.textDisplayQuantity);
        }

        int selectedRadioButtonId = radioGroupOrderType.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(PlaceOrderTabLayout.this,
                    "Select 'Wash', 'Iron' or 'Darning' option", Toast.LENGTH_SHORT).show();
        } else {
            orderTypeSelected = Boolean.TRUE;
        }

        if (itemTypeButton.getText() != null && itemTypeButton.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrderTabLayout.this,
                    "Please enter Item", Toast.LENGTH_SHORT).show();
        } else {
            itemTypeSelected = Boolean.TRUE;
        }


        if (textSubItem.getText()!=null && textSubItem.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrderTabLayout.this,
                    "Please enter Customer Name", Toast.LENGTH_SHORT).show();
        } else {
            subTypeSelected = Boolean.TRUE;
        }


        if (textPrice.getText()!=null && textPrice.getText().toString().toLowerCase().equals("")) {
            Toast.makeText(PlaceOrderTabLayout.this,
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
        Intent goToUserDataScreen = new Intent(this, UserData.class);
        startActivity(goToUserDataScreen);
    }

    public void onClickQuantityDecreaseButton(View quantityButtonView) {
        /* Update counter textView on decrease in Quantity - Start */
        if (textDisplayQuantity.getText().toString().equals("1")) {
            Toast.makeText(PlaceOrderTabLayout.this,
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(itemTypeButton == null) {
                itemTypeButton = findViewById(R.id.buttonItemType);
            }
            if(radioGroupOrderType == null) {
                radioGroupOrderType = findViewById(R.id.radioBoxOrderType);
            }
            if(textSubItem == null) {
                textSubItem = findViewById(R.id.tbSubItem);
            }
            if(textPrice == null) {
                textPrice = findViewById(R.id.tbPrice);
            }
            if(textDisplayQuantity == null) {
                textDisplayQuantity = findViewById(R.id.textDisplayQuantity);
            }
            return 2;
        }
    }
}
