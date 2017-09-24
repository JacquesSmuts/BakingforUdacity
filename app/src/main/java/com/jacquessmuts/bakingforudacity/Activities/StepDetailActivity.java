package com.jacquessmuts.bakingforudacity.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jacquessmuts.bakingforudacity.Fragments.StepDetailFragment;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;
import com.jacquessmuts.bakingforudacity.Utils.Util;

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
        intent.putExtra(EXTRA_STEP_INDEX, recipe.getSteps().indexOf(step));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Util.isTablet(this) && getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        setContentView(R.layout.activity_step_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
    public void newStepIndex(int index) {
        mStepIndex = index;
        populateContents();
    }
}
