package com.jacquessmuts.bakingforudacity.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacquessmuts.bakingforudacity.Activities.RecipeDetailActivity;
import com.jacquessmuts.bakingforudacity.Adapters.RecipeDetailAdapter;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeDetailFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.recyclerview_steps) RecyclerView recyclerview_steps;

    RecipeDetailAdapter mRecipeDetailAdapter;

    private Unbinder mUnbinder;
    private Recipe mRecipe;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Step item);
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        recyclerview_steps.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecipeDetailAdapter = new RecipeDetailAdapter(mRecipe, mListener);
        recyclerview_steps.setAdapter(mRecipeDetailAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        handleExtras(getActivity().getIntent().getExtras());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mUnbinder.unbind();
    }

    public void setSelectedIndex (int index){
        mRecipeDetailAdapter.setSelectedIndex(index);
    }

    private void handleExtras(Bundle extras){
        if (extras == null) return;

        if (extras.containsKey(RecipeDetailActivity.EXTRA_RECIPE)){
            mRecipe = extras.getParcelable(RecipeDetailActivity.EXTRA_RECIPE);
        }
    }
}
