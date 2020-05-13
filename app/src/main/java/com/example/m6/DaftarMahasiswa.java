package com.example.m6;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DaftarMahasiswa extends AppCompatActivity {
    public static final String MHS_ID = "com.fz.app.pmod6.transfer_mhs_id";
    public static final String MHS_NM = "com.fz.app.pmod6.transfer_mhs_name";
    public static final String MHS_MJ = "com.fz.app.pmod6.transfer_mhs_major";
    public static final String MHS_EM = "com.fz.app.pmod6.transfer_mhs_email";


    ListView lvMahasiswa;
    private String TAG = DaftarMahasiswa.class.getSimpleName();
    ArrayList<HashMap<String, String>> mahasiswaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_daftar_mahasiswa);
        new GetMahasiswa().execute();
            }

    class MyAdapter extends ArrayAdapter<HashMap<String, String>> {
        Context context;
        ArrayList<HashMap<String, String>> mData;

        MyAdapter (Context c, ArrayList<HashMap<String, String>> data) {
            super(c, R.layout.mahasiswa_tile, R.id.lblNamaMhs, data);
            this.context = c;
            this.mData = data;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.mahasiswa_tile, parent, false);
            TextView myTitle = row.findViewById(R.id.lblNamaMhs);
            myTitle.setText(mData.get(position).get("id") + " " + mData.get(position).get("name"));
            return row;
        }
    }

    public class GetMahasiswa extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(DaftarMahasiswa.this, "JSON Data is Downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String url = "http://192.168.1.6:1111/";
            String jsonStr = sh.makeServiceCall(url);

            if(jsonStr != null) {
                try {
                    JSONObject mahasiswaData = new JSONObject(jsonStr);
                    JSONArray mahasiswaArray = mahasiswaData.getJSONArray("data");

                    for(int i = 0 ; i < mahasiswaArray.length() ; i++){
                        JSONObject m = mahasiswaArray.getJSONObject(i);
                        HashMap<String, String> tempMahasiswa = new HashMap<>();
                        tempMahasiswa.put("id", m.getString("ID"));
                        tempMahasiswa.put("name", m.getString("FullName"));
                        tempMahasiswa.put("major", m.getString("Major"));
                        tempMahasiswa.put("email", m.getString("Email"));

                        mahasiswaList.add(tempMahasiswa);
                    }

                } catch (final Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lvMahasiswa = findViewById(R.id.lvMahasiswa);
            MyAdapter adapter = new MyAdapter(DaftarMahasiswa.this, mahasiswaList);
            lvMahasiswa.setAdapter(adapter);
            lvMahasiswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(DaftarMahasiswa.this, mahasiswaList.get(Long.valueOf(id).intValue()).get("id"), Toast.LENGTH_SHORT).show();
                    String mhsId = mahasiswaList.get(Long.valueOf(id).intValue()).get("id");
                    String mhsName = mahasiswaList.get(Long.valueOf(id).intValue()).get("name");
                    String mhsMajor = mahasiswaList.get(Long.valueOf(id).intValue()).get("major");
                    String mhsEmail = mahasiswaList.get(Long.valueOf(id).intValue()).get("email");

                    Intent intent = new Intent(DaftarMahasiswa.this, UpdateData.class);

                    intent.putExtra(MHS_ID, mhsId);
                    intent.putExtra(MHS_NM, mhsName);
                    intent.putExtra(MHS_MJ, mhsMajor);
                    intent.putExtra(MHS_EM, mhsEmail);

                    startActivity(intent);
                }
            });
        }
    }
}

