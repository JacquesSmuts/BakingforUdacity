package com.jacquessmuts.bakingforudacity.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacquessmuts.bakingforudacity.Fragments.RecipeFragment.OnListFragmentInteractionListener;
import com.jacquessmuts.bakingforudacity.Fragments.dummy.DummyContent.DummyItem;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;

import java.util.List;

public class MyRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Step> mSteps;
    private final OnListFragmentInteractionListener mListener;

    public MyRecipeRecyclerViewAdapter(List<Step> items, OnListFragmentInteractionListener listener) {
        mSteps = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mStep = mSteps.get(position);
        holder.mIdView.setText(mSteps.get(position).getShortDescription());
        holder.mContentView.setText(mSteps.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mStep);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Step mStep;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
