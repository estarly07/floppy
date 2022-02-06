package com.example.floppy.ui.menu.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.databinding.ItemChatBinding;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.models.StateMessage;
import com.example.floppy.domain.models.User;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.Extensions;

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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       ItemChatBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.item_chat, parent, false);
        return new ViewHolder(binding);
    }

    public interface Click {
        void clickPhoto(View view, int position, User friend);

        void clickChat(View view, int position, User friend,FriendEntity friendEntity);
    }

    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.fontoItemChat.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor((color)? R.color.fondoItem1:R.color.fondoItem2));
        color = !color;

        if (!friends.get(position).getPhoto().equals("")){
            Glide.with(holder.itemView.getContext())
                    .load(friends.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.imgUserItemChat);}
        else{
            holder.binding.imgUserItemChat.setImageDrawable
                    (holder.binding.getRoot().getResources().getDrawable(R.drawable.ic_sinuser));
        }

        holder.binding.txtNombreUser.setText(
                (friendEntities.get(position).nick.equals(""))?
                friends.get(position).getName():friendEntities.get(position).nick);

        holder.binding.imgUserItemChat.setOnClickListener(view -> click.clickPhoto(view, position, friends.get(position)));
        holder.binding.getRoot().setOnClickListener(view -> click.clickChat(view, position, friends.get(position), friendEntities.get(position)));
        holder.binding.imgCheck.setVisibility(View.GONE);
        if(!messages.isEmpty() && messages.size() > position){
            Animations.Companion.animVanish(holder.binding.txtSticker);
            Animations.Companion.animVanish(holder.binding.txtMessage);
            holder.binding.txtDate.setText(messages.get(position).getHora());

            switch (messages.get(position).getTypeMessage()){
                case RECORD:{
                    holder.binding.txtSticker.setImageResource(R.drawable.ic_bmic);
                    Animations.Companion.animAppear(holder.binding.txtSticker);}
                    break;
                case STICKER:{
                    Glide.with(holder.itemView.getContext())
                            .load(messages.get(position).getMessage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.binding.txtSticker);
                    Animations.Companion.animAppear(holder.binding.txtSticker);}
                    break;
                case TEXT: {
                    if(User.getInstance().getIdUser().equals(messages.get(position).getUser())){
                        holder.binding.txtMessage.setText(messages.get(position).getMessage());
                        Animations.Companion.animAppear(holder.binding.imgCheck);
                        Extensions.Companion.changeDoubleCheckColor(
                                holder.binding.imgCheck,
                                messages.get(position).getState() == StateMessage.CHECK);
                        if(messages.get(position).getMessage().length() >27){
                            holder.binding.txtMessage.setText(messages.get(position).getMessage().substring(0,27)+"...");
                        }
                    }else{
                        holder.binding.txtMessage.setText(messages.get(position).getMessage());
                        if(messages.get(position).getMessage().length() >27){
                            holder.binding.txtMessage.setText(messages.get(position).getMessage().substring(0,27)+"...");
                        }
                    }
                    Animations.Companion.animAppear(  holder.binding.txtMessage);
                }
                break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemChatBinding binding ;
        public ViewHolder( ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
