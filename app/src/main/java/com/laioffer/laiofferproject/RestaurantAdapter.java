package com.laioffer.laiofferproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PaiGe on 2016/10/25.
 */


public class RestaurantAdapter extends BaseAdapter {


    Context context;
    List<Restaurant> restaurantData;


    public RestaurantAdapter(Context context, List<Restaurant> restaurantData) {
        this.context = context;
        this.restaurantData = restaurantData;
    }


    @Override
    public int getCount() {
        return restaurantData.size();
    }


    @Override
    public Restaurant getItem(int position) {
        return restaurantData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_restaurant_list_item,
                    parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.restaurant_thumbnail);

        TextView restaurantName = (TextView) convertView.findViewById(
                R.id.restaurant_name);
        TextView restaurantAddress = (TextView) convertView.findViewById(
                R.id.restaurant_address);
        TextView restaurantType = (TextView) convertView.findViewById(
                R.id.restaurant_type);

        ImageView restaurantThumbnail = (ImageView) convertView.findViewById(
                R.id.restaurant_thumbnail);
        //ImageView restaurantRating = (ImageView) convertView.findViewById(
        //R.id.restaurant_rating);
        RatingBar ratingBar = (RatingBar)convertView.findViewById(R.id.restaurant_rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        Restaurant r = restaurantData.get(position);
        restaurantName.setText(r.getName());
        restaurantAddress.setText(r.getAddress());
        restaurantType.setText(r.getCategories().get(0));

        restaurantThumbnail.setImageBitmap(r.getThumbnail());
        //restaurantRating.setImageBitmap(r.getRating());
        ratingBar.setRating((float)r.getStars());
        return convertView;
    }
}
