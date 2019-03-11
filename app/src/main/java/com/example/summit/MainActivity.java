package com.example.summit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    //private EditText textValue;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        statusMessage = (TextView)findViewById(R.id.status_message);
        //    textValue = (EditText) findViewById(R.id.text_value);

        //  autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        // useFlash = (CompoundButton) findViewById(R.id.use_flash);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OcrCaptureActivity.class);
                // intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
                //intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // String text = new String();
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(RetroInterface.ENDPOINT)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    RetroInterface retif = retrofit.create(RetroInterface.class);


                        User u = new User("ABC","Software-Engineer");


                        //JSONObject paramObject = new JSONObject();
                        //paramObject.put("email", "sample@gmail.com");
                        //paramObject.put("pass", "4384984938943");

                       Call<String> userCall = retif.getUser();
                        userCall.enqueue(new Callback<String>() {


                                             @Override
                                             public void onResponse(Call<String> call, Response<String> response) {
                                               //  Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                                                 Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                 int c = response.code();
                                                 String us = (String) response.body();
                                                 if(us== null){
                                                     Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                                     Toast.makeText(MainActivity.this, String.valueOf(c), Toast.LENGTH_SHORT).show();
                                                 }
                                                 if (response.isSuccessful() && us != null) {
                                                     Toast.makeText(MainActivity.this, us, Toast.LENGTH_SHORT).show();
                                                 }
                                             }

                                             @Override
                                             public void onFailure(Call<String> call, Throwable t) {

                                             }
                                         });

                            //Call<User> userCall = retif.createUser(text);
                    //userCall.enqueue(this);
                    String file = "mfile.txt";
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(file, Context.MODE_PRIVATE));
                        outputStreamWriter.write(text);
                        Toast.makeText(this, "File write successful", Toast.LENGTH_LONG).show();
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }

                    String ret = "";

                    try {
                        InputStream inputStream = getApplicationContext().openFileInput(file);

                        if (inputStream != null) {
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String receiveString = "";
                            StringBuilder stringBuilder = new StringBuilder();

                            while ((receiveString = bufferedReader.readLine()) != null) {
                                stringBuilder.append(receiveString);
                            }

                            inputStream.close();
                            ret = stringBuilder.toString();
                            Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();
                            JSONArray arr = new JSONArray();
                            arr.put(ret);

                            Toast.makeText(this, "JSON:" + arr.toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(this, "JSON-OBJ:"+obj.toString(), Toast.LENGTH_LONG).show();
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("login activity", "File not found: " + e.toString());
                    } catch (IOException e) {
                        Log.e("login activity", "Can not read file: " + e.toString());
                    }
                    statusMessage.setText(R.string.ocr_success);

                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


/*
    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        User resusr = (User)response.body();
        int c = response.code();
        if(resusr == null){
            Toast.makeText(this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, String.valueOf(c), Toast.LENGTH_SHORT).show();
        }
        if (response.isSuccessful() && resusr != null) {
            Toast.makeText(this, resusr.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }
*/

    //@Override
   /* public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
       //     return true;
        //}

        //return super.onOptionsItemSelected(item);
    }*/
}
