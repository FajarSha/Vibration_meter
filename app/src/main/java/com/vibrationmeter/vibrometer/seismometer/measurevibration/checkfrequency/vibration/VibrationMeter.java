package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import static com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.R.id.infobtn;
import static com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.R.layout.fragment_vibration_meter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.anastr.speedviewlib.Speedometer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.datatransport.BuildConfig;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.navigation.NavigationView;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.HistoryDao;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.HistoryDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VibrationMeter extends Fragment implements SensorEventListener {

    private LineChart mChart;
    ImageView infoicon, playbtn, pausebtn, Savedbtn;
//    DrawerLayout drawerLayout;
//    private NavigationView navigationView;

    Speedometer speedometer;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float vibrateThresholdValue = 0;
    private Vibrator vibrator;
    private float[] Gravity = new float[3];
    private float[] linear_Acceleration = new float[3];
    private int seconds = 0;
    private String CounterTime;
    private long savedTime = 0;
    private long currentTime = 0;
    boolean isChart = false, isSecondAd = false, forsecondAd, updating = false, isSensorStart = false;
    private boolean is_from_ad = false;
    TextView Peaktextvalue, AVGtxtvalue, Timitxtvalue, Rangetxt, SoundAcceleration;
    private ArrayList<Entry> yVals;

    Bundle bundle = new Bundle();
    private ArrayList<Double> Average = new ArrayList<>();
    Handler handler;
    private HistoryDatabase historyDatabase;
    private HistoryDao historyDao;
    private InterstitialAd mInterstitialAd;
    private boolean isStart = false, isActive = false;
    private int thirdAdCount;
    int Ads_click_Count = 0;
    AdRequest adRequest;
    String updateCountertime;
    AlertDialog.Builder builder;
//    AlertDialog alertDialog;
    Boolean AdsDisplay = false;

    @SuppressLint("MissingInflatedId")
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(fragment_vibration_meter, container, false);
        super.onCreate(savedInstanceState);
        initliazation(v);
//        LoadingDialog();
        adRequest = new AdRequest.Builder().build();
        if (!isActive) {
            //     getArgumentValue();
        }
        //SendDatatoActivity();
        historyDatabase = HistoryDatabase.getInstance(getContext());
        historyDao = historyDatabase.getDao();
        //  MobileAds.initialize(getContext());

        setUpOnBackPressed();
        pausebtn.setVisibility(View.GONE);
        Rangetxt.setText(R.string.first_title);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBtnPlay();
                getArgumentValue();
//                showAds();

                Ads_click_Count++;
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updating = false;
                // showAds();
                playbtn.setVisibility(View.VISIBLE);
                pausebtn.setVisibility(View.GONE);
                handler.removeCallbacksAndMessages(null);

                //  lottie.cancelAnimation();
            }
        });
//        navigationView = v.findViewById(R.id.navView_drawer);
//        setupDrawerContent(navigationView);
        // speedometer.speedTo(50, 10000);
        speedometer.getSections().get(0).setColor(Color.BLACK);
        speedometer.getSections().get(1).setColor(Color.parseColor("#6b0808"));
        speedometer.getSections().get(2).setColor(Color.RED);
        Rangetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogvoicetext = new DialogFragment();
                dialogvoicetext.show(getActivity().getSupportFragmentManager(), "My Dialog Popup");
            }
        });
        infoicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogvoicetext = new DialogFragment();
                dialogvoicetext.show(getActivity().getSupportFragmentManager(), "My Dialog Popup");
            }
        });
        sensorManager = (SensorManager) this.getActivity().getSystemService(getContext().SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThresholdValue = accelerometer.getMaximumRange() / 2;
        }
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        Savedbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d("Time_Counter Value", "Time Counter Value = " + CounterTime + ", " + " updateCountertime = " + updateCountertime);
                if (isSensorStart && Timitxtvalue.getText().toString() == CounterTime && updateCountertime != CounterTime) {
                    getArgumentValue();
//                    showAds();
                    Ads_click_Count++;
                    updateCountertime = CounterTime;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = sdf.format(new Date());
                    Date currentdate = new Date();
                    DateFormat df = new SimpleDateFormat("hh:mm a", Locale.US);
                    String currentTime = df.format(new Date());
                    Double peakValue = Double.parseDouble(Peaktextvalue.getText().toString());
                    // isSensorStart=false;
                    Double average = Double.parseDouble(AVGtxtvalue.getText().toString());
                    String Countervalue = Timitxtvalue.getText().toString();
                    HistoryModel historyModel = new HistoryModel(0, currentDate, currentTime, Countervalue, peakValue, average);
                    historyDao.insert(historyModel);
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    //AlertShow("","Saved successfully");

                } else {
                    Toast.makeText(getContext(), "Play the measurement", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        drawerOpen_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(navigationView);
//            }
//        });
        List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build();
        MobileAds.initialize(getContext());
        MobileAds.setRequestConfiguration(requestConfiguration);
        return v;
    }

    public void onResume() {
        super.onResume();
        Log.d("isfromad", "is from ad = " + is_from_ad);
        if (is_from_ad) {
            is_from_ad = false;
        } else {
            if (yVals != null && !is_from_ad) {
                Refreshfragment();
                isSecondAd = forsecondAd;
            }
        }
        sensorManager.registerListener((SensorEventListener) getActivity(), accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        super.onPause();
        if (isSecondAd) {
            forsecondAd = isSecondAd;
        }
//        if (drawerLayout.isOpen()){
//            drawerLayout.closeDrawers();
//        }
        sensorManager.unregisterListener((SensorEventListener) getActivity());
    }

    public void Refreshfragment() {
        seconds = 0;
        Boolean forsecondAd;
        updating = false;
        getActivity().getSupportFragmentManager().beginTransaction().replace(VibrationMeter.this.getId(), new VibrationMeter()).commit();
        if (isSecondAd) {
            forsecondAd = isSecondAd;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (updating) {
            playbtn.setVisibility(View.GONE);
            pausebtn.setVisibility(View.VISIBLE);
            final float alpha = 0.8f;
            //gravity is calculated here
            Gravity[0] = alpha * Gravity[0] + (1 - alpha) * event.values[0];
            Gravity[1] = alpha * Gravity[1] + (1 - alpha) * event.values[1];
            Gravity[2] = alpha * Gravity[2] + (1 - alpha) * event.values[2];
            //acceleration retrieved from the event and the gravity is removed
            linear_Acceleration[0] = event.values[0] - Gravity[0];
            linear_Acceleration[1] = event.values[1] - Gravity[1];
            linear_Acceleration[2] = event.values[2] - Gravity[2];

            double x2 = Math.pow(event.values[0], 2);
            double y2 = Math.pow(event.values[1], 2);
            double z2 = Math.pow(event.values[2], 2);
//            average = Math.sqrt((x2 + y2 + z2)/3);

            double accelationSquareRoot = Math.sqrt(linear_Acceleration[0] * linear_Acceleration[0] + linear_Acceleration[1] * linear_Acceleration[1] + linear_Acceleration[2] * linear_Acceleration[2]) / SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH;
            Average.add(accelationSquareRoot);
            double averagevalue = calculateAverage(Average);


            updateData((float) accelationSquareRoot, 00);
            // speedometer = getView().findViewById(R.id.speedView);
            speedometer.speedTo((float) accelationSquareRoot, 1000);

            SoundAcceleration.setText(String.format("%.2f", accelationSquareRoot));
            Peaktextvalue.setText(String.format("%.2f", accelationSquareRoot));
            AVGtxtvalue.setText(String.format("%.2f", averagevalue));

            if (accelationSquareRoot > 0.01) {
                Rangetxt.setText(R.string.first_title);
            }
            if (accelationSquareRoot > 0.02) {
                Rangetxt.setText(R.string.Second_title);
            }
            if (accelationSquareRoot > 0.04) {
                Rangetxt.setText(R.string.third_title);
            }
            if (accelationSquareRoot > 0.07) {
                Rangetxt.setText(R.string.four_title);
            }

            if (accelationSquareRoot > 0.15) {
                Rangetxt.setText(R.string.fifth_title);
            }
            if (accelationSquareRoot > 1.0) {
                Rangetxt.setText(R.string.six_title);
            }

            if (accelationSquareRoot > 3.0) {
                Rangetxt.setText(R.string.Seven_title);
            }
            if (accelationSquareRoot > 6.0) {
                Rangetxt.setText(R.string.Eight_title);
            }
            if (accelationSquareRoot > 10.0) {
                Rangetxt.setText(R.string.Nine_title);
            }
            if (accelationSquareRoot > 14.0) {
                Rangetxt.setText(R.string.Eleven_title);
            }

        }
    }

    private double calculateAverage(ArrayList<Double> marks) {
        double sum = 0;
        if (!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum / marks.size();
        }
        return sum;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void Runtimer() {
        if (updating) {
            int hour = (int) (seconds / 3600);
            int minutes = (int) ((seconds % 3600) / 60);
            int secs = (int) (seconds % 60);
            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, secs);
            CounterTime = time;
            Timitxtvalue.setText(time);
        }
    }

    public void initliazation(View v) {

//        drawerOpen_btn = v.findViewById(R.id.drawer_open);
//        materialToolbar = v.findViewById(R.id.top_Bar);
        speedometer = v.findViewById(R.id.speedView);
        //lottie = v.findViewById(R.id.lottieanimaion);
        playbtn = v.findViewById(R.id.playbtn);
        pausebtn = v.findViewById(R.id.pausebtn);
//        drawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
//        navigationView = (NavigationView) v.findViewById(R.id.navView_drawer);
//        materialToolbar = v.findViewById(topAppBar);
        infoicon = v.findViewById(infobtn);
        AVGtxtvalue = v.findViewById(R.id.AVGvalue);
        Peaktextvalue = v.findViewById(R.id.peakvalue);
        Timitxtvalue = v.findViewById(R.id.timevalue);
        Rangetxt = v.findViewById(R.id.rangetxt);
        SoundAcceleration = v.findViewById(R.id.soundvalue);
        Savedbtn = v.findViewById(R.id.saved_btn);

    }

    private void initChart() {
        if (mChart != null) {
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                Log.d(String.valueOf(savedTime), "This is if message");
                savedTime++;

                isChart = true;
            }
        } else {
            Log.d(String.valueOf(savedTime), "This is else message");
            currentTime = new Date().getTime();
            mChart = (LineChart) getView().findViewById(R.id.linechart);
            mChart.getDescription().setEnabled(false);
            mChart.setViewPortOffsets(50, 20, 5, 60);
            // no description text
            //   mChart.setDescription(getString(R.string.graph));
            // enable touch gestures
            mChart.setTouchEnabled(true);
            // enable scaling and dragging
            mChart.setDragEnabled(false);
            mChart.setScaleEnabled(true);
            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(true);
            mChart.setDrawGridBackground(false);
            mChart.setMaxHighlightDistance(100);
            mChart.getAxisLeft().setStartAtZero(false);

            XAxis x = mChart.getXAxis();

            x.setLabelCount(8, false);
            x.setEnabled(true);

            x.setTextColor(getResources().getColor(R.color.blue_primary));
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setDrawGridLines(true);
            x.setAxisLineColor(getResources().getColor(R.color.blue_primary));
            YAxis y = mChart.getAxisLeft();
            y.setLabelCount(6, false);
            y.setTextColor(getResources().getColor(R.color.blue_primary));
            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            y.setDrawGridLines(false);
            y.setAxisLineColor(getResources().getColor(R.color.blue_primary));
            //   mChart.setVisibleYRangeMaximum(800, YAxis.AxisDependency.LEFT);

            y.setAxisMinValue(0);
            y.setAxisMaxValue(12);

            mChart.getAxisRight().setEnabled(true);
            yVals = new ArrayList<Entry>();
            yVals.add(new Entry(0, 0));
            LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.02f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setCircleColor(getResources().getColor(R.color.light_red));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(getResources().getColor(R.color.light_red));
            set1.setFillColor(getResources().getColor(R.color.light_red));
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });
            LineData data;
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                data = mChart.getLineData();
                data.clearValues();
                data.removeDataSet(0);
                data.addDataSet(set1);
            } else {
                data = new LineData(set1);
            }
            data.setValueTextSize(1f);
            data.setDrawValues(false);
            mChart.setData(data);
            mChart.getLegend().setEnabled(false);
            mChart.animateXY(50000, 10000);
            // dont forget to refresh the drawing
            mChart.invalidate();
            isChart = true;
        }
    }

    private void updateData(float val, long time) {
        if (mChart == null) {
            return;
        }
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            LineDataSet set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            Entry entry = new Entry(savedTime, val);
            set1.addEntry(entry);
            if (set1.getEntryCount() > 200) {
                set1.removeFirst();
                set1.setDrawFilled(false);
            }
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
            savedTime++;
        }
    }

    private void OnBtnPlay() {
        isSensorStart = true;
        playbtn.setVisibility(View.GONE);
        pausebtn.setVisibility(View.VISIBLE);
        updating = true;
        handler = new Handler();
        if (!isChart) {
            if (updating) {
                initChart();
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Runtimer();
                seconds++;

                handler.postDelayed(this, 1000);
            }
        }, 1000);
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

    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.shareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Go To Play Store For Download this App " + "\n\n" + "https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                // fragmentClass = FirstFragment.class;
                break;
            case R.id.rate_us:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
//                Toast.makeText(getActivity(), "Second object clicked", Toast.LENGTH_SHORT).show();
                /// fragmentClass = SecondFragment.class;
                break;
            case R.id.more_app:
                Toast.makeText(getActivity(), "More object clicked", Toast.LENGTH_SHORT).show();
                /// fragmentClass = SecondFragment.class;
                break;
            case R.id.privacy_policy:
                Toast.makeText(getActivity(), "privacy_policy object clicked", Toast.LENGTH_SHORT).show();
                /// fragmentClass = SecondFragment.class;
                break;
            case R.id.terms:
                Toast.makeText(getActivity(), "terms object clicked", Toast.LENGTH_SHORT).show();
                // fragmentClass = ThirdFragment.class;
                break;
            default:
        }
        menuItem.setChecked(true);
//        drawerLayout.closeDrawers();
    }

    public void setUpOnBackPressed() {
//        drawerLayout.closeDrawers();
//        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                drawerLayout.closeDrawers();
////                showDialogue();
//            }
//        });
    }
    public void draClose(){
//        drawerLayout.closeDrawers();
    }

    public void funAd() {
        InterstitialAd interstitialAd;
        InterstitialAd.load(getContext(), getString(R.string.interstial_Ad), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getActivity());
                }

//                if (mInterstitialAd != null) {
//                    mInterstitialAd.show(getActivity());
//                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

  /*  public void showAds() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        if (!isStart && Ads_click_Count == 3) {
            Log.d("if-statement-ads", " This is If statement from count ");
            if (connected) {
                alertDialog.show();
            }
            funAd();
            if (alertDialog.isShowing()) {
                CloseAlertDialog();
            }
            Ads_click_Count = 0;
            is_from_ad = true;
        } else {
            if (Ads_click_Count == 2 && !isSecondAd) {
                Log.d("Else_statement", " This is else statement from count ");
                if (connected) {
                    alertDialog.show();
                }
                funAd();
                if (alertDialog.isShowing()) {
                    CloseAlertDialog();
                }
                isSecondAd = true;
                Ads_click_Count = 0;
                is_from_ad = true;
            }
        }

        //Log.d("2nd-Ads", "2nd-Ads = " + isSecondAd);

    }
*/
    private void getArgumentValue() {
        if (getArguments() != null) {
            Log.d("Argument", "getArgument = " + getArguments().getBoolean("SecondAds") + " , " + getArguments().getString("Ads_click_Count"));
        }

        if (!isActive) {
            isActive = true;
            Bundle mybundle = (this).getArguments();
            if (mybundle != null) {
                String Countvalue = getArguments().getString("Ads_click_Count");
                thirdAdCount = Integer.parseInt(Countvalue);
                boolean ForSecdAd = getArguments().getBoolean("SecondAds");
                if (!isSecondAd && ForSecdAd) {
                    isSecondAd = ForSecdAd;
                }
                Ads_click_Count = thirdAdCount;

            } else {
                setfragArgument();
            }
        }

        Log.d("is Active", "Is Active = " + isActive);
    }

    public void setfragArgument() {
        bundle.putBoolean("SecondAds", isSecondAd);
        bundle.putString("Ads_click_Count", String.valueOf(thirdAdCount));
    }

    public void SendDatatoActivity() {
        Intent intent = new Intent(getActivity().getBaseContext(),
                SecondActivity.class);
        intent.putExtra("SecondAds", isSecondAd);
        intent.putExtra("Ads_click_Count", String.valueOf(Ads_click_Count));

    }

    public void showDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(this.getLayoutInflater().inflate(R.layout.native_dialog, null));
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
        TemplateView templateView = alertDialog.findViewById(R.id.nativeAds_template);
        TextView exittxt = alertDialog.findViewById(R.id.Confirm_exit);
        exittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                Intent intent = new Intent(getActivity(), VibrationMeter.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                /// startActivity(intent);
                getActivity().finish();
                //getActivity().finish();
            }
        });
        AdLoader adLoader = new AdLoader.Builder(getContext(), getString(R.string.Native_Ads))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {

                        NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                        templateView.setStyles(style);
                        templateView.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    /*public void LoadingDialog() {
        builder = new AlertDialog.Builder(getContext());
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


    }*/

    /*private void CloseAlertDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                alertDialog.dismiss();

            }
        }, 3000L);
    }*/
}