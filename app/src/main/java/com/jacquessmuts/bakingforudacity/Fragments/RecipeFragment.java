package com.jacquessmuts.bakingforudacity.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacquessmuts.bakingforudacity.Activities.RecipeDetailActivity;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;
import com.jacquessmuts.bakingforudacity.Fragments.dummy.DummyContent;
import com.jacquessmuts.bakingforudacity.Fragments.dummy.DummyContent.DummyItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.recyclerview_steps) RecyclerView recyclerview_steps;

    private Recipe mRecipe;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Step item);
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);
        recyclerview_steps.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview_steps.setAdapter(new MyRecipeRecyclerViewAdapter(mRecipe.getSteps(), mListener));

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
    }

    private void handleExtras(Bundle extras){

        if (extras == null) return;

        if (extras.containsKey(RecipeDetailActivity.EXTRA_RECIPE)){
            mRecipe = extras.getParcelable(RecipeDetailActivity.EXTRA_RECIPE);
        }

    }

}
