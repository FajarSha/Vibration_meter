package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.AdapterListener;
import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom.SavedModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder> {
    private Context context;
    private List<HistoryModel> historyModels;
    ArrayList<HistoryModel> arrhistory;
    private AdapterListener adapterListener;

    RecyclerHistoryAdapter(Context context, AdapterListener listener) {
        this.context = context;
        this.adapterListener = listener;
        historyModels = new ArrayList<>();

    }

    public void removeHistoryRecord() {
        historyModels.removeAll(historyModels);
        notifyDataSetChanged();
    }

    public void addhistoryRecord(HistoryModel model) {
        historyModels.add(model);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        position=holder.getAdapterPosition();
        HistoryModel modelhistory = historyModels.get(position);
        holder.Datetxt.setText(modelhistory.getCurrent_date());
        holder.Timetxt.setText(modelhistory.getSaving_time());
        holder.Countertxt.setText(modelhistory.getCounter_value());
        holder.peaktxt.setText(modelhistory.getPeak_value().toString());
        holder.averagetxt.setText(modelhistory.getAverage_value().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterListener.OnitemDelete(modelhistory);
            }
        });
        holder.Morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.Morebtn);
                popup.inflate(R.menu.item_menu);
               // adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.save_btn:
                                SavedModel model=new SavedModel(0,modelhistory.getCurrent_date(),modelhistory.getSaving_time(),modelhistory.getCounter_value()
                                , modelhistory.getPeak_value(), modelhistory.getAverage_value());
                                adapterListener.Savemodelitem(model);
                                Toast.makeText(context,"Saved item ",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.sharebtn:
                                adapterListener.OnitemSahre(modelhistory);
                                return true;
                            case R.id.delete_btn:
                                adapterListener.OnDelete(modelhistory.getId(),holder.getAdapterPosition());
                             return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Datetxt, Timetxt, Countertxt, peaktxt, averagetxt;
        ImageView Morebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            Datetxt = itemView.findViewById(R.id.datevalue);
            Timetxt = itemView.findViewById(R.id.timevalue);
            Countertxt = itemView.findViewById(R.id.timestampvalue);
            peaktxt = itemView.findViewById(R.id.peakvalue);
            averagetxt = itemView.findViewById(R.id.avgvalue);
            Morebtn=itemView.findViewById(R.id.more);

        }
    }

    public void RemoveSavedhistory(int position) {
        historyModels.remove(position);
        notifyDataSetChanged();

    }




}
