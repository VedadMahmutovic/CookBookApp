package com.example.cookbook.ui.theme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.model.InstructionStep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class InstructionStepInputAdapter extends RecyclerView.Adapter<InstructionStepInputAdapter.StepViewHolder> {

    private final List<InstructionStep> steps;
    private final Context context;
    private final RecyclerView recyclerView;

    public InstructionStepInputAdapter(List<InstructionStep> steps, Context context, RecyclerView recyclerView) {
        this.steps = steps;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_editable_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        InstructionStep step = steps.get(position);
        holder.stepNumber.setText(String.format(Locale.getDefault(), "%s %d", context.getString(R.string.step_text), position + 1));
        holder.editText.setText(Locale.getDefault().getLanguage().equals("bs") ? step.descriptionBs : step.descriptionEn);

        if (step.imageUrl != null && !step.imageUrl.isEmpty()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(step.imageUrl));
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        holder.buttonUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(intent, 1000 + position);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            steps.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, steps.size());
        });

        holder.editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String value = holder.editText.getText().toString();
                if (Locale.getDefault().getLanguage().equals("bs")) {
                    step.descriptionBs = value;
                } else {
                    step.descriptionEn = value;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void handleImageResult(int requestCode, Uri imageUri) {
        int index = requestCode - 1000;
        if (index >= 0 && index < steps.size()) {
            try {
                InputStream stream = context.getContentResolver().openInputStream(imageUri);
                File dir = new File(context.getFilesDir(), "steps");
                if (!dir.exists()) dir.mkdirs();
                File saved = new File(dir, UUID.randomUUID().toString() + ".jpg");
                FileOutputStream out = new FileOutputStream(saved);
                BitmapFactory.decodeStream(stream).compress(Bitmap.CompressFormat.JPEG, 85, out);
                out.close();
                steps.get(index).imageUrl = saved.getAbsolutePath();
                notifyItemChanged(index);
            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.image_fail_save), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepNumber;
        public EditText editText;
        ImageView imageView;
        Button buttonUpload, buttonDelete;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.textViewStepNumber);
            editText = itemView.findViewById(R.id.editTextStepDesc);
            imageView = itemView.findViewById(R.id.imageViewStep);
            buttonUpload = itemView.findViewById(R.id.buttonUploadStepImage);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteStep);
        }
    }

    public void updateStepDescriptions() {
        for (int i = 0; i < steps.size(); i++) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
            if (holder instanceof StepViewHolder) {
                StepViewHolder stepHolder = (StepViewHolder) holder;
                String desc = stepHolder.editText.getText().toString();
                if (Locale.getDefault().getLanguage().equals("bs")) {
                    steps.get(i).descriptionBs = desc;
                } else {
                    steps.get(i).descriptionEn = desc;
                }
            }
        }
    }
}
