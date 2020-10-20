package com.siamin.fivestart.reminder.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.reminder.adapters.ReminderAdapter;
import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.models.Reminder;

import java.util.List;


public class TabFragment extends Fragment {

    RecyclerView recyclerView;
    TextView emptyText;
    LinearLayout linearLayout;
    ImageView imageView;

    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;
    private int remindersType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        initView(view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        remindersType = this.getArguments().getInt("TYPE");
        if (remindersType == Reminder.INACTIVE) {
            emptyText.setText(R.string.no_inactive);
            imageView.setImageResource(R.drawable.ic_notifications_off_black_empty);
        }

        reminderList = getListData();
        reminderAdapter = new ReminderAdapter(getContext(), reminderList);
        recyclerView.setAdapter(reminderAdapter);

        if (reminderAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyText = view.findViewById(R.id.empty_text);
        linearLayout = view.findViewById(R.id.empty_view);
        imageView = view.findViewById(R.id.empty_icon);

    }
    public List<Reminder> getListData() {
        DatabaseHelper database = DatabaseHelper.getInstance(getContext().getApplicationContext());
        List<Reminder> reminderList = database.getNotificationList(remindersType);
        database.close();
        return reminderList;
    }

    public void updateList() {
        reminderList.clear();
        reminderList.addAll(getListData());
        reminderAdapter.notifyDataSetChanged();

        if (reminderAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateList();
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, new IntentFilter("BROADCAST_REFRESH"));
        updateList();
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
        super.onPause();
    }
}