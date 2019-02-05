package com.example.onestop_admin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUploadsActivity extends AppCompatActivity {

    //the listview
    ListView listView;

    //database reference to get uploads data
    CollectionReference mDatabaseReference;
    private SimpleAdapter sa;
    //list to store uploads data
    List<Upload> uploadList;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_uploads);
        getSupportActionBar().setTitle("Latest Menus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        uploadList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);


        //adding a clicklistener on listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Upload upload = uploadList.get(i);

                //Opening the upload file in browser using the upload url
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(upload.getUrl()));
//                startActivity(intent);
            }
        });


        //getting the database reference
//        mDatabaseReference = FirebaseFirestore.getInstance().collection(Constants.DATABASE_PATH_UPLOADS);
//        mDatabaseReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                        Log.i("nikhil",documentSnapshot.getId() + "=> " + documentSnapshot.getData().toString());
//
//                        uploadList.add(new Upload("Hostel",documentSnapshot.getData().get("url").toString()));
//                    }
//                    String[] uploads = new String[uploadList.size()];
//
//                    for (int i = 0; i < uploads.length; i++) {
//                        uploads[i] = uploadList.get(i).gethostel();
//                    }
//
//                    //displaying it to list
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, uploads);
//                    listView.setAdapter(adapter);
//                }
//            }
//        });

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference codesRef = rootRef.collection(Constants.DATABASE_PATH_UPLOADS).document("Months");
        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> map = task.getResult().getData();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        uploadList.add(new Upload(entry.getKey(),entry.getValue().toString()));
                        Log.d("TAG", entry.getKey());
                    }
                    String[] uploads = new String[uploadList.size()];
                    HashMap<String,String> item;
                    for(int i=0;i<uploads.length;i++){
                        item = new HashMap<String,String>();
                        item.put( "line1", uploadList.get(i).gethostel());
                        item.put( "line2", uploadList.get(i).getmonth());
                        list.add( item );
                    }
                    sa = new SimpleAdapter(ViewUploadsActivity.this, list,
                            R.layout.simple_list,
                            new String[] { "line1","line2" },
                            new int[] {R.id.textView1, R.id.textView2});
                    ((ListView)findViewById(R.id.listview)).setAdapter(sa);
                    //Do what you want to do with your list
                }
            }
        });




//        //retrieving upload data from firebase database
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    uploadList.add(upload);
//                }
//
//                String[] uploads = new String[uploadList.size()];
//
//                for (int i = 0; i < uploads.length; i++) {
//                    uploads[i] = uploadList.get(i).getName();
//                }
//
//                //displaying it to list
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
//                listView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}