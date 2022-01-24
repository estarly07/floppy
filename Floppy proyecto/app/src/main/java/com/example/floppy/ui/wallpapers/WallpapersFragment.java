package com.example.floppy.ui.wallpapers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.floppy.R;
import com.example.floppy.databinding.FragmentWallpapersBinding;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.utils.Animations;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment implements WallpaperView{
    private FragmentWallpapersBinding binding;
    private AdapterWallpaper          adapterWallpaper;
    private WallpaperPresenter        wallpaperPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallpapers, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wallpaperPresenter = new WallpaperPresenterImpl(this);

        adapterWallpaper   = new AdapterWallpaper();
        binding.recyclerWallpaper.setHasFixedSize (true);
        binding.recyclerWallpaper.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        binding.setIsDefault(true);
        binding.btnDefault.setOnClickListener(v->{
            binding.setIsDefault(true);
            binding.recyclerWallpaper.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            wallpaperPresenter.showDefaultWallpapers();
        });
        binding.btnAll.setOnClickListener(v->{
            binding.setIsDefault(false);
            binding.recyclerWallpaper.setLayoutManager(new GridLayoutManager( getContext(),3));
            adapterWallpaper.cleanData();
            wallpaperPresenter.showUserWallpapers(v.getContext(), ChatActivity.activity);
        });

        wallpaperPresenter.showDefaultWallpapers();
    }




    @Override
    public void showDefaultWallpapers() {
        ArrayList<Object> wallpapers = new ArrayList();
        wallpapers.add(R.drawable.ic_floppy_background);
        wallpapers.add(R.drawable.ic_fondo_uno);
        wallpapers.add(R.drawable.ic_fondo_uno);
        wallpapers.add(R.drawable.ic_floppy_background);
        wallpapers.add(R.drawable.ic_fondo_uno);
        wallpapers.add(R.drawable.ic_floppy_background);
        binding.recyclerWallpaper.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapterWallpaper = new AdapterWallpaper();
        adapterWallpaper.setWallpapersDefaults(wallpapers,true);
        binding.recyclerWallpaper.setAdapter(adapterWallpaper);
        Animations.Companion.animVanish(binding.progress);
        Animations.Companion.animAppear(binding.recyclerWallpaper);

    }

    @Override
    public void showUserWallpapers(String paths) {
        ChatActivity.activity.runOnUiThread(() -> {
            binding.setIsDefault(false);
//            adapterWallpaper = new AdapterWallpaper();


            adapterWallpaper.setWallpapersDefaults(paths,false);
//            binding.recyclerWallpaper.setAdapter(adapterWallpaper);
        });
    }

    @Override
    public void showHandling(Boolean show) {
        ChatActivity.activity.runOnUiThread(() -> {
            if(show){
                Animations.Companion.animAppear(binding.progress);
                Animations.Companion.animVanish(binding.recyclerWallpaper);
            }else{
                Animations.Companion.animAppear(binding.recyclerWallpaper);
                Animations.Companion.animVanish(binding.progress);
            }
        });
    }


}