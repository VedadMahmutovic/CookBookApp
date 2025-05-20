package com.example.cookbook.ui.theme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cookbook.R;
import com.example.cookbook.model.InstructionStep;
import com.example.cookbook.utils.LangUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepAdapter.StepViewHolder> {

    private final List<InstructionStep> stepList = new ArrayList<>();
    private final Context context;

    public InstructionStepAdapter(Context context) {
        this.context = context;
    }

    public void setSteps(List<InstructionStep> steps) {
        stepList.clear();
        stepList.addAll(steps);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_instruction_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        InstructionStep step = stepList.get(position);

        holder.textViewStepNumber.setText(String.format(Locale.getDefault(), "%s %d", context.getString(R.string.step_text), step.stepNumber));

        String lang = LangUtils.getCurrentLang(context);
        holder.textViewDescription.setText(LangUtils.getStepDescription(step, lang));

        if (step.imageUrl != null && !step.imageUrl.isEmpty()) {
            holder.progressBar.setVisibility(View.VISIBLE);

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference()
                    .child("InstructionImages/" + step.imageUrl);

            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                Log.e("InstructionStepAdapter", "Glide load failed", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.imageViewStep);
            }).addOnFailureListener(e -> {
                holder.progressBar.setVisibility(View.GONE);
                holder.imageViewStep.setVisibility(View.GONE);
                Log.e("InstructionStepAdapter", "Firebase getDownloadUrl failed", e);
            });
        } else {
            holder.imageViewStep.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStepNumber, textViewDescription;
        ImageView imageViewStep;
        ProgressBar progressBar;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStepNumber = itemView.findViewById(R.id.textViewStepNumber);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewStep = itemView.findViewById(R.id.imageViewStep);
            progressBar = itemView.findViewById(R.id.progressBarStep);
        }
    }
}
