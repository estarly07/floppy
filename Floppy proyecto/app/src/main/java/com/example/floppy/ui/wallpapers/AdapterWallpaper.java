package com.example.floppy.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floppy.R;
import com.example.floppy.databinding.ItemWallpaperBinding;

import java.util.ArrayList;

public class AdapterWallpaper extends RecyclerView.Adapter<AdapterWallpaper.Holder> {

    private ArrayList<Integer> wallpapersDefaults;

    public void setWallpapersDefaults(ArrayList<Integer> wallpapersDefaults) {
        this.wallpapersDefaults = wallpapersDefaults;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        return new Holder(DataBindingUtil.inflate(inflater, R.layout.item_wallpaper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.binding.img.setImageDrawable(holder.binding.getRoot().getContext().getDrawable(wallpapersDefaults.get(position)));
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
