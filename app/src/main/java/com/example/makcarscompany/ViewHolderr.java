package com.example.makcarscompany;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderr extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView Description;
    CardView C;

    ItemClickListner listner;

    public ViewHolderr(View itemView) {

        super(itemView);

        Description =  itemView.findViewById(R.id.desc);
        C=itemView.findViewById(R.id.cv);

    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);
    }

}
