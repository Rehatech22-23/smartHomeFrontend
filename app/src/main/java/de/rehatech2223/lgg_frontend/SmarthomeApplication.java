package de.rehatech2223.lgg_frontend;

import android.app.Application;
import android.content.Context;

/**
 * Application that starts on launch and loads app.
 * can be used to request context from anywhere under any circumstances
 * without memory leaks.
 *
 * @author Fynn Debus
 */
public class SmarthomeApplication extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

}
