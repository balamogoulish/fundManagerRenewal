package com.example.fundmanager_renewal.sns;

import android.app.Activity;
import android.util.Log;

import com.example.fundmanager_renewal.callbacks.KakaoCallback;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class KakaoUtils {

    private static final String TAG = "KakaoUtils";

    public static void linkKakao(Activity activity, KakaoCallback callback) {
        Function2<OAuthToken, Throwable, Unit> oauthCallback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG, "CallBack Method");
                if (oAuthToken != null) { // 토큰이 전달된다면 로그인 성공
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if (user != null) {
                                callback.kakaoLoginReceived(user.getId(), user.getKakaoAccount().getProfile().getNickname());
                            }
                            return null;
                        }
                    });
                } else { // 로그인 실패
                    Log.e(TAG, "invoke: login fail");
                }
                return null;
            }
        };

        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.getInstance().loginWithKakaoTalk(activity, oauthCallback);
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(activity, oauthCallback);
        }
    }
}
