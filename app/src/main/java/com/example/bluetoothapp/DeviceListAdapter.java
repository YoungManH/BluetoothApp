package com.example.bluetoothapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {

    class GooseHolder {
        TextView deviceName;
        TextView temp;
        TextView humi;
    }

    public DeviceListAdapter(Context context) {
        mAllDevices = new ArrayList<>();
        mContext = context;
    }

    private List<Card> mAllDevices;
    private Context mContext;

    @Override
    public int getCount() {
        return mAllDevices.size();
    }

    public void setmAllDevices(List<Card> cards) {
        mAllDevices = cards;
    }

    @Override
    public Card getItem(int position) {
        return mAllDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GooseHolder holder = new GooseHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_card, parent, false);
        }
        holder.deviceName = (TextView) convertView.findViewById(R.id.device_name_txt);
        holder.humi = (TextView) convertView.findViewById(R.id.hum_txt);
        holder.temp = (TextView) convertView.findViewById(R.id.tmp_txt);
        convertView.setTag(holder);

        Card card = mAllDevices.get(position);
        Log.i("DeviceListAdapter:", card.mDeviceName);
        holder.deviceName.setText(card.mDeviceName);
        holder.temp.setText(card.mTemp);
        holder.humi.setText(card.mHumi);
        return convertView;
    }
}
