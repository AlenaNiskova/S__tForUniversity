package com.alena.s__tforuniversity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorFragment extends Fragment {

    SensorManager sm;
    Sensor accelero;
    TextView s_x, s_y, s_z;
    float[] values = new float[3];

    public SensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sensor, container, false);

        s_x = (TextView) v.findViewById(R.id.sensor_x);
        s_y = (TextView) v.findViewById(R.id.sensor_y);
        s_z = (TextView) v.findViewById(R.id.sensor_z);

        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelero = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(listener, accelero, sm.SENSOR_DELAY_NORMAL);
    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                values = sensorEvent.values;

                s_x.setText("x: " + values[0]);
                s_y.setText("y: " + values[1]);
                s_z.setText("z: " + values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

}
