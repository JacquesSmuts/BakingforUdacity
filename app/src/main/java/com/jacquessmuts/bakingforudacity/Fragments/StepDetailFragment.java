package com.jacquessmuts.bakingforudacity.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.jacquessmuts.bakingforudacity.Activities.RecipeDetailActivity;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;

public class StepDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE = "arg_recipe";
    private static final String ARG_STEP_INDEX = "arg_step_index";

    @BindView(R.id.exo_player_step) SimpleExoPlayerView exoPlayerStep;
    @BindView(R.id.text_step_description) TextView textStepDescription;
    @BindView(R.id.image_left) ImageView imageLeft;
    @BindView(R.id.image_right) ImageView imageRight;

    @State
    long mPlayerPosition = -1;

    public interface OnFragmentInteractionListener {
        void newStepIndex(int index);
    }

    private Recipe mRecipe;
    private int mStepIndex;
    private Unbinder mUnbinder;
    private OnFragmentInteractionListener mListener;

    private DataSource.Factory mMediaDataSourceFactory;
    private BandwidthMeter mBandwidthMeter;
    private SimpleExoPlayer mPlayer;

    public StepDetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBandwidthMeter = new DefaultBandwidthMeter();
        mMediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "mediaPlayerSample"),
                (TransferListener<? super DataSource>) mBandwidthMeter);

        handleExtras(getActivity().getIntent().getExtras());
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        populateViews();
        setupVideo();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mUnbinder.unbind();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick({R.id.image_left, R.id.image_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                mStepIndex--;
                break;
            case R.id.image_right:
                mStepIndex++;
                break;
        }
        mListener.newStepIndex(mStepIndex);
        populateViews();
        handleVideoSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerPosition = mPlayer.getCurrentPosition();
    }

    public void setStepIndex(int mStepIndex) {
        this.mStepIndex = mStepIndex;
        populateViews();
        handleVideoSource();
    }

    private void handleExtras(Bundle extras){
        if (extras == null) return;

        if (extras.containsKey(RecipeDetailActivity.EXTRA_RECIPE)){
            mRecipe = extras.getParcelable(RecipeDetailActivity.EXTRA_RECIPE);
        }

        mStepIndex = extras.getInt(RecipeDetailActivity.EXTRA_STEP_INDEX, 0);
        if (mStepIndex < 0) mStepIndex = 0;
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void populateViews(){
        Step currentStep = mRecipe.getSteps().get(mStepIndex);
        textStepDescription.setText(currentStep.getDescription());

        if (mStepIndex < 1){
            imageLeft.setVisibility(View.GONE);
        } else {
            imageLeft.setVisibility(View.VISIBLE);
        }

        if (mStepIndex >= (mRecipe.getSteps().size()-1)){
            imageRight.setVisibility(View.GONE);
        } else {
            imageRight.setVisibility(View.VISIBLE);
        }

    }

    private void setupVideo(){
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        // 2. Create the player
        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        // Bind the player to the view.
        exoPlayerStep.setPlayer(mPlayer);

        exoPlayerStep.requestFocus();

        mPlayer.setPlayWhenReady(true);
        handleVideoSource();
    }

    private void handleVideoSource(){
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mRecipe.getSteps().get(mStepIndex).getVideoURL()),
                mMediaDataSourceFactory, extractorsFactory, null, null);

        mPlayer.stop();
        mPlayer.prepare(mediaSource);

        if (mPlayerPosition > 0){
            mPlayer.seekTo(mPlayerPosition);
            mPlayerPosition = -1;
        }

    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

}
