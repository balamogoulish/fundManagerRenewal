package com.example.fundmanager_renewal.sns;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    private static KakaoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,"84565de73f708f3e0f193397c8ce6158");
    }
}
