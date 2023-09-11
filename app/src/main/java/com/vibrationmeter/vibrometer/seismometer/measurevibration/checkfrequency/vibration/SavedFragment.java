package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import static com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.R.layout.fragment_saved;

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

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.anastr.speedviewlib.Speedometer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.AdapterListener;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.HistoryDao;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.HistoryDatabase;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.SavedModel;

import java.util.List;

public class SavedFragment extends Fragment implements AdapterListener {

    ConstraintLayout layout;
    RecyclerView recyclerView;
//    ImageView drawerOpen_btn;
    TextView emptytxt;
    LottieAnimationView lottie;
//    DrawerLayout drawerLayout;
//    NavigationView nav;

    private static Bundle mybundle = new Bundle();
    private HistoryModel historyModel;
    private static HistoryModel historyrecord;
    private HistoryDatabase historyDatabase;
    private HistoryDao historyDao;
    Speedometer speedometer;
    private RecylceSaveAdapter savedadapter;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(fragment_saved, null);
        //lottie = view.findViewById(R.id.lottie_Wave);
        historyDatabase = HistoryDatabase.getInstance(getContext());
        historyDao = historyDatabase.getDao();
        initialization(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedadapter = new RecylceSaveAdapter(getContext(), this);
        recyclerView.setAdapter(savedadapter);
//        nav = (NavigationView) view.findViewById(R.id.navView_drawer);
//        setupDrawerContent(nav);
//        setUpOnBackPressed();
//        drawerOpen_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(nav);
//            }
//        });
        MobileAds.initialize(getContext());
        AdRequest adRequest = new AdRequest.Builder().build();
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (drawerLayout.isOpen()){
//            drawerLayout.closeDrawers();
//        }
    }

    public void initialization(View view) {
        emptytxt = view.findViewById(R.id.emptytxt);
        recyclerView = view.findViewById(R.id.Saved_recylceview);
//        accelerationtxt = view.findViewById(R.id.acceleration);
//        drawerOpen_btn = view.findViewById(R.id.drawer_open);
//        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        nav = (NavigationView) view.findViewById(R.id.navView_drawer);

    }
    @Override
    public void onResume() {
        super.onResume();
      //
        //  ;
        savedadapter.removeHistoryRecord();
        fetchsavedData();
    }

    @Override
    public void OnUpdate(int id, int pos) {

    }

    @Override
    public void OnDelete(int id, int pos) {
        historyDao.delete(id);
    }

    @Override
    public void OnitemDelete(HistoryModel model) {

    }

    @Override
    public void OnitemSahre(HistoryModel model) {

    }

    @Override
    public void Savemodelitem(SavedModel model) {

    }

    @Override
    public void OndeleteSaveditem(SavedModel model, int position) {
        historyDao.deleteSaveditem(model.getId());
        savedadapter.RemoveSavedhistory(position);
    }

    private void fetchsavedData() {
        List<SavedModel> savedmodellist = historyDao.getAllSaved();
        Log.d(" Saved model total value", "saved model item = " + savedmodellist.size());
        for (int i = 0; i < savedmodellist.size(); i++) {
            SavedModel savedmodel = savedmodellist.get(i);
            savedadapter.addhistoryRecord(savedmodel);
            savedadapter.notifyDataSetChanged();
        }
        if (savedmodellist.size() <= 0) {
            emptytxt.setVisibility(View.VISIBLE);
        } else {
            emptytxt.setVisibility(View.GONE);
        }
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
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
        switch (menuItem.getItemId()) {
            case R.id.shareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Go To Play Store For Download this App " + "\n\n" + "https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "Go To Play Store For Download this App " + "\n\n" + "https://play.google.com/store/apps/developer?id=Soons+Valley");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                // fragmentClass = FirstFragment.class;
                break;
            case R.id.rate_us:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));

               // Toast.makeText(getActivity(), "Second object clicked", Toast.LENGTH_SHORT).show();
                /// fragmentClass = SecondFragment.class;
                break;
            case R.id.more_app:
               GoToLink("https://play.google.com/store/apps/developer?id=Soons+Valley");
                break;
            case R.id.privacy_policy:
                GoToLink("https://sites.google.com/view/soons-valley/home");
                break;
            case R.id.terms:
                GoToLink("https://sites.google.com/view/soons-valley/home");
                break;
            default:
                //fragmentClass = FirstFragment.class;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
//        setTitle(menuItem.getTitle());
        // Close the navigation drawer
//        drawerLayout.closeDrawers();
    }

    public void setUpOnBackPressed() {
       // drawerLayout=(DrawerLayout) getView().findViewById(R.id.drawer_layout);

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
    public void GoToLink(String link){
        Intent viewIntent =
        new Intent("android.intent.action.VIEW",
                Uri.parse(link));
        startActivity(viewIntent);
    }

}