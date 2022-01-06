package com.example.floppy.ui.menu.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.R;

import java.util.ArrayList;

public class AdapterEstado extends RecyclerView.Adapter<AdapterEstado.Holder> {
    private ArrayList<ArrayList<Estado>> estados;


    public AdapterEstado(ArrayList<ArrayList<Estado>> estados) {
        this.estados = estados;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estado, parent, false);
        return new Holder(view);
    }

    private Click click;

    public interface Click {
        void onClick(View vista, int position, ArrayList<Estado> estados);
    }

    public void setClick(Click click) {
        this.click = click;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_recicler_estados);
        holder.itemView.setAnimation(animation);
        Glide.with(holder.itemView.getContext())
                .load(estados.get(position).get(0).getImagen())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.txtMensage.setText(estados.get(position).get(0).getNameUser());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click != null) {
                    click.onClick(view, position, estados.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return estados.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtMensage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItemEstado);
            txtMensage = itemView.findViewById(R.id.txtItemEstado);

        }
    }
}
