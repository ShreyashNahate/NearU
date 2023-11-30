package com.example.nearucustomer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import java.util.List;

// GridAdapter.java
public class GridAdapter extends BaseAdapter {
    private List<Item> itemList;
    private Context context;

    public GridAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recycler_item, null);
        }

            TextView itemName = view.findViewById(R.id.price);
            Item price = itemList.get(position);
            itemName.setText(price.getDataPrice());

            TextView itemprice = view.findViewById(R.id.title);
            Item cat = itemList.get(position);
            itemprice.setText(cat.getDataCategory());

            TextView statusicon = view.findViewById(R.id.statusicon);
            Item cat2 = itemList.get(position);
            statusicon.setText(cat2.getDataType());
                if (statusicon.getText().toString().toLowerCase().contains("for rent"))
                {
                    statusicon.setText("");
                    statusicon.setBackgroundColor(Color.RED);
                }else{
                    statusicon.setText("");
                    statusicon.setBackgroundColor(Color.GREEN);
                }
            ImageView itemName2 = view.findViewById(R.id.gridImage);
            CardView recCard = view.findViewById(R.id.recCard);
            Glide.with(context).load(itemList.get(position).getDataImage()).into(itemName2);
            recCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("Image", itemList.get(position).getDataImage());
                    intent.putExtra("Price", itemList.get(position).getDataPrice());
                    intent.putExtra("Number", itemList.get(position).getDataNumber());
                    intent.putExtra("Location", itemList.get(position).getDataLocation());
                    intent.putExtra("Desc", itemList.get(position).getDataDescription());
                    intent.putExtra("Category", itemList.get(position).getDataCategory());
                    intent.putExtra("Type", itemList.get(position).getDataType());
                    context.startActivity(intent);
                }
            });




        return view;
    }
}
