package com.example.floppy.ui.menu.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.Domain.Entitys.FriendEntity;
import com.example.floppy.Domain.Models.Message;
import com.example.floppy.Domain.Models.User;
import com.example.floppy.utils.Animations;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ViewHolder> {
    boolean color = true;
    private static ArrayList<User>         friends;
    private static ArrayList<FriendEntity> friendEntities;
    private static ArrayList<Message>      messages;

    public AdapterChat(ArrayList<User>friends, ArrayList<FriendEntity> friendEntities) {
        AdapterChat.friends        = friends;
        AdapterChat.friendEntities = friendEntities;
        messages                   = new ArrayList<>();
        notifyItemInserted(friends.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFriendEntities(FriendEntity friendEntity, Message message) {
        int index = friendEntities.indexOf(friendEntity);
        if(index>-1){
            System.out.println("MENSAJE "+message.getMessage());
            friendEntities.remove(index);
            User user = friends.get(index);
            friends.remove(user);

            friends.add(0,user);
            friendEntities.add(0,friendEntity);
            if (messages.size() == friendEntities.size()) {
                messages.remove(message);
            }
            messages.add(0,message);
            notifyDataSetChanged();
        }
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
            holder.background.setBackgroundColor(holder.background.getContext().getResources().getColor(R.color.fondoItem1));
            color = false;
        } else {
            holder.background.setBackgroundColor(holder.background.getContext().getResources().getColor(R.color.fondoItem2));
            color = true;

        }
        if (!friends.get(position).getPhoto().equals(""))
            Glide.with(holder.itemView.getContext())
                    .load(friends.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgUser);
        if (friendEntities.get(position).nick.equals("")) {
            holder.txtName.setText(friends.get(position).getName());
        } else {
            holder.txtName.setText(friendEntities.get(position).nick);
        }

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_recicler_chats);
        holder.itemView.setAnimation(animation);
        holder.imgUser.setOnClickListener(view -> clic.clickPhoto(view, position, friends.get(position)));
        holder.itemView.setOnClickListener(view -> clic.clickChat(view, position, friends.get(position), friendEntities.get(position)));

        if(!messages.isEmpty() && messages.size() > position){
            Animations.Companion.animVanish(holder.messageImage);
            Animations.Companion.animVanish(holder.txtMessage);
            if(messages.get(position).getTypeMessage() == Message.TypesMessages.STICKER){
                Glide.with(holder.itemView.getContext())
                        .load(messages.get(position).getMessage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.messageImage);
                Animations.Companion.animAppear(holder.messageImage);
            }else{
                if(User.getInstance().getIdUser().equals(messages.get(position).getUser())){
                    holder.txtMessage.setText("tu: "+messages.get(position).getMessage());
                    if(messages.get(position).getMessage().length() >23){
                        holder.txtMessage.setText("tu: "+messages.get(position).getMessage().substring(0,23)+"...");
                    }
                }else{
                    holder.txtMessage.setText(messages.get(position).getMessage());
                    if(messages.get(position).getMessage().length() >27){

                        holder.txtMessage.setText(messages.get(position).getMessage().substring(0,27)+"...");
                    }
                }
                Animations.Companion.animAppear(holder.txtMessage);
            }
            holder.txtDate   .setText(messages.get(position).getHora());
        }

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout   background;
        RoundedImageView imgUser;
        ImageView        messageImage;
        TextView         txtName;
        TextView         txtMessage;
        TextView         txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            background   = itemView.findViewById(R.id.fontoItemChat);
            imgUser      = itemView.findViewById(R.id.imgUserItemChat);
            txtName      = itemView.findViewById(R.id.txtNombreUser);
            txtMessage   = itemView.findViewById(R.id.txtMessage);
            txtDate      = itemView.findViewById(R.id.txtDate);
            messageImage = itemView.findViewById(R.id.txtSticker);
        }
    }
}
