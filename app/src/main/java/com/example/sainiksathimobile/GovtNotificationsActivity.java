package com.example.sainiksathimobile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GovtNotificationsActivity extends AppCompatActivity {

    private ListView notificationList;
    private SearchView searchNotification;
    private ArrayAdapter<String> adapter;
    private List<String> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_notifications);

        notificationList = findViewById(R.id.notificationList);
        searchNotification = findViewById(R.id.searchNotification);

        notifications = Arrays.asList(
                "🛡️ New pension scheme for widows of gallantry award winners",
                "🏥 Military hospital upgrades announced in South India",
                "📄 Update Aadhaar & PAN by July 31 to avoid penalties",
                "🧠 Mental health support helpline for veterans launched",
                "📢 Sainik welfare department releases new housing benefits"
        );

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notifications);
        notificationList.setAdapter(adapter);

        searchNotification.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
