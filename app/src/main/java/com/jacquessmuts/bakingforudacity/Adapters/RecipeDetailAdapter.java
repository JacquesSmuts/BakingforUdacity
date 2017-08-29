package com.jacquessmuts.bakingforudacity.Adapters;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.jacquessmuts.bakingforudacity.Fragments.RecipeFragment.OnListFragmentInteractionListener;
import com.jacquessmuts.bakingforudacity.Models.Ingredient;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends SectionedRecyclerViewAdapter<RecipeDetailAdapter.ViewHolder> {

    public static final int POS_INGREDIENTS = 0;
    public static final int POS_STEPS = 1;

    private final Recipe mRecipe;
    private final OnListFragmentInteractionListener mListener;

    public enum Type {
        HEADER, INGREDIENT, STEP;
    }

    public RecipeDetailAdapter(Recipe recipe, OnListFragmentInteractionListener listener) {
        mRecipe = recipe;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section, boolean expanded) {

        switch (section) {
            case POS_INGREDIENTS:
                holder.setTitle("Ingredients");
                break;
            case POS_STEPS:
                holder.setTitle("Steps");
                break;
        }
        holder.setType(Type.HEADER);
    }

    @Override
    public void onBindFooterViewHolder(ViewHolder holder, int section) {
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, final int relativePosition, int absolutePosition) {

        switch (section) {
            case POS_INGREDIENTS:
                holder.mIngredient = mRecipe.getIngredients().get(relativePosition);
                holder.setType(Type.INGREDIENT);
                holder.mView.setOnClickListener(null);
                break;
            case POS_STEPS:
                holder.mStep = mRecipe.getSteps().get(relativePosition);
                holder.setType(Type.STEP);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            mListener.onListFragmentInteraction(mRecipe.getSteps().get(relativePosition));
                        }
                    }
                });
                break;
        }


    }

    @Override
    public int getSectionCount() {
        return 2;
    }

    @Override
    public int getItemCount(int section) {
        int itemCount = 0;
        switch (section) {
            case POS_INGREDIENTS:
                itemCount = mRecipe.getIngredients().size();
                break;
            case POS_STEPS:
                itemCount = mRecipe.getSteps().size();
                break;
        }

        return itemCount;
    }

    public class ViewHolder extends SectionedViewHolder {

        public final View mView;
        @BindView(R.id.text_quantity_measure) TextView textQuantityMeasure;
        @BindView(R.id.text_ingredient_name) TextView textIngredientName;
        @BindView(R.id.layout_ingredient) ConstraintLayout layoutIngredient;
        @BindView(R.id.image_right) ImageView imageRight;
        @BindView(R.id.text_step_name) TextView textStepName;
        @BindView(R.id.layout_step) ConstraintLayout layoutStep;
        @BindView(R.id.text_title) TextView textTitle;
        @BindView(R.id.layout_header) LinearLayout layoutHeader;

        public Step mStep;
        public Ingredient mIngredient;
        public Type mType;
        public String mTitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public void setType(Type type) {
            mType = type;
            populateView();
        }

        private void populateView() {

            switch (mType) {
                case HEADER:
                    layoutHeader.setVisibility(View.VISIBLE);
                    layoutIngredient.setVisibility(View.GONE);
                    layoutStep.setVisibility(View.GONE);
                    textTitle.setText(mTitle);
                    break;
                case INGREDIENT:
                    layoutHeader.setVisibility(View.GONE);
                    layoutIngredient.setVisibility(View.VISIBLE);
                    layoutStep.setVisibility(View.GONE);

                    textIngredientName.setText(mIngredient.getIngredient());
                    textQuantityMeasure.setText(mIngredient.getMeasure() + " - " + mIngredient.getQuantity());
                    break;
                case STEP:
                    layoutHeader.setVisibility(View.GONE);
                    layoutIngredient.setVisibility(View.VISIBLE);
                    layoutStep.setVisibility(View.GONE);

                    textStepName.setText(mStep.getShortDescription());
                    break;
            }


        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
