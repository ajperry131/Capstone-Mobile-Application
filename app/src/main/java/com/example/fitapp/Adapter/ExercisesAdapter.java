package com.example.fitapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitapp.Model.Exercise;

import java.util.List;
import com.example.fitapp.R;


public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder> {
    private Context context;
    private List<Exercise> exerciseList;
    private SelectListener listener;
    private boolean isImageFitToScreen;

    public ExercisesAdapter(Context context, List<Exercise> exerciseList, SelectListener listener) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View exerciseView = LayoutInflater.from(context)
                .inflate(R.layout.layout_exercise, parent, false);
        return new ExercisesViewHolder(exerciseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesViewHolder holder, int position) {
        try {
            Glide.with(context).asGif().load(exerciseList.get(position).getExerciseGifUrl()).into(holder.imageViewExerciseGif);
        } catch (Exception e) {
            Glide.with(context).load(R.drawable.baseline_no_image_found_24).into(holder.imageViewExerciseGif);
        }
        holder.textViewExerciseName.setText(exerciseList.get(position).getName());
        holder.textViewExerciseType.setText(exerciseList.get(position).getExerciseType());
        holder.textViewExerciseName.setTag(exerciseList.get(position));

        holder.imageViewExerciseGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ImageView Clicked!", Toast.LENGTH_SHORT).show();
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    holder.imageViewExerciseGif.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    holder.imageViewExerciseGif.setAdjustViewBounds(true);
                    holder.textViewExerciseType.setVisibility(View.VISIBLE);
                } else {
                    isImageFitToScreen = true;
                    holder.imageViewExerciseGif.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, v.getResources().getDisplayMetrics().heightPixels / 2));
                    holder.imageViewExerciseGif.setScaleType(ImageView.ScaleType.FIT_XY);
                    holder.textViewExerciseType.setVisibility(View.INVISIBLE);
                }
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(exerciseList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void setFilteredList(List<Exercise> filteredList) {
        this.exerciseList = filteredList;
        notifyDataSetChanged();
    }

    public static class ExercisesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewExerciseGif;
        TextView textViewExerciseName, textViewExerciseType;

        ConstraintLayout constraintLayout;

        public ExercisesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExerciseName = (TextView) itemView.findViewById(R.id.textViewExerciseName);
            textViewExerciseType = (TextView) itemView.findViewById(R.id.textViewExerciseType);
            imageViewExerciseGif = (ImageView) itemView.findViewById(R.id.imageViewExerciseGif);

            constraintLayout = itemView.findViewById(R.id.container);
        }
    }
}
