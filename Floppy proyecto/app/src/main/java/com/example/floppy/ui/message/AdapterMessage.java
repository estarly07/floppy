package com.example.floppy.ui.message;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.io.IOException;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(DataBindingUtil.inflate(inflater,R.layout.item_mensaje, parent, false));
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
            case RECORD:
                holder.binding.messageTypeText   .setVisibility(View.GONE);
                holder.binding.messageTypeSticker.setVisibility(View.GONE);
                holder.binding.msgAudio.getRoot().setVisibility(View.VISIBLE);

                if (messages.get(position).getUser().equals(myId)){
                    holder.binding.msgAudio.getRoot().setOnClickListener(view -> playAudio(view.getContext(),messages.get(position).getMessage()));
                    holder.binding.msgAudio.messageLeft .setVisibility(View.GONE);
                    holder.binding.msgAudio.messageRight.setVisibility(View.VISIBLE);
//                    holder.binding.txtMensajeEnviado.setText(messages.get(position).getMessage());

                    Extensions.Companion.changeDoubleCheckColor(
                            holder.binding.msgAudio.imgCheck,
                            messages.get(position).getState() == StateMessage.CHECK);
                }else{
                    holder.binding.msgAudio.messageRight.setVisibility(View.GONE);
                    holder.binding.msgAudio.messageLeft .setVisibility(View.VISIBLE);
//                    holder.binding.txtMensaje  .setText(messages.get(position).getMessage());
                }
                break;

            case TEXT:

                holder.binding.messageTypeText   .setVisibility(View.VISIBLE);
                holder.binding.messageTypeSticker.setVisibility(View.GONE);
                if (messages.get(position).getUser().equals(myId)){
                    holder.binding.messageLeft .setVisibility(View.GONE);
                    holder.binding.messageRight.setVisibility(View.VISIBLE);
                    holder.binding.txtMensajeEnviado.setText(messages.get(position).getMessage());

                    Extensions.Companion.changeDoubleCheckColor(
                            holder.binding.imgCheck,
                            messages.get(position).getState() == StateMessage.CHECK);
                }else{
                    holder.binding.messageRight.setVisibility(View.GONE);
                    holder.binding.messageLeft .setVisibility(View.VISIBLE);
                    holder.binding.txtMensaje  .setText(messages.get(position).getMessage());
                }
                break;
            case STICKER:
                holder.binding.messageTypeText   .setVisibility(View.GONE);
                holder.binding.messageTypeSticker.setVisibility(View.VISIBLE);
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
          if(click != null) { click.clickMessage(view,position,messages.get(position)); }
        });

    }

    private void playAudio(Context context,String message) {

        MediaPlayer player = new MediaPlayer();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            player.setAudioAttributes(new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                    .build());
//        } else {
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        }
        try {
            player.setOnPreparedListener(mediaPlayer -> mediaPlayer.start());
            System.out.println(" "+context.getExternalFilesDir(null)+"/audios/"+message+".mp3");
            player.setDataSource("/storage/emulated/0/Android/data/com.example.floppy/files/audios/2022-0-12 15:38:473.mp3");
            player.prepareAsync();
            player.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

    }
}
