package com.example.floppy.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.databinding.ItemMensajeBinding;
import com.example.floppy.domain.models.Message;
import com.example.floppy.R;
import com.example.floppy.domain.models.StateMessage;
import com.example.floppy.utils.Extensions;

import java.util.ArrayList;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {

    private final ArrayList<Message> messages;
    private final String myId;

    public AdapterMessage(ArrayList<Message> messages, String myId) {
        this.messages = messages;
        this.myId     = myId;
        notifyItemInserted(messages.size());
    }

    @NonNull

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_mensaje, parent, false));
    }

    public interface Click {
        void clickMessage(View view, int position, Message message,ViewHolder viewHolder);
    }

    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
        holder.binding.setTypeMessage(messages.get(position).getTypeMessage());
        switch (messages.get(position).getTypeMessage()){
            case RECORD:
                if (messages.get(position).getUser().equals(myId)){
                    holder.binding.msgAudio.messageLeft .setVisibility(View.GONE);
                    holder.binding.msgAudio.messageRight.setVisibility(View.VISIBLE);
                    Extensions.Companion.changeDoubleCheckColor(
                            holder.binding.msgAudio.imgCheck,
                            messages.get(position).getState() == StateMessage.CHECK);
                }else{
                    holder.binding.msgAudio.messageRight.setVisibility(View.GONE);
                    holder.binding.msgAudio.messageLeft .setVisibility(View.VISIBLE);
                }
                break;

            case TEXT:
                if (messages.get(position).getUser().equals(myId)){
                    holder.binding.messageTypeText.messageLeft .setVisibility(View.GONE);
                    holder.binding.messageTypeText.messageRight.setVisibility(View.VISIBLE);
                    holder.binding.messageTypeText.txtMensajeEnviado.setText(messages.get(position).getMessage());

                    Extensions.Companion.changeDoubleCheckColor(
                            holder.binding.messageTypeText.imgCheck,
                            messages.get(position).getState() == StateMessage.CHECK);
                }else{
                    holder.binding.messageTypeText.messageRight.setVisibility(View.GONE);
                    holder.binding.messageTypeText.messageLeft .setVisibility(View.VISIBLE);
                    holder.binding.messageTypeText.txtMensaje  .setText(messages.get(position).getMessage());
                }
                break;
            case STICKER:
                if (messages.get(position).getUser().equals(myId)){
                    holder.binding.stickerLeft .getRoot().setVisibility(View.GONE);
                    holder.binding.stickerRight.getRoot().setVisibility(View.VISIBLE);
                    holder.binding.stickerRight.txtDateSticker.setText(messages.get(position).getHora());

                    Extensions.Companion.changeDoubleCheckColor(
                            holder.binding.stickerRight.imgCheck,
                            messages.get(position).getState() == StateMessage.CHECK);

                    Glide.with(holder.binding.getRoot().getContext())
                            .load(messages.get(position).getMessage())
                            .placeholder(R.drawable.ic_wait_sticker)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.binding.stickerRight.imgSticker);
                }else{
                    holder.binding.stickerRight. getRoot().setVisibility(View.GONE);
                    holder.binding.stickerLeft . getRoot().setVisibility(View.VISIBLE);
                    holder.binding.stickerLeft . txtDateSticker.setText(messages.get(position).getHora());
                    holder.binding.stickerLeft . imgCheck.setVisibility(View.GONE);

                    Glide.with(holder.binding.getRoot().getContext())
                            .load(messages.get(position).getMessage())
                            .placeholder(R.drawable.ic_wait_sticker)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.binding.stickerLeft.imgSticker);
                }

                break;
        }
        holder.binding.getRoot().setOnClickListener(view -> {
          if(click != null) { click.clickMessage(view,position,messages.get(position),holder); }
        });

    }

    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMensajeBinding binding;

        public ViewHolder(ItemMensajeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.msgAudio.setIsPlaying(false);
        }
    }
}
