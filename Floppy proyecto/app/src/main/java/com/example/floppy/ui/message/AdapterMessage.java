package com.example.floppy.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.domain.models.Message;
import com.example.floppy.R;

import java.util.ArrayList;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {

    private ArrayList<Message> messages;
    private String myId;

    public AdapterMessage(ArrayList<Message> messages, String myId) {
        this.messages = messages;
        this.myId     = myId;
        notifyItemInserted(messages.size());
    }

    @NonNull

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    public interface Click {
        void clickMessage(View view, int position, Message message);
    }

    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }


    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (messages.get(position).getTypeMessage()){
            case TEXT:
                holder.containerTypeText.setVisibility(View.VISIBLE);
                holder.containerTypeSticker.setVisibility(View.GONE);
                if (messages.get(position).getUser().equals(myId)){
                    holder.messageLeft .setVisibility(View.GONE);
                    holder.messageRight.setVisibility(View.VISIBLE);
                    holder.txtMessageSend.setText(messages.get(position).getMessage());
                }else{
                    holder.messageRight.setVisibility(View.GONE);
                    holder.messageLeft .setVisibility(View.VISIBLE);
                    holder.txtMessage.setText(messages.get(position).getMessage());
                }
                break;
            case STICKER:
                holder.containerTypeText   .setVisibility(View.GONE);
                holder.containerTypeSticker.setVisibility(View.VISIBLE);
                if (messages.get(position).getUser().equals(myId)){
                    holder.stickerLeft .setVisibility(View.GONE);
                    holder.stickerRight.setVisibility(View.VISIBLE);
                    holder.txtDateStickerRight.setText(messages.get(position).getDate());
                    Glide.with(holder.itemView.getContext())
                            .load(messages.get(position).getMessage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgStickerRight);
                }else{
                    holder.stickerRight.setVisibility(View.GONE);
                    holder.stickerLeft .setVisibility(View.VISIBLE);
                    holder.txtDateStickerLeft.setText(messages.get(position).getDate());
                    Glide.with(holder.itemView.getContext())
                            .load(messages.get(position).getMessage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgStickerLeft);
                }

                break;
        }
        holder.itemView.setOnClickListener(view -> {
          if(click != null) { click.clickMessage(view,position,messages.get(position)); }
        });

    }


    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout messageLeft;
        RelativeLayout stickerLeft;
        RelativeLayout messageRight;
        RelativeLayout stickerRight;
        RelativeLayout container;
        RelativeLayout containerTypeText;
        RelativeLayout containerTypeSticker;
        TextView       txtMessage;
        TextView       txtMessageSend;
        ImageView      imgStickerLeft;
        ImageView      imgStickerRight;
        TextView       txtDateStickerRight;
        TextView       txtDateStickerLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLeft     = itemView.findViewById(R.id.messageLeft);
            stickerLeft     = itemView.findViewById(R.id.stickerLeft);
            messageRight    = itemView.findViewById(R.id.messageRight);
            stickerRight    = itemView.findViewById(R.id.stickerRight);
            container       = itemView.findViewById(R.id.contenedor);
            txtMessage      = itemView.findViewById(R.id.txtMensaje);
            txtMessageSend  = itemView.findViewById(R.id.txtMensajeEnviado);
            imgStickerLeft  = stickerLeft .findViewById(R.id.imgSticker);
            imgStickerRight = stickerRight.findViewById(R.id.imgSticker);
            txtDateStickerLeft  = stickerLeft .findViewById(R.id.txtDateSticker);
            txtDateStickerRight = stickerRight.findViewById(R.id.txtDateSticker);

            containerTypeSticker = itemView.findViewById(R.id.messageTypeSticker);
            containerTypeText    = itemView.findViewById(R.id.messageTypeText);
        }
    }
}
