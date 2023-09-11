
package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.Ad_Util;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.MainActivity;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.R;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.TutorialActivity;

import java.util.Date;

/**
 * Application class that initializes, loads and show ads when activities change states.
 */
public class MyApplication extends Application implements ActivityLifecycleCallbacks, LifecycleObserver {

    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    private static final String TAG = "MyApplication";
    AdView adView;
    AdRequest adRequest;
    Dialog adBlockdialog;
    int clickCount = 0;

    private static final long COUNTER_TIME = 3;


    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);

        // Log the Mobile Ads SDK version.


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager();
    }


    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {

    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.


        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
        if (currentActivity instanceof TutorialActivity) {
            appOpenAdManager.showAdIfAvailable(currentActivity);
            clickCount=1;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
        appOpenAdManager.releaseAppOpenAd();
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    /**
     * Inner class that loads and shows app open ads.
     */
    private class AppOpenAdManager {

        private static final String LOG_TAG = "AppOpenAdManager";


        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;
        boolean dismisedAd = false;
        private long secondsRemaining;
        boolean showfullscreen = false;
        //        private InAppBilling inAppBilling;
        boolean isInAppPurchased;
        private Dialog adBlockdialog;


        /**
         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
         */
        private long loadTime = 0;

        /**
         * Constructor.
         */
        public AppOpenAdManager() {
        }


        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        // for high unit id
        private void loadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();


            AppOpenAd.load(context, getString(R.string.open_app_highId), request, 1, new AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();

//                    startMainActivity();

                    Log.d(LOG_TAG, "onAdLoaded.");
                    // dismisAdif();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                    Log.d(LOG_TAG, "onAdFailedToLoad: app open " + loadAdError.getMessage());
                    forLowloadAd(context);
                }
            });


        }


        // for medium unit id
        private void forMediumloadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
            AppOpenAd.load(context, getString(R.string.open_app_mediumid), request, 1, new AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();
//                    startMainActivity();
//                    createTimer(COUNTER_TIME);
                    // dismisAdif();
                    Log.d(LOG_TAG, "onAdLoaded.");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                    forLowloadAd(context);
                    Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                }
            });
        }

        // for low unit id
        private void forLowloadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
            AppOpenAd.load(context, getString(R.string.open_app_lowId), request, 1, new AppOpenAdLoadCallback() {

                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    // dismisAdif();
                    loadTime = (new Date()).getTime();

//                    createTimer(COUNTER_TIME);


                    Log.d(LOG_TAG, "onAdLoaded.");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                    //    forLowloadAd(context);
//                    Intent intent = new Intent(currentActivity, SecondActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                    Log.d(LOG_TAG, "onAdFailedToLoad: it is low ad " + loadAdError.getMessage());
                }
            });
        }

        private void createTimer(long seconds) {


            CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    secondsRemaining = ((millisUntilFinished / 1000) + 1);
                }

                @Override
                public void onFinish() {
                    secondsRemaining = 0;
                    if (!dismisedAd && !showfullscreen) {
//                        startMainActivity();
                    }
                }

            };
            countDownTimer.start();
        }

        /**
         * Check if ad was loaded more than n hours ago.
         */
        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        /**
         * Check if ad exists and can be shown.
         */
        private boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            });
        }

        private void showAdIfAvailable(@NonNull final Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                /** Called when full screen content is dismissed. */
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    appOpenAd = null;
                    isShowingAd = false;
//                    startMainActivity();
                    onShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
//
                    dismisedAd = true;
                    //startMainActivity();
                }

                /** Called when fullscreen content failed to show. */
                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    appOpenAd = null;
                    isShowingAd = false;

                    Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

//                    onShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
                }

                /** Called when fullscreen content is shown. */
                @Override
                public void onAdShowedFullScreenContent() {
                    showfullscreen = true;

                    Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
                }
            });

            isShowingAd = true;

            appOpenAd.show(activity);

        }

        public void releaseAppOpenAd() {
            appOpenAd = null;
        }
    }
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
