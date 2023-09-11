package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;


import static com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.R.layout.fragment_history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.AdapterListener;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.HistoryDao;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.HistoryDatabase;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.SavedModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements AdapterListener {

//    AdView adView;
    ArrayList<HistoryModel> historyModelArray = new ArrayList<>();
//    private DrawerLayout drawerLayout;
//    ImageView drawer_open;
//    private NavigationView navigationView;
    private Toolbar materialToolbar;
    private RecyclerView recyclerView;
    private HistoryDatabase historyDatabase;
    private HistoryDao historyDao;
    private RecyclerHistoryAdapter adapter;
    TextView Emptytxt;
    private AdapterListener adapterListener;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(fragment_history, null);

        historyDatabase = HistoryDatabase.getInstance(getContext());
        historyDao = historyDatabase.getDao();
        Initializatio(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerHistoryAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        setUpOnBackPressed();
//        setupDrawerContent(navigationView);
        Emptytxt.setVisibility(View.GONE);
        List<HistoryModel> historyList = historyDao.getAllHistory();
        Log.d("Imp my message", "history list size=" + historyList.size() + "is empty or not");
        if (historyList.size() <= 0) {
            Emptytxt.setVisibility(View.VISIBLE);
            Emptytxt.setText(R.string.emptytxt);
        }
//        drawer_open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(navigationView);
//            }
//        });
        MobileAds.initialize(getContext());
        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
        return view;
    }
    private void fetchData() {
        List<HistoryModel> historyList = historyDao.getAllHistory();
        Log.d("History list","History List.size = "+ historyList.size());
        for (int i = 0; i < historyList.size(); i++) {
            HistoryModel history = historyList.get(i);
            adapter.addhistoryRecord(history);
            adapter.notifyDataSetChanged();
        }
        if (historyList.size() <= 0) {
            Emptytxt.setVisibility(View.VISIBLE);
        } else {
            Emptytxt.setVisibility(View.GONE);
        }
    }
    @Override
    public void OnUpdate(int id, int pos) {

    }
    @Override
    public void OnDelete(int id, int pos) {

        historyDao.delete(id);
        adapter.RemoveSavedhistory(pos);
    }
    @Override
    public void OnitemDelete(HistoryModel model) {

    }
    @Override
    public void OnitemSahre(HistoryModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Peak value : " + model.getPeak_value() + "," + "\n" + "AVG Value : " + model.getAverage_value() + "," + "\n" + "Time-counter : " + model.getCounter_value() + "," + "\n" + "Date :" + model.getCurrent_date() + "," + "\n" + "Time : " + model.getSaving_time());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    @Override
    public void Savemodelitem(SavedModel model) {
        historyDao.insert(model);
    }

    @Override
    public void OndeleteSaveditem(SavedModel model, int position) {

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (drawerLayout.isOpen()){
//            drawerLayout.closeDrawers();
//        }

        // fetchData();
    }
    @Override
    public void onResume() {
        super.onResume();

        adapter.removeHistoryRecord();
        fetchData();
    }
    private void Initializatio(View view) {
        // materialToolbar = view.findViewById(topAppBar);
//        drawerLayout = view.findViewById(R.id.drawer_layout);
//        adView=view.findViewById(R.id.banner_ad);
        Emptytxt = view.findViewById(R.id.emptytxt);
//       drawer_open=view.findViewById(R.id.drawer_open);

        recyclerView = view.findViewById(R.id.recycle_view);
//        navigationView = view.findViewById(R.id.navView_drawer);
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
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
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
                break;
            case R.id.more_app:
                Toast.makeText(getActivity(), "More object clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.privacy_policy:
                Toast.makeText(getActivity(), "privacy_policy object clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.terms:
                Toast.makeText(getActivity(), "terms object clicked", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        menuItem.setChecked(true);
//        drawerLayout.closeDrawers();
    }
    public void  setUpOnBackPressed(){
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                drawerLayout.closeDrawers();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.vibration_meter);
                Fragment someFragment = new VibrationMeter();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, someFragment, "DetailedPizza");
                transaction.addToBackStack("DetailedPizza");
                transaction.commit();
            }
        });
    }
}
