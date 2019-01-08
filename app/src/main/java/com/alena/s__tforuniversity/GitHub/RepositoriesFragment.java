package com.alena.s__tforuniversity.GitHub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.alena.s__tforuniversity.R;

import java.util.ArrayList;

public class RepositoriesFragment extends ListFragment {

    public RepositoriesFragment() {
        // Required empty public constructor
    }

    private ArrayList<String> repos = new ArrayList<>();
    String[] data = new String[] {"one", "two", "three", "four"};

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, repos);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MyLog" ,"onCreateViewRepos");
        return inflater.inflate(R.layout.fragment_repositories, null);
    }

    public void getRepos(ArrayList<String> repos, Context context) {
        this.repos = repos;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, repos);
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MyLog" ,"onStartViewRepos");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}