package com.alena.s__tforuniversity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
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
import android.widget.Toast;

import com.alena.s__tforuniversity.GitHub.GitHubFragment;
import com.alena.s__tforuniversity.GitHub.RepositoriesFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GitHubFragment.OnFragmentInteractionListener, SensorFragment.OnFragmentInteractionListener {

    private DrawerLayout Drawer;
    private Toolbar toolbar;
    private NavigationView NaviView;
    private ArrayList<String> repos = new ArrayList<>();
    private Fragment gitHub, was;
    private boolean isRepos = false, isSens = false;
    private RepositoriesFragment ReposFrag;
    private SensorFragment SensFrag;
    private TextView login_view;
    MenuItem mi;

    public static final int REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_CAMERA = 2;

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

    @Override
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            SensFrag.onMakeClick();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
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
        Class fragmentClass;
        mi = menuItem;

        switch(menuItem.getItemId()) {
            case R.id.nav_github: {
                fragmentClass = GitHubFragment.class;
                PermissionGranted(fragmentClass, menuItem);
                break;
            }
            case R.id.nav_reposits: {
                fragmentClass = RepositoriesFragment.class;
                PermissionGranted(fragmentClass, menuItem);
                break;
            }
            case R.id.nav_info: {
                fragmentClass = InfoFragment.class;
                PermissionGranted(fragmentClass, menuItem);
                break;
            }
            case R.id.nav_map: {
                fragmentClass = MapFragment.class;
                PermissionGranted(fragmentClass, menuItem);
                break;
            }
            case R.id.nav_contacts: {
                fragmentClass = ContactsFragment.class;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    PermissionGranted(fragmentClass, menuItem);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_READ_CONTACTS);
                }
                break;
            }
            case R.id.nav_sensor: {
                fragmentClass = SensorFragment.class;
                PermissionGranted(fragmentClass, menuItem);
                break;
            }
            default: {
                fragmentClass = GitHubFragment.class;
                PermissionGranted(fragmentClass, menuItem);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionGranted(ContactsFragment.class, mi);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.contacts_permission, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }
                break;
            }
            case REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SensFrag.onMakeClick();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.camera_permission, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }
                break;
            }
        }
    }

    public void PermissionGranted(Class fragmentClass, MenuItem menuItem) {
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (was == gitHub || was == ReposFrag || was == SensFrag) {
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
        } else if (fragmentClass==SensorFragment.class) {
            if (isSens) {
                fTrans.show(SensFrag);
                was = SensFrag;
            } else {
                isSens=true;
                SensFrag = new SensorFragment();
                fTrans.add(R.id.flContent, SensFrag);
                was = SensFrag;
            }
        } else {
            Fragment fragment = null;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
