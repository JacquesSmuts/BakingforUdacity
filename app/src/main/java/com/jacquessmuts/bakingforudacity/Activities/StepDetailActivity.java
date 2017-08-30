package com.jacquessmuts.bakingforudacity.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jacquessmuts.bakingforudacity.Fragments.StepDetailFragment;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;

import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

import static com.jacquessmuts.bakingforudacity.Activities.RecipeDetailActivity.EXTRA_RECIPE;
import static com.jacquessmuts.bakingforudacity.Activities.RecipeDetailActivity.EXTRA_STEP_INDEX;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnFragmentInteractionListener {

    @State
    Recipe mRecipe;

    public static Intent getIntent(Context context, Recipe recipe, Step step){
        Intent intent = new Intent(context, StepDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        intent.putExtra(EXTRA_STEP_INDEX, recipe.getSteps().indexOf(step));

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
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

    private void populateContents() {
        if (mRecipe == null) {
            finish();
            return;
        }

        setTitle(mRecipe.getName());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
