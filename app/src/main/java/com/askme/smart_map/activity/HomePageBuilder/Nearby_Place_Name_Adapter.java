package com.askme.smart_map.activity.HomePageBuilder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;


import com.askme.mapmyday.R;

import java.util.List;


public class Nearby_Place_Name_Adapter extends RecyclerView.Adapter<Nearby_Place_Name_Adapter.ContactViewHolder> {

    private Context context;

    private List<Nearby_Place_Name_Info> ne;
    private ClickListener clickListener;


    public Nearby_Place_Name_Adapter(Context context, List<Nearby_Place_Name_Info> contactlist) {
        this.context = context;
        this.ne = contactlist;
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {

        Nearby_Place_Name_Info ci = ne.get(i);
        contactViewHolder.vTitle.setText(ci.name);
        contactViewHolder.vImage.setImageResource(ci.image);
    }

    @Override
    public int getItemCount() {
        return ne.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void itemClicked(View clickedView, int position);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Animation.AnimationListener {
        protected TextView vTitle;
        protected ImageView vImage;

        public ContactViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            vTitle = (TextView) itemView.findViewById(R.id.title);
            vImage = (ImageView) itemView.findViewById(R.id.select_image);


        }


        @Override
        public void onClick(View view) {

            if (clickListener != null) {
                clickListener.itemClicked(view, getPosition());
            }
        }


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}