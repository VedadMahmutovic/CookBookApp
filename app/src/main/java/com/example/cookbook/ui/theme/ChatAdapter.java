package com.example.cookbook.ui.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookbook.R;
import com.example.cookbook.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_AI = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if ("loading".equals(message.sender)) return VIEW_TYPE_LOADING;
        return "user".equals(message.sender) ? VIEW_TYPE_USER : VIEW_TYPE_AI;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.item_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_AI) {
            View view = inflater.inflate(R.layout.item_message_ai, parent, false);
            return new AiMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_chat_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        Context context = holder.itemView.getContext();
        if (context == null) return; // fallback!

        if (holder instanceof UserMessageViewHolder) {
            UserMessageViewHolder userHolder = (UserMessageViewHolder) holder;
            userHolder.text.setText(message.text);

            Uri imageUri = null;
            try {
                SharedPreferences prefs = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
                String imageUrl = prefs.getString("profile_pic_url", null);

                if (imageUrl != null && !"null".equals(imageUrl) && !imageUrl.trim().isEmpty()) {
                    imageUri = Uri.parse(imageUrl);
                }
            } catch (Exception ignored) {}

            if (userHolder.avatar != null) {
                if (imageUri != null) {
                    Glide.with(context)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile)
                            .circleCrop()
                            .into(userHolder.avatar);
                } else {
                    userHolder.avatar.setImageResource(R.drawable.ic_profile);
                }
            }

        } else if (holder instanceof AiMessageViewHolder) {
            AiMessageViewHolder aiHolder = (AiMessageViewHolder) holder;
            aiHolder.text.setText(message.text);

            if (aiHolder.avatar != null) {
                Glide.with(context)
                        .load(R.drawable.ic_ai)
                        .circleCrop()
                        .into(aiHolder.avatar);
            }

        } else if (holder instanceof LoadingViewHolder) {
            View dot1 = holder.itemView.findViewById(R.id.dot1);
            View dot2 = holder.itemView.findViewById(R.id.dot2);
            View dot3 = holder.itemView.findViewById(R.id.dot3);

            dot1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up_down));
            android.view.animation.Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.scale_up_down);
            android.view.animation.Animation anim3 = AnimationUtils.loadAnimation(context, R.anim.scale_up_down);
            anim2.setStartOffset(150);
            anim3.setStartOffset(300);
            dot2.startAnimation(anim2);
            dot3.startAnimation(anim3);
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView avatar;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textMessage);
            avatar = itemView.findViewById(R.id.avatarImage);
        }
    }

    static class AiMessageViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView avatar;

        AiMessageViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textMessage);
            avatar = itemView.findViewById(R.id.avatarImage);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
