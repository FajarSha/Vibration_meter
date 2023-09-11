package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.AdapterListener;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.SavedModel;
import com.github.anastr.speedviewlib.Speedometer;

import java.util.ArrayList;
import java.util.List;

public class RecylceSaveAdapter extends RecyclerView.Adapter<RecylceSaveAdapter.ViewHolder> {
    private Context context;
    private AdapterListener adapterListener;
    private List<SavedModel> savedModelList;

    RecylceSaveAdapter(Context context, AdapterListener adapterListener) {
        this.context = context;
        this.adapterListener = adapterListener;
        savedModelList = new ArrayList<>();
    }

    public void addhistoryRecord(SavedModel model) {
        savedModelList.add(model);
        notifyDataSetChanged();
    }

    /*   @NonNull
       @Override
       public RecylceSaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(context).inflate(R.layout.saved_item_list, parent, false);
           ViewHolder viewHolder=new ViewHolder(view);
           return viewHolder;
       }*/
    @NonNull
    @Override
    public RecylceSaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_item_list, parent, false);
        RecylceSaveAdapter.ViewHolder viewHolder = new RecylceSaveAdapter.ViewHolder(view);
//     Log.d("View holder","View Holder of saved recycle view Is "+ viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecylceSaveAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        SavedModel model = savedModelList.get(position);
        holder.Timetxt.setText(model.getSaving_time().toString());
        holder.Datetxt.setText(model.getCurrent_date().toString());
        holder.AVGtxt.setText(model.getAverage_value().toString());
        holder.Peaktxt.setText(model.getPeak_value().toString());
        holder.TimeCounterTxt.setText(model.getCounter_value().toString());
        holder.Accelerationtxt.setText(model.getPeak_value().toString());
        holder.speedometer.setSpeedAt(model.getPeak_value().floatValue());
        holder.Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterListener.OndeleteSaveditem(model, holder.getAdapterPosition());
                Toast.makeText(context,"Item deleted",Toast.LENGTH_SHORT).show();
            }
        });
        holder.ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Peak value : " + model.getPeak_value() + "," + "\n" + "AVG Value : " + model.getAverage_value() + "," + "\n" + "Time-counter : " + model.getCounter_value() + "," + "\n" + "Date :" + model.getCurrent_date() + "," + "\n" + "Time : " + model.getSaving_time());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
               context.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return savedModelList.size();
    }

    public void removeHistoryRecord() {
        savedModelList.removeAll(savedModelList);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Datetxt, Timetxt, Peaktxt, TimeCounterTxt, AVGtxt, Accelerationtxt;
        ImageView Deletebtn, ShareBtn;
        Speedometer speedometer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Datetxt = itemView.findViewById(R.id.datetxt);
            Timetxt = itemView.findViewById(R.id.timetxt);
            Peaktxt = itemView.findViewById(R.id.peakvalue);
            TimeCounterTxt = itemView.findViewById(R.id.timeCounter);
            AVGtxt = itemView.findViewById(R.id.AVGvalue);
            Accelerationtxt = itemView.findViewById(R.id.acceleration);
            Deletebtn = itemView.findViewById(R.id.deletebtn);
            ShareBtn = itemView.findViewById(R.id.sharebtn);
            speedometer = itemView.findViewById(R.id.speedView);

        }

    }

    public void RemoveSavedhistory(int position) {
        savedModelList.remove(position);
        notifyDataSetChanged();

    }
}
