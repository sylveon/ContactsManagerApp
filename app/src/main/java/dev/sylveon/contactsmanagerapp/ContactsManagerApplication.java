package dev.sylveon.contactsmanagerapp;

import android.app.Application;

import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import dev.sylveon.contactsmanagerapp.services.AppDatabase;

public class ContactsManagerApplication extends Application {
    private static final String TAG = ContactsManagerApplication.class.getSimpleName();

    private static ContactsManagerApplication mInstance;
    private AppDatabase mAppDatabase;
    private RequestQueue mRequestQueue;

    public static ContactsManagerApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public synchronized AppDatabase getDatabase() {
        if (mAppDatabase == null) {
            mAppDatabase = Room
                    .databaseBuilder(getApplicationContext(), AppDatabase.class, "contacts-database")
                    .allowMainThreadQueries()
                    .build();
        }

        return mAppDatabase;
    }

    public synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
