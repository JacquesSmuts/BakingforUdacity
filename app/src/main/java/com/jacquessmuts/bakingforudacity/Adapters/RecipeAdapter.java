package com.jacquessmuts.bakingforudacity.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.R;
import com.jacquessmuts.bakingforudacity.Utils.Server;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by smuts on 2017/08/27.
 */

public class RecipeAdapter extends RecyclerView.Adapter <RecipeAdapter.RecipeItemViewHolder> {

    private ArrayList<Recipe> mRecipeList;

    private final RecipeItemOnClickListener mRecipeOnClickListener;

    public RecipeAdapter(RecipeItemOnClickListener mRecipeOnClickListener) {
        this.mRecipeOnClickListener = mRecipeOnClickListener;
    }

    public interface RecipeItemOnClickListener {
        void onClick(Recipe recipe);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mRecipeList != null) {
            size = mRecipeList.size();
        }
        return size;
    }

    @Override
    public RecipeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipe, viewGroup, false);
        return new RecipeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeItemViewHolder recipeItemViewHolder, int position) {

        Recipe model = mRecipeList.get(position);

        Context context = recipeItemViewHolder.image_recipe.getContext();
        String url = Server.findFirstImageInStack(model);
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context)
                    .load(url)
                    .placeholder(android.R.drawable.ic_input_get)
                    .into(recipeItemViewHolder.image_recipe);
        }

        recipeItemViewHolder.text_name.setText(model.getName());
        recipeItemViewHolder.text_servings.setText(context.getString(R.string.number_of_servings, model.getServings()));
    }


    public class RecipeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_recipe) ImageView image_recipe;
        @BindView(R.id.text_name) TextView text_name;
        @BindView(R.id.text_servings) TextView text_servings;

        public RecipeItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //imageMoviePoster = (ImageView) view.findViewById(R.id.image_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mRecipeList.get(getAdapterPosition());
            mRecipeOnClickListener.onClick(recipe);
        }
    }

    public void setData(ArrayList<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        notifyDataSetChanged();
    }


}
