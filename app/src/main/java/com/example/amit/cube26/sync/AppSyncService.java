package com.example.amit.cube26.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by amit on 7/23/2015.
 */
public class AppSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static AppSyncAdapter sCube26SyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sCube26SyncAdapter == null) {
                sCube26SyncAdapter = new AppSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sCube26SyncAdapter.getSyncAdapterBinder();
    }
}
