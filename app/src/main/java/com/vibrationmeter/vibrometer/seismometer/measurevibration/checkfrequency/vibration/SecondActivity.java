package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.Ad_Util.InAppBilling;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements SensorEventListener {
    //    LottieAnimationView lottie1;
    BottomNavigationView bottomNavigationView;
    VibrationMeter vibrationMeter = new VibrationMeter();
    SavedFragment savedFragment = new SavedFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    int Ads_click_Count = 0;
    private boolean isStart = false;
    private boolean isSecondAd = false, firstaction = false;
    Bundle bundle = new Bundle();
    AdRequest adRequest;
    InterstitialAd mInterstitialAd;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Boolean AdsDisplay = false;
    Dialog adBlockdialog, loadindAdDialogue, exitDialog;
    boolean isInAppPurchased = false;
    CardView subBtn;
    private boolean is_High_called = false, is_Medium_Called = false, ishighDisplay = false, isMediumDisaplay = false, isthirdCalled = false;
    ConstraintLayout adLayout;
    int adcount = 1;
    InAppBilling inAppBilling;
    TemplateView templateView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView drawerBtn, adDialogBtn;
    TextView mainTitle;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        initView();
        showmainScreen();
        setDialog();

        setupDrawerContent(navigationView);
        inAppBilling = new InAppBilling(this, this);
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        adLayout = findViewById(R.id.adLayout);
        if (adBlockdialog != null && !isInAppPurchased) {
            adBlockdialog.show();
        }
        showNativeAd();
        drawerBtn.setOnClickListener(view -> {
            drawerLayout.openDrawer(navigationView);
        });
        adDialogBtn.setOnClickListener(view -> {
            if (adBlockdialog != null) {
                adBlockdialog.show();
            }
        });


    }

    private void initView() {
        navigationView = findViewById(R.id.navView_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerBtn = findViewById(R.id.drawer_open);
        mainTitle = findViewById(R.id.main_title);
        adDialogBtn = findViewById(R.id.ad_img_btn);
    }


    private void showmainScreen() {
        adRequest = new AdRequest.Builder().build();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, vibrationMeter).commit();
        mainTitle.setText("Vibration Meter");
        setFragArgument();
        showDialogue();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Menuid = item.getItemId();
                Log.d("Saved Or History ids", "Saved id = " + R.id.saved + ", " + "History Ids = " + R.id.history + ", " + "Selected id= " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.vibration_meter:
                        Log.d("History", "History is clicked so it should go to history fragment");
                        AdCount(1);
                        mainTitle.setText("Vibration Meter");

                        return true;
                    case R.id.saved:
                        Log.d("Saved", "History is clicked so it should go to history fragment");
                        AdCount(2);
                        mainTitle.setText("Saved");

                        return true;
                    case R.id.history:
                        Log.d("History", "History is clicked so it should go to history fragment");
                        AdCount(3);
                        mainTitle.setText("History");

                        return true;
                }
                return false;
            }
        });
        List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);

        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder().setTestDeviceIds(testDevices).build();
        MobileAds.initialize(this);
        MobileAds.setRequestConfiguration(requestConfiguration);
        adRequest = new AdRequest.Builder().build();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void setFragArgument() {
        getData();
        bundle.putString("Ads_click_Count", String.valueOf(Ads_click_Count));
        bundle.putBoolean("SecondAds", isSecondAd);
        int num = 65;
        bundle.putString("Hello", String.valueOf(num));
        num++;
        vibrationMeter.setArguments(bundle);
        showAds();

        Ads_click_Count++;

    }

    public void getData() {
        if (!isSecondAd) {
            isSecondAd = vibrationMeter.isSecondAd;
        }
        if (Ads_click_Count != vibrationMeter.Ads_click_Count && Ads_click_Count < vibrationMeter.Ads_click_Count) {
            Ads_click_Count = vibrationMeter.Ads_click_Count;
        }
    }

    public void funAd() {
        InterstitialAd interstitialAd;

        InterstitialAd.load(this, getString(R.string.interstial_Ad), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {

                    mInterstitialAd.show(SecondActivity.this);

                }
                //  showDialogue();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                // AdsDisplay = true;
            }
        });
        AdsDisplay = true;
    }

    public void showAds() {
        Log.d("Ads count", "Ads COunt = " + Ads_click_Count);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        if (!isStart && Ads_click_Count == 3) {
            if (connected) {
                alertDialog.show();
            }
            funAd();

            Ads_click_Count = 0;
        } else {
            if (Ads_click_Count == 2 && !isSecondAd) {
                if (connected) {
                    alertDialog.show();
                }
                funAd();
                firstaction = true;
                isSecondAd = true;
                Ads_click_Count = 0;
            }
        }
    }

    public void showDialogue() {
        builder = new AlertDialog.Builder(this);
        builder.setView(this.getLayoutInflater().inflate(R.layout.loading, null));
        alertDialog = builder.create();
        int color = Color.parseColor("#0C0C0C");
        alertDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alertDialog.getWindow().setBackgroundBlurRadius(12);
        }
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(color));
        //alertDialog.getWindow().getAttributes().height*1.0f;


    }

    private void setDialog() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_scale);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        adBlockdialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        View view1 = getLayoutInflater().inflate(R.layout.ad_remover_dialog, null);
        adBlockdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adBlockdialog.setContentView(view1);
        Window window = adBlockdialog.getWindow();


        WindowManager.LayoutParams wlp = window.getAttributes();
        TextView closebtn = view1.findViewById(R.id.cross_btn);
        subBtn = view1.findViewById(R.id.subscribeNow);
        subBtn.startAnimation(animation);

        closebtn.setOnClickListener(view -> {
            if (adBlockdialog.isShowing()) {
                adBlockdialog.dismiss();
            }
        });
        subBtn.setOnClickListener(view -> {
            inAppBilling.purchase(view);
        });
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        adBlockdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen()) {
            drawerLayout.closeDrawers();
        } else {
            exitDialog.show();
        }
    }

    private void showNativeAd() {
        exitDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        View view1 = LayoutInflater.from(this).inflate(R.layout.exit_dialouge, null);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(view1);

        Window window = exitDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        exitDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        templateView = view1.findViewById(R.id.nativeAds_template);
        ConstraintLayout adnativeLayout = view1.findViewById(R.id.adlayoutNative);

        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        if (isInAppPurchased) {
            templateView.setVisibility(View.GONE);
            adnativeLayout.setVisibility(View.GONE);
        } else {
            templateView.setVisibility(View.VISIBLE);
            adnativeLayout.setVisibility(View.VISIBLE);
        }
        CardView yesBtn, NoBtn;
        yesBtn = view1.findViewById(R.id.yesBtn);
        NoBtn = view1.findViewById(R.id.Nobtn);
        /*LottieAnimationView waveAnimation = view1.findViewById(R.id.lottie_animaion);
        waveAnimation.setRepeatCount(20);
        waveAnimation.playAnimation();
*/
        yesBtn.setOnClickListener(view -> {
            if (exitDialog.isShowing()) {
                exitDialog.dismiss();
            }
//            super.onBackPressed();
            finishAffinity();
        });
        NoBtn.setOnClickListener(view -> {
            exitDialog.dismiss();
        });

//        Native_adLayout = view1.findViewById(R.id.native_adLayout);
        if (!isInAppPurchased) {
            AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_low_Id)).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                    templateView.setStyles(style);
                    templateView.setNativeAd(nativeAd);
                    templateView.setVisibility(View.VISIBLE); // Show the TemplateView since the ad is loaded

//                    setprority(forhigh, formedium, forlow, getString(R.string.native1));
                }
            }).withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d(TAG, "onAdFailedToLoad: Native Ad" + loadAdError);
//
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }

    }

    private void loadBannerAd() {
        ishighDisplay = true;
        is_Medium_Called = false;
        isthirdCalled = false;
        showBannerAd();
    }

    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_high_Id));
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
//       this.adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(TAG, "onAdFailedToLoad: banner high Ad Error for " + loadAdError);
                }
                showMediumBannerAd();
            }
        });
    }

    private void showMediumBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_medium_id));
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
//       this.adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(TAG, "onAdFailedToLoad: banner mediumr Ad Error for " + loadAdError);
                }
                showLowBannerAd();
            }
        });
    }

    private void showLowBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_low_Id));
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
//       this.adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {

                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(TAG, "onAdFailedToLoad: banner  low Ad Error for " + loadAdError);

                }
            }
        });
    }

    private void setprority(boolean forhigh, boolean formedium, boolean forlow, String strvalue) {
        if (forhigh) {
            ishighDisplay = true;
        } else if (formedium) {
            isMediumDisaplay = true;
        } else {
            is_High_called = false;
            ishighDisplay = false;
            is_Medium_Called = false;
            isMediumDisaplay = false;
        }
    }

    private void checkprority(boolean forhigh, boolean formedium, boolean forlow, String strvalue, int val) {
        if (is_High_called && !is_Medium_Called) {
            is_Medium_Called = true;
            if (strvalue.equals(getString(R.string.banner))) {
//                showBannerAd(getString(R.string.banner_medium_id), false, true, false, getString(R.string.banner));
            } else if (strvalue.equals(getString(R.string.interstial))) {
                showInterstialAd(getString(R.string.interstial_medium_id), false, true, false, getString(R.string.interstial), val);
            }
        } else {
            if (strvalue.equals(getString(R.string.banner))) {
//                showBannerAd(getString(R.string.banner_low_Id), false, false, true, getString(R.string.banner));
            } else if (strvalue.equals(getString(R.string.interstial))) {
                showInterstialAd(getString(R.string.interstial_low_Id), false, false, true, getString(R.string.interstial), val);
            }
        }

    }

    private void showInterstialAd(String unitId, boolean forhigh, boolean formedium, boolean forlow, String Value, int acti_value) {

        AdRequest ad_request = new AdRequest.Builder().build();
        InterstitialAd.load(SecondActivity.this, unitId, ad_request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                setprority(forhigh, formedium, forlow, Value);
                // interstitialAd=interstitialAd;
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        moveToNextActivity(acti_value);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        if (loadindAdDialogue != null) {
                            if (loadindAdDialogue.isShowing()) {
                                loadindAdDialogue.dismiss();
                            }
                        }
                    }
                });
                if (interstitialAd != null) {
                    interstitialAd.show(SecondActivity.this);
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (!forlow) {
                    checkprority(forhigh, formedium, forlow, Value, acti_value);

                } else if (forlow && !isthirdCalled) {
                    if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                        loadindAdDialogue.dismiss();
                    }
                    Log.d(TAG, "onAdFailedToLoad: Interstitial Ad" + loadAdError);
                    checkprority(forhigh, formedium, forlow, Value, acti_value);
                    moveToNextActivity(acti_value);
                    isthirdCalled = true;
                }
            }
        });

    }


    public void laodinterstialAd(int value) {
        is_Medium_Called = true;
        is_High_called = true;
        isthirdCalled = false;
        showInterstialAd(getString(R.string.interstial_medium_id), true, false, false, getString(R.string.interstial), value);
    }

    private void AdCount(int val) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        if (adcount % 2 == 1) {
            if (!isInAppPurchased && connected) {
                laodinterstialAd(val);
                /// showProgressDialogue();
                showAdLoadingDialogue();
            } else {
                moveToNextActivity(val);
            }
        } else {
            moveToNextActivity(val);
        }
        adcount++;
    }

    private void moveToNextActivity(int Value) {
        Intent intent;
        switch (Value) {
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, vibrationMeter).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, savedFragment).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, historyFragment).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, vibrationMeter).commit();
                break;
        }
        // Optional: finish the current activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adBlockdialog != null) {
            if (adBlockdialog.isShowing()) {
                adBlockdialog.dismiss();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    private void showAdLoadingDialogue() {

        loadindAdDialogue = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        View view1 = getLayoutInflater().inflate(R.layout.loading, null);
        loadindAdDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadindAdDialogue.setContentView(view1);
        Window window = loadindAdDialogue.getWindow();
        loadindAdDialogue.setCancelable(false);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        loadindAdDialogue.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        loadindAdDialogue.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inAppBilling = new InAppBilling(SecondActivity.this, SecondActivity.this);
        boolean isPurchased = inAppBilling.isCurrentpurchased();
        Log.d(TAG, "onResume: " + isPurchased);
        if (isPurchased) {
            inAppBilling.is_Current_Purchased = false;
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        } else {
            isInAppPurchased = inAppBilling.hasUserBoughtInApp();
            showNativeAd();
            if (isInAppPurchased) {
                adLayout.setVisibility(View.GONE);
                adDialogBtn.setVisibility(View.GONE);
            } else {
                adLayout.setVisibility(View.VISIBLE);
                adDialogBtn.setVisibility(View.VISIBLE);
                loadBannerAd();
            }
            if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                loadindAdDialogue.dismiss();
            }
        }
        if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
            loadindAdDialogue.dismiss();
        }
//

    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public boolean selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.shareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Go To Play Store For Download this App " + "\n\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);

                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                // fragmentClass = FirstFragment.class;
                break;
            case R.id.rate_us:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));

//                Toast.makeText(getActivity(), "Second object clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more_app:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Bhagowal+Hood")));
                break;
            case R.id.privacy_policy:
            case R.id.terms:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/bhagowalhood/home"));
                startActivity(browserIntent);
//                Toast.makeText(this, "terms object clicked", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        drawerLayout.closeDrawers();
        return false;


    }
}