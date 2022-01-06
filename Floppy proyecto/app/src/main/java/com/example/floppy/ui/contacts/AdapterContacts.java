package com.example.floppy.ui.contacts;

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
import com.example.floppy.domain.models.User;
import com.example.floppy.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.ViewHolder> {
    boolean color = true;
    private ArrayList<User> users;

    public AdapterContacts(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    public interface Click { void click(View view, int position, User user);}

    private Click click;

    public void setClick(Click click) { this.click = click; }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (color) {
            holder.background.setBackgroundColor(holder.background.getContext().getResources().getColor(R.color.fondoItem1));
            color = false;
        } else {
            holder.background.setBackgroundColor(holder.background.getContext().getResources().getColor(R.color.fondoItem2));
            color = true;

        }
        if (!users.get(position).getPhoto().equals(""))
            Glide.with(holder.itemView.getContext())
                    .load(users.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgUser);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_recicler_chats);

        holder.txtName.setText(users.get(position).getName());
        holder.itemView.setAnimation(animation);
        holder.itemView.setOnClickListener(view -> click.click(view, position, users.get(position)));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout   background;
        RoundedImageView imgUser;
        TextView         txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.fontoItemChat);
            imgUser    = itemView.findViewById(R.id.imgUserItemChat);
            txtName    = itemView.findViewById(R.id.txtNombreUser);
        }
    }
}
