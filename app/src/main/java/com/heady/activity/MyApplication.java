package com.heady.activity;

import android.app.Application;

import com.heady.components.DaggerECommComponent;
import com.heady.components.DaggerNetComponent;
import com.heady.components.ECommComponent;
import com.heady.components.NetComponent;
import com.heady.db.DatabaseHandler;
import com.heady.modules.AppModule;
import com.heady.modules.ECommModule;
import com.heady.modules.NetModule;
import com.heady.utils.Constants;

import dagger.Module;

/**
 * Created by DELL on 12/3/2017.
 */

@Module
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private NetComponent mNetComponent;
    private ECommComponent mECommComponent;
    private DatabaseHandler mDatabaseHandler;

    public MyApplication() {
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(Constants.SERVER_URL))
                .build();

        mECommComponent = DaggerECommComponent.builder()
                .netComponent(mNetComponent)
                .eCommModule(new ECommModule())
                .build();

        mDatabaseHandler = DatabaseHandler.getInstance(MyApplication.getInstance());
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public ECommComponent getEcommComponent() {
        return mECommComponent;
    }

    public DatabaseHandler getDataHelper() {
        return this.mDatabaseHandler;
    }

}
