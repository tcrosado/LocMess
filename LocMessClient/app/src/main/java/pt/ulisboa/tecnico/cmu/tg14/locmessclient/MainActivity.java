package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Location;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Listeners.OnLocationReceivedListener;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.BluetoothReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.GPSReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Receivers.WifiReceiver;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.BluetoothService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.GPSService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.MasterService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.WifiService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServerActions;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.ServiceManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ListMessages.OnFragmentInteractionListener,
        MyMessagesFragment.OnFragmentInteractionListener,ListLocations.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = "Main Activity" ;
    private ListView mDrawerList;
    private FloatingActionButton mFab;


    private ServiceManager mServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceManager = ServiceManager.getInstance(this); //This starts all the needed Services

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Activity activity = this;

        mDrawerList = (ListView) findViewById(R.id.nav_options);
        //mDrawerList.setItemChecked(0,true);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity,AddMessage.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, new ListMessages());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


/*
       List<String> ble = new ArrayList<>();
        ble.add("34:4D:F7:D7:DF:E7");
*/
        List<String> ssid = new ArrayList<>();
        ssid.add("6c:99:89:b1:26:00");

        LocationQuery locationQuery = new LocationQuery(new Float(38.737888),new Float(-9.303022),ssid,null);
        ServerActions serverActions = new ServerActions(getApplicationContext());
        List<Location> resultl = serverActions.getNearLocations(locationQuery);
        for(Location loc : resultl)
            Log.d(TAG, "onCreate: "+loc.getName());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        if (id == R.id.nav_messages) {
            // Handle the camera action
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new ListMessages());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("Menu","msg");

        } else if (id == R.id.nav_my_messages) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MyMessagesFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("Menu","Mymsg");

        } else if (id == R.id.nav_location) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new ListLocations());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("Menu","location");
        } else if (id == R.id.nav_profile) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new ProfileFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("Menu","profile");

        } else if (id == R.id.nav_log_out) {
            Log.d("Menu","Log");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("URI",uri.toString());
    }

}
