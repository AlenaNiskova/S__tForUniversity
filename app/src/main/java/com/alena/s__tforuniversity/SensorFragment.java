package com.alena.s__tforuniversity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alena.s__tforuniversity.GitHub.GitHubFragment;

import java.util.ArrayList;

public class SensorFragment extends Fragment {

    SensorManager sm;
    Sensor accelero;
    float[] values = new float[3];

    TextView s_x, s_y, s_z;
    ImageView image;
    Button make, save;

    public SensorFragment() {
    }

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void checkPermission();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sensor, container, false);

        s_x = (TextView) v.findViewById(R.id.sensor_x);
        s_y = (TextView) v.findViewById(R.id.sensor_y);
        s_z = (TextView) v.findViewById(R.id.sensor_z);
        make = (Button) v.findViewById(R.id.make_photo);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.checkPermission();
            }
        });
        save = (Button) v.findViewById(R.id.save_photo);
        image = (ImageView) v.findViewById(R.id.photo);

        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelero = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return v;
    }

    public void onMakeClick() {

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
