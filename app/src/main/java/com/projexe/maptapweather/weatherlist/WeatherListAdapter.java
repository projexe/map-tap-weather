package com.projexe.maptapweather.weatherlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projexe.maptapweather.R;

import java.util.List;

/**
 * Adapter class providing the binder between the RecyclerView and the data it contains
 * @author Simon Hutton
 * @version 1.0
 */
public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private boolean mStale; // data returned from the persisted database is flagged as being stale. (not currently implemented)
    private List<List> mItems;
    private WeatherItemListener mItemListener;

    WeatherListAdapter(List<List> weatherDataList, WeatherItemListener itemListener) {
        mItems = weatherDataList;
        mItemListener = itemListener;
    }

    @Override
    public WeatherListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityWeatherView = inflater.inflate(R.layout.recycle_list_item, parent, false);

        return new ViewHolder(cityWeatherView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(WeatherListAdapter.ViewHolder holder, int position) {
        List rowItem = mItems.get(position);

        String name = (String) rowItem.get(0);
        String description = (String) rowItem.get(1);
        String tempRange = (String) rowItem.get(2);

        holder.tvCity.setText(name);
        holder.tvDescription.setText(description);
        holder.tvRange.setText(tempRange);
//        if (mStale) {
//            holder.itemView.setBackgroundColor(Color.LTGRAY);
//        } else {
//            holder.itemView.setBackgroundColor(Color.WHITE);
//        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateWeather(List<List> items, boolean staleData) {
        mStale = staleData;
        mItems = items;
        notifyDataSetChanged();
    }

    private List getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface WeatherItemListener {
        void onWeatherClick(String message);
    }

    //**********************************************************************************************
    /**
     * Viewholders keep the references in memory. When a new view is required a new Viewholder is
     * either created or it is recycled fromthe stack
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvCity;
        TextView tvDescription;
        TextView tvRange;
        WeatherItemListener mItemListener;

        ViewHolder(View itemView, WeatherItemListener weatherItemListener) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvItemCity);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvRange = itemView.findViewById(R.id.tvItemTempRange);

            this.mItemListener = weatherItemListener;
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            List item = getItem(getAdapterPosition());
            this.mItemListener.onWeatherClick(item.get(0).toString());
            notifyDataSetChanged();
        }
    }
    //**********************************************************************************************

}