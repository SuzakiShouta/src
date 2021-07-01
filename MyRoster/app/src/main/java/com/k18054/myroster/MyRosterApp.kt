package com.k18054.myroster

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyRosterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //Realm 初期化
        Realm.init(this)
        //扱うデータが少量なのでUiThreadをtrue(UIスレッドでも書き込める)に。
        val config = RealmConfiguration.Builder().allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)
    }
}