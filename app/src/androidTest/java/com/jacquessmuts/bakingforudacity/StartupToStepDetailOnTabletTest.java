package com.jacquessmuts.bakingforudacity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.jacquessmuts.bakingforudacity.Activities.MainActivity;
import com.jacquessmuts.bakingforudacity.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartupToStepDetailOnTabletTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void startupToStepDetailOnTabletTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview_home),
                        withParent(withId(R.id.swiperefresh_home)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recyclerview_steps),
                        withParent(withId(R.id.recipe_detail_fragment)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(14, click()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.image_right),
                        withParent(withId(R.id.step_detail_fragment)),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.recyclerview_steps),
                        withParent(withId(R.id.recipe_detail_fragment)),
                        isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(15, click()));
    }

}
