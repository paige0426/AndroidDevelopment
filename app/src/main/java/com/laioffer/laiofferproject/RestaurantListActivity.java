package com.laioffer.laiofferproject;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListFragment.OnItemSelectListener {

    RestaurantListFragment listFragment;
    RestaurantGridFragment gridFragment;

    @Override
    public void onItemSelected(int position) {
        gridFragment.onItemSelected(position);
//        listFragment.onItemSelected(position);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
/*        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                YelpApi yelp = new YelpApi();
                yelp.searchForBusinessesByLocation("dinner", "San Francisco, CA", 20);
                return null;
            }
        }.execute();*/

/*        if (findViewById(R.id.fragment_container) != null) {
            Fragment fragment = isTablet() ?
                    new RestaurantGridFragment() : new RestaurantListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }*/
        if (findViewById(R.id.fragment_container) != null) {
            listFragment = new RestaurantListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, listFragment).commit();
        }

 /*       if (!isTablet()) {
            listFragment = new RestaurantListFragment();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_list_container, listFragment).commit();
        } else {
            listFragment = new RestaurantListFragment();
            gridFragment = new RestaurantGridFragment();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_list_container, listFragment).commit();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_grid_container, gridFragment).commit();
        }*/
    }


    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Life cycle test", "We are at onStart()");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Life cycle test", "We are at onResume()");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Life cycle test", "We are at onPause()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Life cycle test", "We are at onStop()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Life cycle test", "We are at onDestroy()");
    }

}
