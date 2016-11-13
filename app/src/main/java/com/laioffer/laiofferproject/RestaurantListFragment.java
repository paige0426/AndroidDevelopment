package com.laioffer.laiofferproject;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment {


    public RestaurantListFragment() {
        // Required empty public constructor
    }

    private ListView listView;
    private DataService dataService;
    private Clock clock;
    private Fragment mfragment;
    private final String recommendation = "http://fengdemeng.mooo.com:8080/Dashi/recommendation?user_id=";
    private final String search = "http://fengdemeng.mooo.com:8080/Dashi/restaurants?user_id=";
    private final String history = "http://fengdemeng.mooo.com:8080/Dashi/history?user_id=";
    private double lon = -122.06;
    private double lat = 36.99;
    private Context mContext;
    private LocationManager lm;
    private Location curLocation;
    private TextView textView;
    private DrawerLayout drawerLayout;
    private ListView functionList;
    private RestaurantListFragment listFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        listView = (ListView) view.findViewById(R.id.restaurant_list);
        listFragment = this;
        // Set a listener to ListView.
        mfragment = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant r = (Restaurant) listView.getItemAtPosition(position);
                // Prepare all the data we need to start map activity.
                Bundle bundle = new Bundle();
                bundle.putParcelable(RestaurantMapActivity.EXTRA_LATLNG, new LatLng(r.getLat(), r.getLng()));
                Intent intent = new Intent(view.getContext(), RestaurantMapActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        textView = (TextView) view.findViewById(R.id.labelName);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerView);
        functionList = (ListView) getActivity().findViewById(R.id.functionList);
        functionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        updateRestaurant(search + Config.user_name + "&lon=" + lon + "&lat=" + lat);
                        textView.setText("Search Nearby");
                        break;
                    case 1:
                        updateRestaurant(history + Config.user_name);
                        textView.setText("My Favorites");
                        break;
                    case 2:
                        updateRestaurant(recommendation + Config.user_name);
                        textView.setText("Recommendation");
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(functionList);
            }
        });
        mContext = getActivity();
        getLocation();
        dataService = new DataService(mContext);
//        refreshRestaurantList(dataService);
        updateRestaurant(recommendation + Config.user_name);
        return view;

    }

    public void updateRestaurant(String url) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                new GetRestaurantsNearbyAsyncTask(mfragment, response, dataService).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", Config.cookies);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest2);
    }

    public void addDeleteJson(String businessId, boolean isVisited) {
        /*..
        Use OwnHttpClientStack to let delete carry body
         */
        String userAgent = "volley/0";
        try {
            String packageName = getContext().getPackageName();
            PackageInfo info = getContext().getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        HttpStack httpStack = new OwnHttpClientStack(AndroidHttpClient.newInstance(userAgent));
        RequestQueue queue = Volley.newRequestQueue(mContext, httpStack);
        String url = "http://fengdemeng.mooo.com:8080/Dashi/history";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id", Config.user_name);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(businessId);
            jsonObject.put("visited", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int method = isVisited ? Request.Method.DELETE : Request.Method.POST;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("status");
                    if (result.equals("OK")) {
                        Toast.makeText(mfragment.getActivity(), "Success", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(mfragment.getActivity(), result, Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mfragment.getActivity(), error.toString(), Toast.LENGTH_LONG);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", Config.cookies);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonRequest);
    }

    public void addDeleteFavorite(final String businessId, boolean isVisited) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "http://fengdemeng.mooo.com:8080/Dashi/history";
        int method = isVisited ? Request.Method.DELETE : Request.Method.POST;
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("status");
                    if (result.equals("OK")) {
                        Toast.makeText(mfragment.getActivity(), "Success", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(mfragment.getActivity(), result, Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mfragment.getActivity(), error.toString(), Toast.LENGTH_LONG);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                String body = null;
                Log.e("life", "get body");
                try {
                    jsonObject.put("user_id", Config.user_name);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(businessId);
                    jsonObject.put("visited", jsonArray);
                    body = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    return body.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", Config.cookies);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }


    private String[] getRestaurantNames() {
        String[] names = {
                "Restaurant1", "Restaurant2", "Restaurant3",
                "Restaurant4", "Restaurant5", "Restaurant6",
                "Restaurant7", "Restaurant8", "Restaurant9",
                "Restaurant10"};
        return names;
    }

    OnItemSelectListener mCallback;

    public void onItemSelected(int position) {
    }


    // Container Activity must implement this interface
    public interface OnItemSelectListener {
        public void onItemSelected(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnItemSelectListener) context;
        } catch (ClassCastException e) {
            //do something
        }
    }

/*    private void refreshRestaurantList(DataService dataService) {
        new GetRestaurantsNearbyAsyncTask(this, dataService).execute();
    }*/


    //create AsyncTask background thread task
    private class GetRestaurantsNearbyAsyncTask extends AsyncTask<Void, Void, List<Restaurant>> {
        private Fragment fragment;
        private DataService dataService;
        private Clock clock;
        private String response;
        private List<Restaurant> restaurantList;

        public GetRestaurantsNearbyAsyncTask(Fragment fragment, String response, DataService dataService) {
            this.fragment = fragment;
            this.dataService = dataService;
            this.response = response;
            restaurantList = new ArrayList<Restaurant>();
            this.clock = new Clock();
            this.clock.reset();
        }


        @Override
        protected List<Restaurant> doInBackground(Void... params) {
            clock.start();

            try {
                JSONArray reader = new JSONArray(response);
                for (int i = 0; i < reader.length(); i++) {
                    JSONObject item = reader.getJSONObject(i);
                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(item.getString("name"));
                    restaurant.setAddress(item.getString("full_address"));
                    restaurant.setLat(item.getDouble("latitude"));
                    restaurant.setLng(item.getDouble("longitude"));
                    restaurant.setStars(item.getDouble("stars"));
                    restaurant.setThumbnail(dataService.getBitmapFromURL(item.getString("image_url")));


                    JSONArray category = item.getJSONArray("categories");
                    List<String> cat = new ArrayList<String>();
                    for (int j = 0; j < category.length(); j++) {
                        cat.add(category.get(j).toString());
                    }
                    restaurant.setCategories(cat);
                    restaurant.setVisited(item.getBoolean("is_visited"));
                    restaurant.setBusinessId(item.getString("business_id"));
                    restaurantList.add(restaurant);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return restaurantList;
        }


        @Override
        protected void onPostExecute(List<Restaurant> restaurants) {
            // Measure the latency of the API call.
            clock.stop();
            Log.e("Latency", Long.toString(clock.getCurrentInterval()));
            if (restaurants != null) {
                super.onPostExecute(restaurants);
                RestaurantAdapter adapter = new RestaurantAdapter(fragment.getActivity(), restaurants, listFragment);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(fragment.getActivity(), "Data service error.", Toast.LENGTH_LONG);
            }
        }
    }

    private void getLocation() {
        if (lm == null) {
            lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        boolean isValid = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("Life", Boolean.toString(isValid));
        curLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (curLocation != null) {
            lat = curLocation.getLatitude();
            lon = curLocation.getLongitude();
        }
        Log.e("Life", Double.toString(lat));
        Log.e("Life", Double.toString(lon));
    }

/*    public void onItemSelected(int position){
        for(int i = 0; i < listView.getChildCount(); i++){
            if(position == i){
                listView.getChildAt(i).setBackgroundColor(Color.BLUE);
            }else
                listView.getChildAt(i).setBackgroundColor(Color.parseColor("#EEEEEE"));
        }
    }*/
}
