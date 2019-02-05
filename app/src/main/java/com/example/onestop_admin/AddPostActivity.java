package com.example.onestop_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    public static RecyclerView recyclerView;
    public static ArrayList<Data_model> data1;
    static FirebaseFirestore db;
    RecyclerView.Adapter adapter;
    static ArrayList<Map<String, Object>> feedList;
    TextView login ;
    SwipeRefreshLayout swipeRefreshLayout_feed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView.LayoutManager layoutManager;
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_feed);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(AddPostActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data1 = new ArrayList<Data_model>();
        db = FirebaseFirestore.getInstance();

        Log.d("feed", "ref" + db);

        feedList = new ArrayList<>();

//                more = (Button) findViewById(R.id.more);
        swipeRefreshLayout_feed =findViewById(R.id.swiperefresh_feed);
        setFeedRecyclerview();





        swipeRefreshLayout_feed.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        setFeedRecyclerview();
                        Toast.makeText(AddPostActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();

                    }
                }
        );


//                for (int i = 0; i < MyData.titleArray.length; i++) {
//                    data.add(new Data_model(
//                            MyData.titleArray[i],
//                            MyData.subtitleArray[i],
//                            MyData.descArray[i],
//                            MyData.dpArray[i],
//                            MyData.imageArray[i]
//                    ));
//                }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewPostActivity.class));
            }
        });
    }


    private void setFeedRecyclerview() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference feedRef = rootRef.collection("feed");
        Query firstQuery = feedRef.orderBy("time", Query.Direction.DESCENDING);
        firstQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    data1.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        Map singleData = document.getData();

                        data1.add(new Data_model(
                                singleData.get("title").toString(),
                                singleData.get("subtitle").toString(),
                                singleData.get("desc").toString(),
                                singleData.get("dp").toString(),
                                singleData.get("image").toString(),
                                document.getId()
                        ));

                        Log.d("Data Size", String.valueOf(data1.size()));


//                                feedList.add(document.getData().);
                        //Toast.makeText(Main2Activity.this, document.getData().toString(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.d("test", "error");
                }
                adapter = new CustomAdapter(data1, AddPostActivity.this);
                recyclerView.removeAllViews();
                recyclerView.setAdapter(adapter);


            }
        });
        swipeRefreshLayout_feed.setRefreshing(false);
    }

}
