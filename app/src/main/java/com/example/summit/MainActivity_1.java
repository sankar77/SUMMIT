package com.example.summit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity_1 extends AppCompatActivity {
    EditText ed;
    EditText iptext;

    Button sBtn;
    Button ipBtn;

    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        ed = findViewById(R.id.ed1);
        iptext = findViewById(R.id.ip_addr);

        sBtn = findViewById(R.id.submit_btn);
        ipBtn = findViewById(R.id.ip_addr_btn);

        ipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iptext.getText().toString() == ""){
                    return;
                }
                retrofit = new Retrofit.Builder()
                        .baseUrl(iptext.getText().toString())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Toast.makeText(MainActivity_1.this, "Got The Link", Toast.LENGTH_SHORT).show();









            }
        });
        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ed.getText().toString();
                //User u = new User("ABC","Software-Engineer");
                String msg = text.replaceAll("\\.\\s", "\\. \\\n");
//                msg = msg.replaceAll("\"", "\"");
//                Summ1 s = new Summ1(msg,1);
                JSONArray jar = new JSONArray();
                JSONObject jso = new JSONObject();
                JSONObject t = new JSONObject();

                RetroInterface retif = retrofit.create(RetroInterface.class);

                Log.d("TAG",jar.toString());
                try {
                    jso.put("doc",msg);
                    jso.put("id",1);
                    jar.put(jso);
                    t.put("articles",jar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity_1.this, "Got The Doc", Toast.LENGTH_SHORT).show();
                try {
                    Log.d("TAG",t.toString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<String> userCall = retif.getSumm(t.toString());
                userCall.enqueue(new Callback<String>() {


                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //  Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity_1.this, "Success", Toast.LENGTH_SHORT).show();
                        int c = response.code();
                        String us = response.body();
                        if(us== null){
                            Toast.makeText(MainActivity_1.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity_1.this, "Error code:" + String.valueOf(c), Toast.LENGTH_SHORT).show();
                        }
                        if (response.isSuccessful() && us != null) {
//                            Toast.makeText(MainActivity_1.this, us, Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity_1.this).setTitle("Summary").setMessage(us).setPositiveButton("OK", null).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MainActivity_1.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
