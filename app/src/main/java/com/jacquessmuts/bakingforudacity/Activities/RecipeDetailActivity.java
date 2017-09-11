package com.jacquessmuts.bakingforudacity.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jacquessmuts.bakingforudacity.Fragments.RecipeDetailFragment;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;

import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_STEP_INDEX = "extra_step_index";

    @State
    Recipe mRecipe;

    // Track whether to display a two-pane or single-pane UI (Tablet/Phone)
    private boolean mTwoPane;

    public static Intent getIntent(Context context, Recipe recipe){
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        handleExtras();
        Icepick.restoreInstanceState(this, savedInstanceState);
        populateContents();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void handleExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_RECIPE)){
            mRecipe = extras.getParcelable(EXTRA_RECIPE);
        }
    }

    private void populateContents(){
        if (mRecipe == null){
            finish();
            return;
        }

        setTitle(mRecipe.getName());

        // Determine if you're creating a two-pane or single-pane display
//        if(findViewById(R.id.second_fragment) != null) {
//            // This LinearLayout will only initially exist in the two-pane tablet case
//            mTwoPane = true;
//
//        }
    }

    @Override
    public void onListFragmentInteraction(Step item) {
        if (mTwoPane){
            //todo: update detail view fragment
        } else {
            startActivity(StepDetailActivity.getIntent(this, mRecipe, item));
        }
    }
}