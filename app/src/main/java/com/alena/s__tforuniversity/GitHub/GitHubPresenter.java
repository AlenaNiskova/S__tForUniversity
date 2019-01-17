package com.alena.s__tforuniversity.GitHub;

import java.util.ArrayList;

public class GitHubPresenter {
    private boolean need_sec_step, isCrashed = true;
    private boolean sign_in = false;
    private GitHubFragment fragment;

    GitHubPresenter(GitHubFragment gitHubFragment) {
        this.fragment = gitHubFragment;
    }

    public void onButtonClick() {
        if (!sign_in) {
            if (!need_sec_step) {
                onSignInClick();
            }
            else {
                onSecStepClick();
            }
        } else {
            onSignOutClick();
            sign_in = false;
            fragment.getRepos(new ArrayList<String>());
            setLogin("");
        }
    }

    public void loadRepos(ArrayList<String> repos) {
        fragment.getRepos(repos);
    }

    public void onSignInClick() {
        GitHubModel model = new GitHubModel(this);
        model.connect();
    }

    public void setErrors(boolean b) {
        this.isCrashed = b;
        if (!need_sec_step) {
            if (!isCrashed) {
                fragment.isSignedIn();
                sign_in = true;
            }
        } else {
            fragment.isNeedSecStep();
        }
    }

    public void onSecStepClick() {
        GitHubModel model = new GitHubModel(this);
        model.connect();
    }

    public void onSignOutClick() {
        fragment.isSignedOut();
    }

    public String getSecAuth() {
        return fragment.getSecAuth();
    }

    public String getLogin() {
        return fragment.getLogin();
    }

    public String getPass() {
        return fragment.getPass();
    }

    public void isSecFactor(boolean b) {
        if (need_sec_step) {
            need_sec_step = false;
        } else {
            need_sec_step = b;
        }
        fragment.isSecFactor(b);
    }

    public void setLogin(String login) {
        fragment.setLogin(login);
    }

}
