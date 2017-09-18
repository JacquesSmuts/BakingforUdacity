package com.jacquessmuts.bakingforudacity.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by smuts on 2017/09/18.
 */

public class PreferencesManager {
    private static final String PREF_NAME = "com.example.app.PREF_NAME";

    private static final String RECIPE_INDEX = "com.example.app.RECIPE_INDEX";
    private static final String INGREDIENT_INDEX = "com.example.app.INGREDIENT_INDEX";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setWidgetRecipeIndex(int value){
        setValue(RECIPE_INDEX, value);
    }

    public int getWidgetRecipeIndex(){
        return getValue(RECIPE_INDEX);
    }

    public void setWidgetIngredientIndex(int value){
        setValue(INGREDIENT_INDEX, value);
    }

    public int getWidgetIngredientIndex(){
        return getValue(INGREDIENT_INDEX);
    }

    public void setValue(String name, int value) {
        mPref.edit()
                .putInt(name, value)
                .commit();
    }

    public int getValue(String name) {
        return mPref.getInt(name, 0);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }



}
