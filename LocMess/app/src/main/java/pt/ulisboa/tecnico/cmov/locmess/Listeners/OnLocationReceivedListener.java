package pt.ulisboa.tecnico.cmov.locmess.Listeners;

import java.util.List;

/**
 * Created by tiago on 25/03/2017.
 */

public interface OnLocationReceivedListener {
     void onGPSReceived(double lat,double lon);
     void onWifiReceived(String ssid);
     void onBleReceived(String ble);
}
