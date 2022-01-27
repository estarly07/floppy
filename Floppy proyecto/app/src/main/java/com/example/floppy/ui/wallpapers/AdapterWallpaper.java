package com.example.floppy.ui.wallpapers;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.databinding.ItemWallpaperBinding;

import java.util.ArrayList;

public class AdapterWallpaper extends RecyclerView.Adapter<AdapterWallpaper.Holder> {

    private ArrayList<Object> wallpapersDefaults = new ArrayList<>();
    private Boolean isDefault;

    public void setWallpapersDefaults(ArrayList<Object> wallpapersDefaults,Boolean isDefault) {
        this.wallpapersDefaults = wallpapersDefaults;
        this.isDefault = isDefault;
        notifyDataSetChanged();
    }
    public void cleanData(){
        wallpapersDefaults.clear();
    }
    public void setWallpapersDefaults(Object wallpaper,Boolean isDefault) {
        this.wallpapersDefaults.add(wallpaper);
        this.isDefault = isDefault;
        notifyItemInserted(wallpapersDefaults.size());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new Holder(DataBindingUtil.inflate(inflater, R.layout.item_wallpaper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(isDefault){
            holder.binding.img.setVisibility(View .VISIBLE);
            holder.binding.img2.setVisibility(View.GONE);
            holder.binding.img.setImageDrawable(holder.binding.getRoot().getContext().getDrawable((Integer) wallpapersDefaults.get(position)));
        }else{


            holder.binding.img.setVisibility(View .GONE);
            holder.binding.img2.setVisibility(View.VISIBLE);
//            holder.binding.img.setImageBitmap((Bitmap) wallpapersDefaults.get(position));
            Glide.with(holder.binding.getRoot().getContext())
                    .load(wallpapersDefaults.get(position))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.img2);
        }
    }

    @Override
    public int getItemCount() {
        return wallpapersDefaults.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ItemWallpaperBinding binding;

        public Holder(ItemWallpaperBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
