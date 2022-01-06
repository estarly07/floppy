package com.example.floppy.ui.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.databinding.ItemContactBinding;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemContactBinding binding = DataBindingUtil.inflate(inflater,R.layout.item_contact, parent, false);
        return new ViewHolder(binding);
    }

    public interface Click { void click(View view, int position, User user);}

    private Click click;

    public void setClick(Click click) { this.click = click; }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.fontoItemChat.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor((color)?R.color.fondoItem1:R.color.fondoItem2));
        color = !color;

        if (!users.get(position).getPhoto().equals(""))
            Glide.with(holder.binding.getRoot().getContext())
                    .load(users.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.imgUserItemChat);

        holder.bind(users.get(position));
        holder.binding.getRoot().setOnClickListener(view -> click.click(view, position, users.get(position)));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemContactBinding binding;

        public ViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(User user){ binding.setUser(user); }
    }
}
