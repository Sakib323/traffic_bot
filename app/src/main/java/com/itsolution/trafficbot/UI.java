package com.itsolution.trafficbot;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class UI extends AppCompatActivity {
    ExtendedFloatingActionButton start;
    CardView stay,ip_type,click_coordinate,interval_time,cycle_time;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        start=findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UI.this,MainActivity.class);
                startActivity(intent);
            }
        });

        
            if(ContextCompat.checkSelfPermission(UI.this, Settings.ACTION_ACCESSIBILITY_SETTINGS) != PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }



        interval_time=findViewById(R.id.interval_time);
        interval_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog=new Dialog(UI.this);
                dialog.setContentView(R.layout.add_car);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                TextView txt=dialog.findViewById(R.id.text);
                txt.setText("Set a interval time.10 second is set as default");
                dialog.show();
                EditText nmbr=dialog.findViewById(R.id.set_nmbr);

                CardView btn=dialog.findViewById(R.id.next_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data=nmbr.getText().toString();
                        if(data!=null){
                            SharedPreferences settings=getSharedPreferences("settings",MODE_PRIVATE);
                            SharedPreferences.Editor editor=settings.edit();
                            editor.putInt("interval_time",Integer.valueOf(data));
                            editor.apply();
                            dialog.dismiss();
                            Toast.makeText(UI.this, "You have saved interval time to "+data, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UI.this, "Set a value!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        cycle_time=findViewById(R.id.cycle_time);
        cycle_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog=new Dialog(UI.this);
                dialog.setContentView(R.layout.add_car);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                TextView txt=dialog.findViewById(R.id.text);
                txt.setText("How much time you want to run the bot.right now it's infinite");
                dialog.show();
                EditText nmbr=dialog.findViewById(R.id.set_nmbr);

                CardView btn=dialog.findViewById(R.id.next_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data=nmbr.getText().toString();
                        if(data!=null){
                            SharedPreferences settings=getSharedPreferences("settings",MODE_PRIVATE);
                            SharedPreferences.Editor editor=settings.edit();
                            editor.putInt("cycle_time",Integer.valueOf(data));
                            editor.apply();
                            dialog.dismiss();
                            Toast.makeText(UI.this, "You have saved cycle time to "+data, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UI.this, "Set a value!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        stay=findViewById(R.id.stay_time);
        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog=new Dialog(UI.this);
                dialog.setContentView(R.layout.add_car);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                TextView txt=dialog.findViewById(R.id.text);
                txt.setText("How much minutes you want to stay in a website.Minimum 1 minutes");
                dialog.show();
                EditText nmbr=dialog.findViewById(R.id.set_nmbr);

                CardView btn=dialog.findViewById(R.id.next_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data=nmbr.getText().toString();
                        if(data!=null){
                            SharedPreferences settings=getSharedPreferences("settings",MODE_PRIVATE);
                            SharedPreferences.Editor editor=settings.edit();
                            editor.putInt("sleep_time",Integer.valueOf(data));
                            editor.apply();
                            dialog.dismiss();
                            Toast.makeText(UI.this, "You have saved sleep time to "+data, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UI.this, "Set a value!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        click_coordinate=findViewById(R.id.click_coordinates);
        click_coordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog=new Dialog(UI.this);
                dialog.setContentView(R.layout.add_car);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                TextView txt=dialog.findViewById(R.id.text);
                txt.setText("X and Y coordinates in screen");
                dialog.show();
                EditText nmbr=dialog.findViewById(R.id.set_nmbr);

                CardView btn=dialog.findViewById(R.id.next_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data=nmbr.getText().toString();
                        if(data!=null){
                            SharedPreferences settings=getSharedPreferences("settings",MODE_PRIVATE);
                            SharedPreferences.Editor editor=settings.edit();
                            editor.putInt("coordinates",Integer.valueOf(data));
                            editor.apply();
                            dialog.dismiss();
                            Toast.makeText(UI.this, "You have saved coordinates to "+data, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UI.this, "Set a value!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });






    }





}