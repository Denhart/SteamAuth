package dk.denhart.steamauther.steamauth;

/**
 * Created by DenPC on 22-12-2015.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class CustomListAdapter extends BaseAdapter {

    private ArrayList<User> userData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<User> userData) {
        this.userData = userData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userData.size();
    }

    @Override
    public Object getItem(int position) {
        return userData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(ArrayList<User> userData) {
        this.userData.clear();
        this.userData.addAll(userData);
        this.notifyDataSetChanged();
    }

    public void updateCodes() {
        this.notifyDataSetChanged();
    }

    public void deleteItem(int id) {

        this.userData.remove(id);
        this.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.steamCodeView = (TextView) convertView.findViewById(R.id.steamCode);
            holder.accountNameView = (TextView) convertView.findViewById(R.id.accountName);
            holder.circularProgressBar = (CircularProgressBar) convertView.findViewById(R.id.yourCircularProgressbar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.steamCodeView.setText(userData.get(position).getsteamCode());
        holder.accountNameView.setText("For: " + userData.get(position).getaccountName());

        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        String currentDateandTime = sdf.format(new Date());
        int seconds=Integer.parseInt(currentDateandTime)%30;
        holder.circularProgressBar.setProgress((float) (3.34 * seconds));
        int animationDuration = (30-seconds)*1000; // 2500ms = 2,5s
        holder.circularProgressBar.setProgressWithAnimation(100, animationDuration); // Default duration = 1500ms
        return convertView;
    }

    static class ViewHolder {
        TextView steamCodeView;
        TextView accountNameView;
        CircularProgressBar circularProgressBar;
    }
}