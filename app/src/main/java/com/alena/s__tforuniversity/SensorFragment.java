package com.alena.s__tforuniversity;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.FileProvider.getUriForFile;

public class SensorFragment extends Fragment {

    SensorManager sm;
    Sensor accelero;
    float[] values = new float[3];

    TextView s_x, s_y, s_z, photo;
    ImageView image;
    Button make, save;

    public SensorFragment() {
    }

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void checkCamera();
        void checkStorage();
    }

    private static final int CAMERA_REQUEST = 0;
    private Uri selectedPhotoPath = null;
    private Bitmap bitmap;

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
                mListener.checkCamera();
            }
        });
        save = (Button) v.findViewById(R.id.save_photo);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.checkStorage();
            }
        });
        photo = (TextView) v.findViewById(R.id.text_photo);
        image = (ImageView) v.findViewById(R.id.photo);
        image.setImageResource(0);

        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelero = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedPhotoPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            image.setImageURI(selectedPhotoPath);
        }
    }

    public void onMakeClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagePath = new File(getContext().getCacheDir(), "img");
        imagePath.mkdirs();
        File newFile = null;
        photo.setText("");

        try {
            newFile = File.createTempFile("default_image", ".jpg", imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedPhotoPath = getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoPath);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ClipData clip = ClipData.newUri(getContext().getContentResolver(), "A photo", selectedPhotoPath);
            intent.setClipData(clip);
        }
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void onSaveClick() {
        ContentValues values = new ContentValues();
        long time = System.currentTimeMillis();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, time/1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);

        try {
            Uri url = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            OutputStream fOut = getContext().getContentResolver().openOutputStream(url);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (bitmap != null) {
            photo.setText("Фотография сохранена.");
        }
        bitmap = null;
        image.setImageResource(0);
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
