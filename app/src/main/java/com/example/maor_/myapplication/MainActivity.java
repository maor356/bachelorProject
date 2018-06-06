package com.example.maor_.myapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.view.View;
import android.widget.ToggleButton;

import interdroid.swancore.swanmain.ExpressionManager;
import interdroid.swancore.swanmain.SwanException;
import interdroid.swancore.swanmain.ValueExpressionListener;
import interdroid.swancore.swansong.ExpressionFactory;
import interdroid.swancore.swansong.ExpressionParseException;
import interdroid.swancore.swansong.TimestampedValue;
import interdroid.swancore.swansong.ValueExpression;




public class MainActivity extends AppCompatActivity {
    private static int REQUEST_CODE_WALKING_RUNNING = 100;
    private static int REQUEST_CODE_GYRO = 101;
    private static int REQUEST_CODE_PROX = 102;
    private static int REQUEST_CODE_LIGHT = 103;
    private static final String TAG = "MainActivity";
    final String expression="self@movement:meters?sensitivity=2.96#step_coeficient=77";
    final String expressionGyro="self@gyroscope:x{ANY, 0}";
    final String expressionProx="self@proximity:distance{ANY, 0}";
    final String expressionLight="self@light:lux{MAX, 1000}";
    Button btnStop;
    Button btnStart;
    ToggleButton toggleAcc;
    ToggleButton toggleGyro;
    ToggleButton toggleLight;
    ToggleButton toggleProx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

       //StrictMode.setThreadPolicy(policy);
        btnStart=findViewById(R.id.btnStart);
        btnStop=findViewById(R.id.btnStop);
        toggleAcc = (ToggleButton) findViewById(R.id.toggleAcc);
        toggleLight = (ToggleButton) findViewById(R.id.toggleLight);
        toggleProx = (ToggleButton) findViewById(R.id.toggleProx);
        toggleGyro = (ToggleButton) findViewById(R.id.toggleGyro);
        unregisterSWANSensor();


        /*btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG,"BTN START");
                registerSWANSensorWalkRunStep(expression, REQUEST_CODE_WALKING_RUNNING);
            }
        });*/


        /*btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG,"BTN STOP");

                unregisterSWANSensor();
            }
        });*/

        toggleAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG,"ACC is ON");
                    registerSWANSensorWalkRunStep(expression, REQUEST_CODE_WALKING_RUNNING);
                } else {
                    Log.i(TAG,"ACC is OFF");
                    unregisterSWANSensor();
                }
            }
        });


        toggleLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG,"LIGHT is ON");
                    registerSWANSensorWalkRunStep(expressionLight, REQUEST_CODE_LIGHT);
                } else {
                    Log.i(TAG,"LIGHT is OFF");
                    unregisterSWANSensor();
                }
            }
        });

        toggleGyro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG,"GYRO is ON");
                    registerSWANSensorWalkRunStep(expressionGyro, REQUEST_CODE_GYRO);
                } else {
                    Log.i(TAG,"GYRO is OFF");
                    unregisterSWANSensor();
                }
            }
        });


        toggleProx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG,"PROX is ON");
                    registerSWANSensorWalkRunStep(expressionProx, REQUEST_CODE_PROX);
                } else {
                    Log.i(TAG,"PROX is OFF");
                    unregisterSWANSensor();
                }
            }
        });

    }



    private void registerSWANSensorWalkRunStep(String myExpression, int requestCode){


        //TODO: Check if you need to add this as a parameter in the method, for now the mLimit value would remain 10
//        public void setSensitivity(float sensitivity) {
//            mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
//        }

        try {
            Log.i(TAG,"in registerSWANSensorWalkRunStep");
            ValueExpression ve=(ValueExpression) ExpressionFactory.parse(myExpression);
            ExpressionManager.registerValueExpression(this, String.valueOf(requestCode),ve
                    ,
                    new ValueExpressionListener() {

                        /* Registering a listener to process new values from the registered sensor*/
                        @Override
                        public void onNewValues(String id, TimestampedValue[] arg1) {
                            Log.i(TAG,"in onNewValues");
                            if (arg1 != null && arg1.length > 0) {

                                String value = arg1[0].getValue().toString();

                                Toast.makeText(MainActivity.this,"Coachee value= "+value,Toast.LENGTH_SHORT).show();



                                Log.i(TAG,"SWAN [IF] onNewValues: setting meters=" + String.valueOf(value));


                            } else {
                                Log.i(TAG,"SWAN [ELSE] onNewValues: setting meters=0");


                            }

                        }
                    });
        } catch (SwanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExpressionParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void unregisterSWANSensor(){
        ExpressionManager.unregisterExpression(this, String.valueOf(REQUEST_CODE_WALKING_RUNNING));
    }

}
