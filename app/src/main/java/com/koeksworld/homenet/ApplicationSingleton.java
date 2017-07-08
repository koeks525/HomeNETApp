package com.koeksworld.homenet;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Okuhle on 2017/07/04.
 */

//Source: http://www.androidhive.info/2016/05/android-working-with-realm-database-replacing-sqlite-core-data/
public class ApplicationSingleton extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("HomeNET").schemaVersion(0).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
