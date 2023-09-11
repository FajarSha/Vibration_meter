package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.Ad_Util.InAppBilling;


public class MainActivity extends AppCompatActivity {
    ;
    Context context;

    LottieAnimationView lottie;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    boolean isInAppPurchase = false;
    ConstraintLayout adLayout;
    InAppBilling inAppBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this);
        adLayout = findViewById(R.id.adLayout);


        lottie = findViewById(R.id.lottieanimaion);
        lottie.setRepeatCount(20);

        int yourColor = ContextCompat.getColor(getBaseContext(), R.color.blue_primary);
        SimpleColorFilter filter = new SimpleColorFilter(yourColor);
        KeyPath keyPath = new KeyPath("**");
        LottieValueCallback<ColorFilter> callback = new LottieValueCallback<ColorFilter>(filter);
        lottie.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback);
        lottie.playAnimation();
        inAppBilling = new InAppBilling(this, this);
        isInAppPurchase=inAppBilling.hasUserBoughtInApp();
        if(isInAppPurchase){
            new Handler().postDelayed(() -> {
                Intent localIntent = new Intent(MainActivity.this, TutorialActivity.class);
                MainActivity.this.startActivity(localIntent);
                MainActivity.this.finish();
            }, 3000L);
        }
        else {
            new Handler().postDelayed(() -> {
                Intent localIntent = new Intent(MainActivity.this, TutorialActivity.class);
                MainActivity.this.startActivity(localIntent);
                MainActivity.this.finish();


            }, 6000L);
        }



    }

    private void loadBannerAd(String unitId) {
        AdView adView = new AdView(this);
        adView.setAdUnitId(unitId);
        adView.setAdSize(AdSize.BANNER);
//        adView.loadAd(adRequest);
        Bundle extras = new Bundle();
        extras.putString("collapsible", "bottom");
        adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
//       this.adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "onAdLoaded: Banner  Ad on Splash loaded ");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(TAG, "onAdFailedToLoad: banner Ad on Splash Error for " + loadAdError);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        inAppBilling = new InAppBilling(this, this);

        isInAppPurchase = inAppBilling.hasUserBoughtInApp();
        if (isInAppPurchase) {
            adLayout.setVisibility(View.GONE);
        } else {
            adLayout.setVisibility(View.VISIBLE);
            loadBannerAd(getString(R.string.collapsable_test_banner));
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}