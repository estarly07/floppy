package com.example.floppy.ui.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.Domain.Entitys.StickersEntity;
import com.example.floppy.R;

import java.util.ArrayList;

public class AdapterSticker extends RecyclerView.Adapter<AdapterSticker.ViewHolder> {
    private ArrayList<StickersEntity> listStickers;

    public interface Click{
        void click(View view,StickersEntity stickersEntity);
    }
    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public AdapterSticker(ArrayList<StickersEntity> listStickers) {
        this.listStickers = listStickers;
        notifyItemInserted(listStickers.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(listStickers.get(position).urlImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( holder.sticker);

        holder.itemView.setOnClickListener(view -> {
            if(click != null){click.click(view,listStickers.get(position));}
        });
    }

    @Override
    public int getItemCount() { return listStickers.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sticker = itemView.findViewById(R.id.imgSticker);
        }
    }
}
