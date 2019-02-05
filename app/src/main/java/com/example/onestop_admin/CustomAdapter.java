package com.example.onestop_admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

    private ArrayList<Data_model> dataSet;
    private Context context;
    static FirebaseFirestore db;
    SessionManager sessionManager;



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewtitle;
        TextView textViewSubtitle;
        TextView textViewinfo;
        ImageView imageViewdesc;
        CircleImageView imageViewdp;



        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageViewdp = (CircleImageView) itemView.findViewById(R.id.dp);
            this.textViewtitle = (TextView) itemView.findViewById(R.id.title_name);
            this.textViewSubtitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.textViewinfo = (TextView) itemView.findViewById(R.id.info);
            this.imageViewdesc = (ImageView)itemView.findViewById(R.id.image_desc);
            Log.d("CustomAdapter","FindviewbyId Done");


        }
    }

    public CustomAdapter(ArrayList<Data_model> data, Context context) {
        this.dataSet = data;
        this.context = context;
        Log.d("CustomAdapter",dataSet.get(0).getTitle());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(parent.getContext());

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        TextView textViewtitle = holder.textViewtitle;
        TextView textViewSubtitle = holder.textViewSubtitle;
        TextView textViewinfo = holder.textViewinfo;
        ImageView imageViewdesc = holder.imageViewdesc;
        ImageView imageViewdp= holder.imageViewdp;


        textViewtitle.setText(dataSet.get(listPosition).getTitle());
        textViewSubtitle.setText(dataSet.get(listPosition).getSubtitle());
        textViewinfo.setText(dataSet.get(listPosition).getDesc());

        String dpurl = dataSet.get(listPosition).getdp();
        String imageurl = dataSet.get(listPosition).getImage();

        Log.d("url", String.valueOf(dpurl.isEmpty()));

       if(!dpurl.isEmpty())
       {

           Picasso.get().load(dpurl).into(imageViewdp);
       }
       if(!imageurl.isEmpty())
       {

           Picasso.get().load(imageurl).into(imageViewdesc);
       }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}