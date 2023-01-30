package com.example.firebase3;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {

    Context context;
    ArrayList<Model> arrayList;
    Myadapter(Context context ,ArrayList<Model> arrayList){
        this.context =context;
        this.arrayList =arrayList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name ,phone_no,github,description;
        CircleImageView Dp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.nametext);
            phone_no=itemView.findViewById(R.id.phonetext);
            github=itemView.findViewById(R.id.githubtext);
            description=itemView.findViewById(R.id.destext);
            Dp=itemView.findViewById(R.id.profilePic);
        }
    }

    @NonNull
    @Override
    public Myadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=  LayoutInflater.from(context).inflate(R.layout.text_row_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myadapter.ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).Name);
        holder.phone_no.setText(arrayList.get(position).Phone_no);
        holder.github.setText(arrayList.get(position).Github);
        holder.description.setText(arrayList.get(position).Description);
        Picasso.get().load(arrayList.get(position).getImgUrl()).fit().centerCrop().into(holder.Dp);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
