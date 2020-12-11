package com.example.makcarscompany;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class CarsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


         TextView Description,status;
         ImageView imagec;
         ItemClickListner listner;
         CardView CV;

        public CarsViewHolder(View itemView) {

            super(itemView);

            imagec = itemView.findViewById(R.id.img);
            Description =  itemView.findViewById(R.id.desc);
            status=itemView.findViewById(R.id.sts);
            CV=itemView.findViewById(R.id.cv);

        }

        public void setItemClickListner(ItemClickListner listner) {

            this.listner = listner;
        }

        @Override
        public void onClick(View view) {

            listner.onClick(view, getAdapterPosition(), false);
        }
    }











