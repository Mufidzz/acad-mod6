package com.example.m6;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateData extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_update_data);

        Intent intent = getIntent();
        final String mhsId = intent.getStringExtra(DaftarMahasiswa.MHS_ID);
        String mhsName = intent.getStringExtra(DaftarMahasiswa.MHS_NM);
        String mhsMajor = intent.getStringExtra(DaftarMahasiswa.MHS_MJ);
        String mhsEmail = intent.getStringExtra(DaftarMahasiswa.MHS_EM);

        TextInputEditText tbID = findViewById(R.id.tbUID);
        TextInputEditText tbName = findViewById(R.id.tbUNama);
        TextInputEditText tbJurusan = findViewById(R.id.tbUJurusan);
        TextInputEditText tbEmail = findViewById(R.id.tbUEmail);

        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);

        tbID.setText(mhsId);
        tbName.setText(mhsName);
        tbJurusan.setText(mhsMajor);
        tbEmail.setText(mhsEmail);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.6:1111/" + mhsId;
                RequestQueue rQ = Volley.newRequestQueue(UpdateData.this);

                final TextInputEditText Name = findViewById(R.id.tbUNama);
                final TextInputEditText Major = findViewById(R.id.tbUJurusan);
                final TextInputEditText Email = findViewById(R.id.tbUEmail);

                StringRequest objectRequest = new StringRequest(
                        Request.Method.PUT,
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
                    public byte[] getBody() {
                        Map<String, String> json = new HashMap<>();
                        json.put("FullName", Objects.requireNonNull(Name.getText()).toString());
                        json.put("Major", Objects.requireNonNull(Major.getText()).toString());
                        json.put("Email", Objects.requireNonNull(Email.getText()).toString());

                        JSONObject rJson = new JSONObject(json);
                        String readyJson = rJson.toString();
//                        System.out.println(readyJsondsffds);
                        return readyJson.getBytes();
                    }
                };
                rQ.add(objectRequest);
                Name.setText("");
                Major.setText("");
                Email.setText("");
                Name.requestFocus();
                Toast.makeText(getApplicationContext(),
                        "Update Completed!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UpdateData.this, Dashboard.class);
                startActivity(intent);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
