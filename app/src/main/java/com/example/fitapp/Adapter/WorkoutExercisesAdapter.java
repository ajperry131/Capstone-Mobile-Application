package com.example.fitapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.Model.WorkoutExercise;

import java.util.List;
import com.example.fitapp.R;


public class WorkoutExercisesAdapter extends RecyclerView.Adapter<WorkoutExercisesAdapter.WorkoutExercisesViewHolder> {
    Context context;
    List<WorkoutExercise> workoutExerciseList;
    boolean isImageFitToScreen;

    public WorkoutExercisesAdapter(Context context, List<WorkoutExercise> WorkoutExerciseList) {
        this.context = context;
        this.workoutExerciseList = WorkoutExerciseList;
    }

    @NonNull
    @Override
    public WorkoutExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View WorkoutExerciseView = LayoutInflater.from(context)
                .inflate(R.layout.layout_exercise_in_workout, parent, false);
        return new WorkoutExercisesViewHolder(WorkoutExerciseView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutExercisesViewHolder holder, int position) {
        DBHandler dbHandler = new DBHandler(context);

        //try to load gif into image view, if it fails, user may not have internet connection or
        //gif may no longer exist, display default icon instead
        String exerciseGifUrl = dbHandler.getGifUrlOfExercise(workoutExerciseList.get(position).getExerciseName());
        try {
            Glide.with(context).asGif().load(exerciseGifUrl).into(holder.imageViewWorkoutExerciseGif);
        } catch (Exception e) {
            Glide.with(context).load(R.drawable.baseline_no_image_found_24).into(holder.imageViewWorkoutExerciseGif);
        }
        holder.imageViewWorkoutExerciseGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ImageView Clicked!", Toast.LENGTH_SHORT).show();
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    holder.imageViewWorkoutExerciseGif.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    holder.imageViewWorkoutExerciseGif.setAdjustViewBounds(true);
                    holder.textViewWorkoutExerciseType.setVisibility(View.VISIBLE);
                } else {
                    isImageFitToScreen = true;
                    holder.imageViewWorkoutExerciseGif.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, v.getResources().getDisplayMetrics().heightPixels / 2));
                    holder.imageViewWorkoutExerciseGif.setScaleType(ImageView.ScaleType.FIT_XY);
                    holder.textViewWorkoutExerciseType.setVisibility(View.INVISIBLE);
                }
            }
        });

        String exerciseType = dbHandler.getTypeOfExercise(workoutExerciseList.get(position).getExerciseName());
        holder.textViewWorkoutExerciseType.setText(exerciseType);

        holder.textViewWorkoutExerciseName.setText(workoutExerciseList.get(position).getExerciseName());
        holder.textViewWorkoutExerciseSets.setText(String.valueOf(workoutExerciseList.get(position).getWorkoutExerciseSets()));
        holder.textViewWorkoutExerciseReps.setText(String.valueOf(workoutExerciseList.get(position).getWorkoutExerciseReps()));
        holder.textViewWorkoutExerciseWeight.setText(String.valueOf(workoutExerciseList.get(position).getWorkoutExerciseWeight()));
    }

    @Override
    public int getItemCount() {
        return workoutExerciseList.size();
    }

    public void clear() {
        int size = workoutExerciseList.size();
        workoutExerciseList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void removeAt(int position) {
        workoutExerciseList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, workoutExerciseList.size());
    }

    public class WorkoutExercisesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWorkoutExerciseName, textViewWorkoutExerciseType, textViewWorkoutExerciseSets,
                textViewWorkoutExerciseReps, textViewWorkoutExerciseWeight;
        ImageView imageViewWorkoutExerciseGif;

        public WorkoutExercisesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewWorkoutExerciseGif = (ImageView) itemView.findViewById(R.id.imageViewWorkoutExerciseGif);
            textViewWorkoutExerciseName = (TextView) itemView.findViewById(R.id.textViewWorkoutExerciseName);
            textViewWorkoutExerciseType = (TextView) itemView.findViewById(R.id.textViewWorkoutExerciseType);
            textViewWorkoutExerciseSets = (TextView) itemView.findViewById(R.id.textViewWorkoutExerciseSets);
            textViewWorkoutExerciseReps = (TextView) itemView.findViewById(R.id.textViewWorkoutExerciseReps);
            textViewWorkoutExerciseWeight = (TextView) itemView.findViewById(R.id.textViewWorkoutExerciseWeight);
        }

    }
}
