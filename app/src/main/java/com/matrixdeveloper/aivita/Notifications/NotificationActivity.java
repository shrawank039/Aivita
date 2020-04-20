package com.matrixdeveloper.aivita.Notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;
import com.matrixdeveloper.aivita.model.NotificationModel;


import com.matrixdeveloper.aivita.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String NOTIFICATIONLIST_URL = "https://infinityfacts.com/aivita/API/index.php?p=notification";
    private JsonArrayRequest request;
    private RequestQueue queue;
    private ImageView back;
    private List<NotificationModel> fdtNtificationList;
    private RecyclerView recyclerViewNotification;
    private NotificationAdapter notificationAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        back = findViewById(R.id.back_not);
        recyclerViewNotification = findViewById(R.id.recyclerNotification);
        fdtNtificationList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        jsonrequest();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void jsonrequest() {
        swipeRefreshLayout.setRefreshing(true);
        request = new JsonArrayRequest(NOTIFICATIONLIST_URL, response -> {
            JSONObject jsonObject = null;
            Log.e("response", response.toString());
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setNotificationTitle(jsonObject.getString("notification_title"));
                    notificationModel.setDescription(jsonObject.getString("description"));
                    notificationModel.setView(jsonObject.getString("view"));
                    notificationModel.setGif(jsonObject.getString("gif"));
                    notificationModel.setCreated(jsonObject.getString("created"));
                    fdtNtificationList.add(notificationModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            setuprecyclerview(fdtNtificationList);
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fb_id", Variables.u_id);
                return params;
            }
        };
        queue = Volley.newRequestQueue(NotificationActivity.this);
        queue.add(request);
    }

    private void setuprecyclerview(List<NotificationModel> fstAdminLeadList) {
        notificationAdapter = new NotificationAdapter(this, fstAdminLeadList);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotification.setLayoutFrozen(true);
        recyclerViewNotification.setAdapter(notificationAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        jsonrequest();
        fdtNtificationList.clear();
    }
}