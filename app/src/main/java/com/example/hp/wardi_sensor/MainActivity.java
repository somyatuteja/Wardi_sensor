package com.example.hp.wardi_sensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
 boolean flag=false;
    private SensorManager senSensorManager;
    private SensorManager senSensorManager2;
    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private ImageView mImageView;
    float x, y,z;
    FileWriter fos;
    FileWriter fos2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     Button mStartButton=(Button)findViewById(R.id.startbutton);
        Button mStopButton=(Button)findViewById(R.id.stopbutton);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senSensorManager2=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGyroscope=senSensorManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager2.registerListener(this,senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        String state = Environment.getExternalStorageState();
        mImageView=(ImageView)findViewById(R.id.imageView);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            try {
                Log.v("Merged", "Media mounted");
                File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                Log.v("Merged", "ExternalStoragePublicDirectory");
                File file = new File(f, "acc.csv");
               File file2=new File(f,"gyro.csv");
                Log.v("Merged", "created");
                 fos = null;
                try {
                    fos = new FileWriter(file);
                    fos2=new FileWriter(file2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
                Log.v("READINGS","In flag");
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fos.close();
                    fos2.close();
     flag=false;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            if(x>8||x<-8)
            {
                mImageView.setImageResource(R.mipmap.x);
            }
            else
            if(y>8||y<-8)
            {
                mImageView.setImageResource(R.mipmap.y);
            }
            else
            if(z>8||z<-8)
            {
                mImageView.setImageResource(R.mipmap.z);
            }
            long curTime = System.currentTimeMillis();
            if (flag) {
      try {
                            fos.write(curTime+","+x + "," + y + "," + z + "\n");

                        } catch (Exception e)  {
                            e.printStackTrace();
                        }
                    Log.v("READINGS", "x:" + x + " y:" + y + " z:" + z);
                }

            }
        if(mySensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            long curTime = System.currentTimeMillis();
            if (flag) {
                TextView mReadinView = (TextView) findViewById(R.id.textView);

                mReadinView.setText(x + "," + y + "," + z + "\n");
                try {
                    fos2.write(curTime+","+x + "," + y + "," + z + "\n");

                } catch (Exception e)  {
                    e.printStackTrace();
                }
                Log.v("READINGS", "x:" + x + " y:" + y + " z:" + z);
            }

        }
        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
