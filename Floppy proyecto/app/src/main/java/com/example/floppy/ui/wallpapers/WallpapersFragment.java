package com.example.floppy.ui.wallpapers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.floppy.R;
import com.example.floppy.databinding.FragmentWallpapersBinding;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment {
    private FragmentWallpapersBinding binding;

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
        binding.recyclerWallpaper.setHasFixedSize(true);
        binding.recyclerWallpaper.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        AdapterWallpaper adapterWallpaper = new AdapterWallpaper();
        ArrayList<Integer> wallpapers = new ArrayList();
        wallpapers.add(R.drawable.ic_floppy_background);
        wallpapers.add(R.drawable.ic_fondo_dos);

        adapterWallpaper.setWallpapersDefaults(wallpapers);
        binding.recyclerWallpaper.setAdapter(adapterWallpaper);
    }
}