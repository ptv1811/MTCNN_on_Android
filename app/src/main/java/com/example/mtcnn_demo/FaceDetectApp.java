package com.example.mtcnn_demo;

import android.app.Application;
import android.content.Context;

public class FaceDetectApp extends Application {
    public static Context sAppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
    }

    public Context getsAppContext() {
        return sAppContext;
    }

    public void setsAppContext(Context sAppContext) {
        this.sAppContext = sAppContext;
    }
}
