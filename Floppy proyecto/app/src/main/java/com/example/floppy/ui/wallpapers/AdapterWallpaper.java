package com.example.floppy.ui.wallpapers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.floppy.R;
import com.example.floppy.databinding.ItemWallpaperBinding;
import com.example.floppy.ui.Chat.ChatActivity;

import java.util.ArrayList;

public class AdapterWallpaper extends RecyclerView.Adapter<AdapterWallpaper.Holder> {

    private ArrayList<String> wallpapersDefaults = new ArrayList<>();
    private Boolean isDefault;
    RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(500, 500)
            .centerCrop();

    public interface  Click{
        void click(String background,View view);
    }
    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public void setWallpapersDefaults(ArrayList<String> wallpapersDefaults, Boolean isDefault) {
        this.wallpapersDefaults = wallpapersDefaults;
        this.isDefault = isDefault;
//        notifyDataSetChanged();
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
            holder.binding.getRoot().setOnClickListener(v -> {
                if(click!=null)
                    click.click(wallpapersDefaults.get(position),v);
            });
            holder.binding.img.setImageDrawable(holder.binding.getRoot().getContext().getDrawable(Integer.parseInt(wallpapersDefaults.get(position))));
        }else{



            holder.binding.img.setVisibility(View .GONE);
            holder.binding.img2.setVisibility(View.VISIBLE);
//            holder.binding.img.setImageBitmap((Bitmap) wallpapersDefaults.get(position));
            Glide.with(ChatActivity.activity.getBaseContext())
                    .load(wallpapersDefaults.get(position))
                    .apply(options)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .thumbnail(0.5f)
                    .into(holder.binding.img2);
        ViewCompat.setTransitionName(holder.binding.img2, position + "_image");
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
