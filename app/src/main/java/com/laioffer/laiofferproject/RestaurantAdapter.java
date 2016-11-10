package com.laioffer.laiofferproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
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

        TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView restaurantAddress = (TextView) convertView.findViewById(R.id.restaurant_address);
        TextView restaurantType = (TextView) convertView.findViewById(R.id.restaurant_type);

        ImageView restaurantThumbnail = (ImageView) convertView.findViewById(R.id.restaurant_thumbnail);
        //ImageView restaurantRating = (ImageView) convertView.findViewById(
        //R.id.restaurant_rating);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.restaurant_rating);
        final LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        /*restaurantThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stars.getDrawable(2).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                Log.e("Life", "try");
            }
        });*/

        final ImageView visited = (ImageView) convertView.findViewById(R.id.imageView);
        final Restaurant r = restaurantData.get(position);
        restaurantName.setText(r.getName());
        restaurantAddress.setText(r.getAddress());
        restaurantType.setText(r.getCategories().get(0));

        restaurantThumbnail.setImageBitmap(r.getThumbnail());
        //restaurantRating.setImageBitmap(r.getRating());
        ratingBar.setRating((float)r.getStars());
        setImageView(visited, r.isVisited());
        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageView(visited, !r.isVisited());
                r.setVisited(!r.isVisited());
            }
        });
        return convertView;
    }

    private void setImageView(ImageView visited, boolean val) {
        if (val == true) {
            visited.setImageResource(R.drawable.presence_online);
        } else {
            visited.setImageResource(R.drawable.presence_invisible);
        }
    }
}
