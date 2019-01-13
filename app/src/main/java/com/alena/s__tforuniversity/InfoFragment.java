package com.alena.s__tforuniversity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoFragment extends Fragment {


    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        ListView lv = (ListView) v.findViewById(R.id.info_lv);

        ArrayList<HashMap<String, String>> infos = new ArrayList<>();
        HashMap<String, String> inf;

        Intent batteryStatus = getContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        String  level = Integer.toString(batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));

        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> info = new ArrayList<String>();
        name.add("Производитель");
        info.add(Build.MANUFACTURER);
        name.add("Модель");
        info.add(Build.MODEL);
        name.add("Версия Android");
        info.add(Build.VERSION.RELEASE);
        name.add("Номер сборки");
        info.add(Build.DISPLAY);
        name.add("Серийный номер");
        info.add(Build.SERIAL);
        name.add("Уровень заряда батареи");
        info.add(level+"%");

        for (int i=0; i<info.size(); i++) {
            inf = new HashMap<>();
            inf.put("Name", name.get(i));
            inf.put("Info", info.get(i));
            infos.add(inf);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), infos, R.layout.contact_item,
                new String[]{"Name", "Info"},
                new int[]{R.id.contact_name, R.id.contact_number});

        lv.setAdapter(adapter);

        return v;
    }
}