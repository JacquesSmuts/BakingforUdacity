package com.jacquessmuts.bakingforudacity.Activities;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jacquessmuts.bakingforudacity.Adapters.RecipeAdapter;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.R;
import com.jacquessmuts.bakingforudacity.Utils.PreferencesManager;
import com.jacquessmuts.bakingforudacity.Utils.Server;
import com.jacquessmuts.bakingforudacity.Utils.Util;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.swiperefresh_home) SwipeRefreshLayout swiperefresh_home;
    @BindView(R.id.recyclerview_home) RecyclerView recyclerview_home;
    GridLayoutManager mLayoutManager;

    private RecipeAdapter mRecipeAdapter;
    @State int scrollPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        setupRecyclerView();
        getRecipesFromUrl();

        if (savedInstanceState != null){
            mLayoutManager.scrollToPosition(scrollPosition);
            scrollPosition = -1;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(KEY_LAYOUT_INSTANCE_STATE, layoutManager.onSaveInstanceState());

        scrollPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        Icepick.saveInstanceState(this, outState);
    }

    private void getRecipesFromUrl(){
        if (Util.getConnected(this)){
            swiperefresh_home.setRefreshing(true);
            Server.getRecipes(new GetRecipesListener());
        } else {
            handleServerSuccess(false);
        }
    }


    private void setupRecyclerView(){
        int columns = 1;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            columns = 2;
        }
        if (Util.isTablet(this)){
            columns = columns+1;
        }

        mLayoutManager = new GridLayoutManager(this, columns);
        recyclerview_home.setLayoutManager(mLayoutManager);

        int marginInPixels = (int) getResources().getDimension(R.dimen.grid_layout_margin);
        recyclerview_home.addItemDecoration(new GridSpacingItemDecoration(columns, marginInPixels, true));
        recyclerview_home.setHasFixedSize(true);
        swiperefresh_home.setEnabled(false);

        mRecipeAdapter = new RecipeAdapter(new OnRecipeItemClickListener());
        recyclerview_home.setAdapter(mRecipeAdapter);
    }

    public void handleServerResponse(final String response){

        //runOnUiThread needs to be done because the adapter's notifydatasetchanged only works on UI thread
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final ArrayList<Recipe> recipes = Recipe.listFromJson(response);
                handleServerSuccess(recipes != null && recipes.size() > 0);
                mRecipeAdapter.setData(recipes);
                swiperefresh_home.setRefreshing(false);

                PreferencesManager.initializeInstance(MainActivity.this);
                PreferencesManager.getInstance().setRecipes(recipes);
            }
        });
    }

    private void handleServerSuccess(boolean success){
        if (!success){
            Util.errorMessageInternet(this);
        }
    }

    private class GetRecipesListener implements Server.ServerListener{

        @Override
        public void serverResponse(String response) {
            handleServerResponse(response);
        }
    }

    private class OnRecipeItemClickListener implements RecipeAdapter.RecipeItemOnClickListener {

        @Override
        public void onClick(Recipe recipe) {
            startActivity(RecipeDetailActivity.getIntent(MainActivity.this, recipe));
        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
