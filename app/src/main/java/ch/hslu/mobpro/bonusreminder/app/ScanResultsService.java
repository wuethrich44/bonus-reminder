package ch.hslu.mobpro.bonusreminder.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.util.*;

public class ScanResultsService extends Service {

    private Set<String> usedSSIDs;

    @Override
    public void onCreate() {
        usedSSIDs = new HashSet<String>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Set<String> foundSSIDs = findSSIDs();
        cleanUsedSSIDs(foundSSIDs);

        Map<String, String> ssidBonus = getMap();

        for (String foundSSID : foundSSIDs) {
            if (ssidBonus.containsKey(foundSSID) && !usedSSIDs.contains(foundSSID)) {
                String bonus = ssidBonus.get(foundSSID);
                buildBonusNotification(bonus);
                usedSSIDs.add(foundSSID);
            }
        }

        return START_STICKY;
    }

    private Set<String> findSSIDs() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Set<String> foundSSIDs = new HashSet<String>();
        for (ScanResult scanResult : wifiManager.getScanResults()) {
            foundSSIDs.add(scanResult.SSID);
        }
        return foundSSIDs;
    }

    private void cleanUsedSSIDs(Set<String> foundSSIDs) {
        for (String usedSSID : usedSSIDs) {
            if (!foundSSIDs.contains(usedSSID)) {
                usedSSIDs.remove(usedSSID);
            }
        }
    }

    private Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        /*map.put("hslu", "Mensagutschein");
        map.put("UPC0039811", "Irgendein Gutschein");*/

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        Set<String> boni = new HashSet<String>();
        boni = prefs.getStringSet("boni", boni);
        for(String bonus : boni){
            String accessPoint = prefs.getString(bonus, "");
            if(accessPoint!=""){
                map.put(bonus, accessPoint);
            }
        }

        return map;
    }

    private void buildBonusNotification(String bonus) {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.emo_im_money_mouth)
                .setContentTitle(bonus)
                .setContentText("Es wurde ein Gutschein erkannt!");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        mNotificationManager.notify(notificationId, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
