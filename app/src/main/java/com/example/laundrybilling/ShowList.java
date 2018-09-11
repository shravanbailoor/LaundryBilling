package com.example.laundrybilling;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<String> addedItemDetailArray;
    ArrayList<String> addedItemPriceArray;

    Float totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        Intent goToShowList = getIntent();
        addedItemDetailArray = goToShowList.getStringArrayListExtra("addedItemDetail");
        addedItemPriceArray = goToShowList.getStringArrayListExtra("addedItemPrice");

        for (int i = 0; i < addedItemPriceArray.size(); i++) {
            String var = addedItemPriceArray.get(i);
            Float floatAmount = Float.parseFloat(var);

            if (totalAmount == null) {
                totalAmount = floatAmount;
            } else {
                totalAmount = totalAmount + floatAmount;
            }
            //statements using var;
        }

        TextView displayAmountTextView = findViewById(R.id.textViewDisplayAmount);
        displayAmountTextView.setText(displayAmountTextView.getText().charAt(0) +
                String.valueOf(totalAmount));

        recyclerView = findViewById(R.id.recyclerViewShowList);

        /* addedItems = new ArrayList<>();
        addedItems.add("India");
        addedItems.add("Germany");
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItems); */

        RecyclerView.Adapter adapter = new RecyclerViewAdapter(addedItemDetailArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*Toast.makeText(ShowList.this, "Single Click on position :" + position,
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onLongClick(View view, int position) {
                /*Toast.makeText(ShowList.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();*/
            }
        }));
    }

    public void onClickConfirmItems(View viewConfirmItems) {
        Intent goToUserDataScreen = new Intent(this, UserData.class);
        goToUserDataScreen.putExtra("addedItemDetail", addedItemDetailArray);
        goToUserDataScreen.putExtra("addedItemPrice", addedItemPriceArray);
        startActivity(goToUserDataScreen);
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
}