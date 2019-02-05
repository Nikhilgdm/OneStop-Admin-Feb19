package com.example.onestop_admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MessMainActivity extends AppCompatActivity implements View.OnClickListener {

    final int PICK_PDF_CODE = 2342;

    TextView textViewStatus;
    EditText editTextFilename;
    ProgressBar progressBar;
    String Hostel = "BARAK",Month = "JANUARY";
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    CollectionReference collectionReference;
    Boolean pdfexits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_main);


        mStorageReference = FirebaseStorage.getInstance().getReference();
        collectionReference = FirebaseFirestore.getInstance().collection("MessMenuURLs");
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        final NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("BARAK", "BRAHMAPUTRA", "DHANSIRI", "DIBANG", "DIHING","KAMENG","KAPILI","LOHIT", "MANAS", "SIANG", "SUBHANSIRI","UMIAM"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Hostel = "BARAK";
                        break;
                    case 1:
                        Hostel = "BRAHMAPUTRA";
                        break;
                    case 2:
                        Hostel = "DHANSIRI";
                        break;
                    case 3:
                        Hostel = "DIBANG";
                        break;
                    case 4:
                        Hostel = "DIHING";
                        break;
                    case 5:
                        Hostel = "KAMENG";
                        break;
                    case 6:
                        Hostel = "KAPILI";
                        break;
                    case 7:
                        Hostel = "LOHIT";
                        break;
                    case 8:
                        Hostel = "MANAS";
                        break;
                    case 9:
                        Hostel = "SIANG";
                        break;
                    case 10:
                        Hostel = "SUBHANSIRI";
                        break;
                    case 11:
                        Hostel = "UMIAM";
                        break;

                    default:
                        //  url="no match";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final NiceSpinner niceSpinner_month = (NiceSpinner) findViewById(R.id.nice_spinner_month);
        List<String> dataset_month = new LinkedList<>(Arrays.asList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY","JUNE","JULY","AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER","DECEMBER"));
        niceSpinner_month.attachDataSource(dataset_month);
        niceSpinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Month = "JANUARY";
                        break;
                    case 1:
                        Month = "FEBRUARY";
                        break;
                    case 2:
                        Month = "MARCH";
                        break;
                    case 3:
                        Month = "APRIL";
                        break;
                    case 4:
                        Month = "MAY";
                        break;
                    case 5:
                        Month = "JUNE";
                        break;
                    case 6:
                        Month = "JULY";
                        break;
                    case 7:
                        Month = "AUGUST";
                        break;
                    case 8:
                        Month = "SEPTEMBER";
                        break;
                    case 9:
                        Month = "OCTOBER";
                        break;
                    case 10:
                        Month = "NOVEMBER";
                        break;
                    case 11:
                        Month = "DECEMBER";
                        break;

                    default:
                        //  url="no match";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });









        findViewById(R.id.buttonUploadFile).setOnClickListener(this);
        findViewById(R.id.textViewUploads).setOnClickListener(this);
    }




    private void getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }


        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void uploadFile(Uri data) {
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference sRef = mStorageReference.child("MessMenus/" + Hostel + "-"+Month+".pdf");
        sRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("nikhil", Hostel+".pdf Deleted");
                pdfexits =true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("nikhil", Hostel+".pdf Does not exists");
                pdfexits=false;
            }
        });
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");

//                        Upload upload = new Upload(editTextFilename.getText().toString(), sRef.getDownloadUrl().toString());


                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String,Object> newurl = new HashMap<>();
                                newurl.put(Hostel,uri.toString());

                                collectionReference.document("URLs").set(newurl, SetOptions.merge());
                                Map<String,Object> month = new HashMap<>();
                                month.put(Hostel,Month);
                                collectionReference.document("Months").set(month, SetOptions.merge());

                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        textViewStatus.setText((int) progress + "% Uploading...");
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUploadFile:
                getPDF();
                break;
            case R.id.textViewUploads:
                startActivity(new Intent(this, ViewUploadsActivity.class));
                break;
        }
    }
}