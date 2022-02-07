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
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.Animations;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment implements WallpaperView{
    private FragmentWallpapersBinding binding;
    private AdapterWallpaper          adapterWallpaper;
    private AdapterWallpaper          adapterWallpaperUser;
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
        wallpaperPresenter = new WallpaperPresenterImpl(view.getContext(),this,ChatActivity.presenter);
        binding.setIsDefault(true);
        adapterWallpaper       = new AdapterWallpaper();
        adapterWallpaper.setClick((background, v) -> wallpaperPresenter.chosenBackground(background));

        binding.recyclerWallpaper.setHasFixedSize (true);
        binding.recyclerWallpaper.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapterWallpaperUser   = new AdapterWallpaper();
        binding.recyclerWallpaperUser.setHasFixedSize (true);
        binding.recyclerWallpaperUser.setLayoutManager(new GridLayoutManager(view.getContext(),3));
        binding.recyclerWallpaperUser.setItemViewCacheSize(20);
        binding.recyclerWallpaperUser.setDrawingCacheEnabled(true);
        binding.recyclerWallpaperUser.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.btnDefault.setOnClickListener(v ->{
            binding.setIsDefault(true);
            wallpaperPresenter.showDefaultWallpapers();});
        binding.btnAll.setOnClickListener(v -> {
            binding.setIsDefault(false);
                wallpaperPresenter.showUserWallpapers(v.getContext(), ChatActivity.activity);});

        wallpaperPresenter.showDefaultWallpapers();
    }




    @Override
    public void showDefaultWallpapers() {
        ArrayList<String> wallpapers = new ArrayList();
        wallpapers.add(R.drawable.ic_floppy_background+"");
        wallpapers.add(R.drawable.ic_fondo_uno+"");
        wallpapers.add(R.drawable.ic_fondo_tres+"");
        wallpapers.add(R.drawable.ic_fondo_dos+"");
        wallpapers.add(R.drawable.ic_fondo_cuatro+"");

        adapterWallpaper.setWallpapersDefaults(wallpapers,true);
        binding.recyclerWallpaper.setAdapter(adapterWallpaper);
    }

    @Override
    public void showUserWallpapers(ArrayList<String> path) {
        ChatActivity.activity.runOnUiThread(() -> {
            adapterWallpaperUser = new AdapterWallpaper();
            adapterWallpaperUser.setWallpapersDefaults(path,false);
            binding.recyclerWallpaperUser.setAdapter(adapterWallpaperUser);
        });
    }

    @Override
    public void changeView(Boolean show) { binding.setIsDefault(show); }

    @Override
    public void showHandling(Boolean show) {
        ChatActivity.activity.runOnUiThread(() -> {
            if(show){
                Animations.Companion.animAppear(binding.progress);
            }else{
                Animations.Companion.animVanish(binding.progress);
            }
        });
    }


}