package com.alena.s__tforuniversity.GitHub;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GitHubModel {

    private static GitHubPresenter presenter;
    private static boolean twoFactorAuth = false;

    GitHubModel(GitHubPresenter presenter) {
        GitHubModel.presenter = presenter;
    }

    void connect() {
        String encod = Base64.encodeToString((presenter.getLogin() + ":" + presenter.getPass()).getBytes(), Base64.DEFAULT);
        AuthAsyncTask authAsyncTask = new AuthAsyncTask();
        authAsyncTask.execute(encod, presenter.getSecAuth());
    }

    public static class AuthAsyncTask extends AsyncTask<String, Void, Void> {
        private ArrayList<String> repos = new ArrayList<>();
        private boolean isCrashed = false;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCrashed || twoFactorAuth) {
                String login = presenter.getLogin();
                String pass = presenter.getPass();
                presenter.isSecFactor(twoFactorAuth);
                presenter.setLogin(login);
                presenter.loadRepos(repos);
                presenter.setErrors(isCrashed);
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            isCrashed = false;
            try {
                URL location = new URL("https://api.github.com/user/repos");
                connection = (HttpURLConnection) location.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.setRequestProperty("Authorization", "Basic " + strings[0].trim()
                        .replaceAll("\\n", ""));
                connection.setRequestProperty("X-GitHub-OTP", strings[1]);
                InputStream content = null;
                try {
                    content = connection.getInputStream();
                } catch (Exception ex) {
                    content = connection.getErrorStream();
                }
                bufferedReader = new BufferedReader(
                        new InputStreamReader(content));
                String res = bufferedReader.readLine();

                if (res.toLowerCase().contains("two-factor authentication") && twoFactorAuth) {
                    isCrashed = true;
                } else if (res.toLowerCase().contains("two-factor authentication")) {
                    twoFactorAuth = true;
                } else {
                    JSONArray jsonArray = new JSONArray(res);
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        repos.add(jsonObject.getString("name"));
                    }
                }
            } catch (Exception e) {
                isCrashed = true;
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }
    }
}
