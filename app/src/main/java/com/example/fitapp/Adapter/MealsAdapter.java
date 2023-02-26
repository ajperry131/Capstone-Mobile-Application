package com.example.fitapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitapp.Model.Meal;

import java.util.List;
import com.example.fitapp.R;


public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealsViewHolder> {
    Context context;
    List<Meal> mealList;

    public MealsAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mealView = LayoutInflater.from(context)
                .inflate(R.layout.layout_meal, parent, false);
        return new MealsViewHolder(mealView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsViewHolder holder, int position) {
        holder.textViewMealName.setText(mealList.get(position).getName());
        holder.textViewMealCalories.setText(String.valueOf(mealList.get(position).getCalories()));
        holder.textViewMealProtein.setText(String.valueOf(mealList.get(position).getProtein()));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void removeAt(int position) {
        mealList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mealList.size());
    }

    public static class MealsViewHolder extends RecyclerView.ViewHolder{
        TextView textViewMealName, textViewMealCalories, textViewMealProtein;

        public MealsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMealName = (TextView) itemView.findViewById(R.id.textViewMealName);
            textViewMealCalories = (TextView) itemView.findViewById(R.id.textViewMealCalories);
            textViewMealProtein = (TextView) itemView.findViewById(R.id.textViewMealProtein);
        }

    }
}
