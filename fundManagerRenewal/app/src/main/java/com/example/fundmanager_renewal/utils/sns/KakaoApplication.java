package com.example.fundmanager_renewal.utils.sns;

import android.app.Application;

import com.example.fundmanager_renewal.R;
import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    private static KakaoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,getString(R.string.kakaoApi));
    }
}
