package com.example.fundmanager_renewal.utils.sns;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.NaverCallback;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfile;
import com.navercorp.nid.profile.data.NidProfileResponse;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;


public class NaverUtils {
    private static final String TAG = "NaverUtils";
    public static void linkNaver(Activity activity, NaverCallback callback){
        final Ref.ObjectRef naverToken = new Ref.ObjectRef();
        naverToken.element = "";
        NidProfileCallback<NidProfileResponse> profileCallback = new NidProfileCallback<NidProfileResponse>() {
            @Override
            public void onSuccess(NidProfileResponse response) {
                if(response!=null){
                    NidProfile userProfile = response.getProfile();
                    String userId = userProfile.getId();
                    String nickName = userProfile.getNickname();
                    callback.naverLoginReceived(userId, nickName);
                }
            }
            @Override
            public void onFailure(int i, @NonNull String s) {
                String errorCode = NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode();
                String errorDescription = NaverIdLoginSDK.INSTANCE.getLastErrorDescription();
                Log.e("NAVERFAIL", errorCode+errorDescription);
            }
            @Override
            public void onError(int i, @NonNull String s) {
                onFailure(i, s);
            }
        };

        OAuthLoginCallback oauthLoginCallback = new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                naverToken.element = NaverIdLoginSDK.INSTANCE.getAccessToken();
                NidOAuthLogin nidOAuthLogin = new NidOAuthLogin();
                nidOAuthLogin.callProfileApi(profileCallback);
            }
            @Override
            public void onFailure(int httpStatus, @NonNull String message) {
                Intrinsics.checkNotNullParameter(message, "message");
                String errorCode = NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode();
                String errorDescription = NaverIdLoginSDK.INSTANCE.getLastErrorDescription();
                Log.d("oAuthLogin", "errorCode: " + errorCode + '\n' + "errorDescription: " + errorDescription);
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Intrinsics.checkNotNullParameter(s, "message");
                this.onFailure(i, s);
            }
        };
        NaverIdLoginSDK.INSTANCE.authenticate(activity, oauthLoginCallback);
    }
}
