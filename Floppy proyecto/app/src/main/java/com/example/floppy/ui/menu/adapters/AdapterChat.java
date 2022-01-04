package com.example.floppy.ui.menu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ViewHolder> {
    boolean color = true;
    private ArrayList<User>         friends;
    private ArrayList<FriendEntity> friendEntities;

    public AdapterChat(ArrayList<User>friends, ArrayList<FriendEntity> friendEntities) {
        this.friends        = friends;
        this.friendEntities = friendEntities;
        notifyItemInserted(friends.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    public interface Clic {
        void clickPhoto(View view, int position, User friend);

        void clickChat(View view, int position, User friend,FriendEntity friendEntity);
    }

    private Clic clic;

    public void setClick(Clic clic) {
        this.clic = clic;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (color) {
            holder.fondo.setBackgroundColor(holder.fondo.getContext().getResources().getColor(R.color.fondoItem1));
            color = false;
        } else {
            holder.fondo.setBackgroundColor(holder.fondo.getContext().getResources().getColor(R.color.fondoItem2));
            color = true;

        }
        if (!friends.get(position).getPhoto().equals(""))
            Glide.with(holder.itemView.getContext())
                    .load(friends.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgUser);
        if (friendEntities.get(position).nick.equals("")) {
            holder.txtNombre.setText(friends.get(position).getName());
        } else {
            holder.txtNombre.setText(friendEntities.get(position).nick);
        }

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_recicler_chats);
        holder.itemView.setAnimation(animation);
        holder.imgUser.setOnClickListener(view -> {
            clic.clickPhoto(view, position, friends.get(position));
        });
        holder.itemView.setOnClickListener(view -> {
            clic.clickChat(view, position, friends.get(position), friendEntities.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout fondo;
        RoundedImageView imgUser;
        TextView txtNombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fondo = itemView.findViewById(R.id.fontoItemChat);
            imgUser = itemView.findViewById(R.id.imgUserItemChat);
            txtNombre = itemView.findViewById(R.id.txtNombreUser);
        }
    }
}
