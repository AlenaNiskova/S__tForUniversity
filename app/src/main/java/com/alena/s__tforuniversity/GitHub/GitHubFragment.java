package com.alena.s__tforuniversity.GitHub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alena.s__tforuniversity.R;

import java.util.ArrayList;


public class GitHubFragment extends Fragment {


    public GitHubFragment() {
        // Required empty public constructor
    }

    private Button button;
    private EditText login, password, sec_step;
    private GitHubPresenter gitHubPresenter;
    private LinearLayout first_step;
    public boolean is_sec_step = false;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void loadRepos(ArrayList<String> reps);
        void loadLogin(String login);
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_git_hub, container, false);

        Log.d("MyLog" ,"onCreateViewGit");

        login = (EditText) v.findViewById(R.id.login);
        first_step = (LinearLayout) v.findViewById(R.id.first_step);
        password = (EditText) v.findViewById(R.id.password);
        sec_step = (EditText) v.findViewById(R.id.second_step);
        if (gitHubPresenter == null) {
            gitHubPresenter = new GitHubPresenter(this);
            gitHubPresenter.attachView(this);
        }
        button = (Button) v.findViewById(R.id.github_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gitHubPresenter.onButtonClick();
            }
        });

        return v;
    }

    public void getRepos(ArrayList<String> repos) {
        mListener.loadRepos(repos);
    }

    public void isNeedSecStep() {
        first_step.setVisibility(View.INVISIBLE);
        sec_step.setVisibility(View.VISIBLE);
    }

    public void isSignedIn() {
        button.setText(R.string.action_sign_out);
        first_step.setVisibility(View.INVISIBLE);
        sec_step.setVisibility(View.INVISIBLE);
    }

    public void isSignedOut() {
        button.setText(R.string.action_sign_in);
        first_step.setVisibility(View.VISIBLE);
        login.setText("");
        password.setText("");
        sec_step.setText("");
        sec_step.setVisibility(View.INVISIBLE);
    }

    public void isSecFactor(boolean b) {
        is_sec_step = b;
    }

    public String getPass() {
        try {
            return password.getText().toString();
        } catch (NullPointerException loginNull) {
            return null;
        }
    }

    public String getLogin() {
        try {
            return login.getText().toString();
        } catch (NullPointerException loginNull) {
            return null;
        }
    }

    public String getSecAuth() {
        if (is_sec_step) {
            return sec_step.getText().toString();
        }
        else {
            return " ";
        }
    }

    public void setLogin(String name) {
        if (is_sec_step) {
            sec_step.setText(name);
        }
        else {
            login.setText(name);
        }
        mListener.loadLogin(name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("MyLog", "onDestroyViewGit");
        gitHubPresenter.detachView();
    }
}
