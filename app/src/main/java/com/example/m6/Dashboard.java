package com.example.m6;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Dashboard extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.dashboard);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnList = findViewById(R.id.btnList);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO : Request Post to Server
                String url = "http://192.168.1.6:1111/";
                RequestQueue rQ = Volley.newRequestQueue(Dashboard.this);

                StringRequest objectRequest = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                error.printStackTrace();
                            }
                        }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        Map<String, String>  json = new HashMap<String, String>();
                        TextInputEditText Name = findViewById(R.id.tbNama);
                        TextInputEditText Major = findViewById(R.id.tbJurusan);
                        TextInputEditText Email = findViewById(R.id.tbEmail);

                        json.put("FullName", Objects.requireNonNull(Name.getText()).toString());
                        json.put("Major", Objects.requireNonNull(Major.getText()).toString());
                        json.put("Email", Objects.requireNonNull(Email.getText()).toString());

                        JSONObject rJson = new JSONObject(json);
                        String readyJson = rJson.toString();

                        System.out.println(readyJson);

                        return readyJson.getBytes();
                    }
                };
                TextInputEditText Name = findViewById(R.id.tbNama);
                TextInputEditText Major = findViewById(R.id.tbJurusan);
                TextInputEditText Email = findViewById(R.id.tbEmail);

                rQ.add(objectRequest);
                Name.setText("");
                Major.setText("");
                Email.setText("");
                Name.requestFocus();
                Toast.makeText(getApplicationContext(),
                        "Register Completed!",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Dashboard.this, DaftarMahasiswa.class);
                startActivity(intent);
            }
        });

    }
}
