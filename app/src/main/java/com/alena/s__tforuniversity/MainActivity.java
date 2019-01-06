package com.alena.s__tforuniversity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alena.s__tforuniversity.GitHub.GitHubFragment;
import com.alena.s__tforuniversity.GitHub.RepositoriesFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GitHubFragment.OnFragmentInteractionListener {

    private DrawerLayout Drawer;
    private Toolbar toolbar;
    private NavigationView NaviView;

    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private static boolean READ_CONTACTS_GRANTED = false;
    private ArrayList<String> repos = new ArrayList<>();
    private Fragment gitHub, was;
    private boolean isRepos = false;
    private RepositoriesFragment ReposFrag;
    private TextView login_view;

    MenuItem mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Drawer.addDrawerListener(toggle);
        toggle.syncState();

        NaviView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(NaviView);

        mi = NaviView.getMenu().findItem(R.id.nav_github);
        Fragment fragment = null;
        try {
            fragment = (Fragment) GitHubFragment.class.newInstance();
            gitHub = fragment;
            was = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent, gitHub).commit();
        //mi.setChecked(true);
        setTitle(mi.getTitle());

        View header = NaviView.getHeaderView(0);
        login_view = (TextView) header.findViewById(R.id.header_login);
        login_view.setText("");
    }

    @Override
    public void loadLogin(String login) {
        login_view.setText(login);
    }

    @Override
    public void loadRepos(ArrayList<String> reps) {
        repos = reps;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Создать новый фрагмент и задать фрагмент для отображения
        // на основе нажатия на элемент навигации
        Fragment fragment = null;
        Class fragmentClass;


        switch(menuItem.getItemId()) {
            case R.id.nav_github:
                fragmentClass = GitHubFragment.class;
                break;
            case R.id.nav_reposits: {
                fragmentClass = RepositoriesFragment.class;
                break;
            }
            case R.id.nav_info:
                fragmentClass = InfoFragment.class;
                break;
            case R.id.nav_map:
                fragmentClass = MapFragment.class;
                break;
            case R.id.nav_contacts:
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_sensor:
                fragmentClass = SensorFragment.class;
                break;
            default:
                fragmentClass = GitHubFragment.class;
        }


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вставить фрагмент, заменяя любой существующий
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (was == gitHub || was == ReposFrag) {
            fTrans.hide(was);
        }
        else {
            fTrans.remove(was);
        }
        if (fragmentClass==GitHubFragment.class) {
            fTrans.show(gitHub);
            was = gitHub;
        } else if (fragmentClass==RepositoriesFragment.class) {
            if (isRepos) {
                ReposFrag.getRepos(repos, getApplicationContext());
                fTrans.show(ReposFrag);
                was = ReposFrag;
            } else {
                isRepos=true;
                ReposFrag = new RepositoriesFragment();
                ReposFrag.getRepos(repos, getApplicationContext());
                fTrans.add(R.id.flContent, ReposFrag);
                was = ReposFrag;
            }
        } else {
            fTrans.add(R.id.flContent, fragment);
            was = fragment;
        }
        fTrans.commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        Drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
