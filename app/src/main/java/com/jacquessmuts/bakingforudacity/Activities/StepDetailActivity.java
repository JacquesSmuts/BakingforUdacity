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
    @State
    int mStepIndex;

    public static Intent getIntent(Context context, Recipe recipe, Step step){
        Intent intent = new Intent(context, StepDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        int indexof = recipe.getSteps().indexOf(step);
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
        if (extras.containsKey(EXTRA_STEP_INDEX)){
            mStepIndex = extras.getInt(EXTRA_STEP_INDEX, 0);
        }
    }

    private void populateContents() {
        if (mRecipe == null) {
            finish();
            return;
        }
        Step currentStep = mRecipe.getSteps().get(mStepIndex);

        setTitle(getString(R.string.step_title, mRecipe.getName(), currentStep.getShortDescription()));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
