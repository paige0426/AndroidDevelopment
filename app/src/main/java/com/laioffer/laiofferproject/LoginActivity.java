package com.laioffer.laiofferproject;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button button;
    private LinearLayout linearLayout;
    private float origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        button = (Button) findViewById(R.id.submit);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        origin = linearLayout.getTranslationY();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                linearLayout.animate().setDuration(500).translationY(origin);
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long l) {
                    }
                    @Override
                    public void onFinish() {
                        LoginAuthentification();
                    }
                }.start();
            }
        });
    }

    /**
     * login request
     */
    public void LoginAuthentification() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String user_name = editTextLogin.getText().toString();
        String user_password = editTextPassword.getText().toString();
        String password = md5(user_name + md5(user_password));


        //backend url we want to connect
        String url = "http://fengdemeng.mooo.com:8080/Dashi/LoginServlet?user_id=" + user_name + "&password=" + password;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //get JSON response and parse the response
                    JSONObject reader = new JSONObject(response);
                    String result = reader.optString("status").toString();
                    Log.i("Login", result);
                    if (result.equals("OK")) {
                        //save the information
                        Config.user_name = user_name;
                        Intent intent = new Intent(getBaseContext(), RestaurantListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getBaseContext(), "Please reenter your account and password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(getBaseContext(), "Please reenter your account and password", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                linearLayout.animate().setDuration(500).translationY(0);
                Toast.makeText(getBaseContext(), "Please reenter your account and password", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Response<String> superResponse = super.parseNetworkResponse(response);
                //we have to get cookies from response headers
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                Config.cookies = rawCookies.substring(0, rawCookies.indexOf(";"));
                return superResponse;
            }
        };
        queue.add(stringRequest);
    }

    private String md5(String input) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));
            byte[] resultByte = messageDigest.digest();
            result = new String(Hex.encodeHex(resultByte));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Life cycle test", "We are at onStart()");
        linearLayout.animate().setDuration(500).translationY(0);
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

}
