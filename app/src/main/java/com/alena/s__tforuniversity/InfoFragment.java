package com.alena.s__tforuniversity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class InfoFragment extends Fragment {


    public InfoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        ListView lv = (ListView) v.findViewById(R.id.info_lv);

        ArrayList<String> info = new ArrayList<String>();
        info.add(Build.MODEL);
        info.add(Integer.toString(Build.VERSION.SDK_INT));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, info);

        lv.setAdapter(adapter);

        return v;
    }

}