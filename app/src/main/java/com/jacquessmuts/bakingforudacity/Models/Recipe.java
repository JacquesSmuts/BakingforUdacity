
package com.jacquessmuts.bakingforudacity.Models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jacquessmuts.bakingforudacity.Utils.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    public static final String RECIPE_ASSET_NAME = "baking.json";

    private Long id;
    private String image;
    private List<Ingredient> ingredients;
    private String name;
    private Long servings;
    private List<Step> steps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServings() {
        return servings;
    }

    public void setServings(Long servings) {
        this.servings = servings;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public static ArrayList<Recipe> getAllFromJson(Context context){
        ArrayList<Recipe> toReturn;
        String json = Util.loadJsonFromAsset(RECIPE_ASSET_NAME, context);
        toReturn = listFromJson(json);
        return toReturn;
    }

    public static ArrayList<Recipe> listFromJson(String jsonString){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Recipe>>(){}.getType();
        ArrayList<Recipe> recipes = null;
        try {
            recipes = gson.fromJson(jsonString, listType);
            //GSON does not read Unicode correctly. Not sure what the best solution for that is...
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.image);
        dest.writeList(this.ingredients);
        dest.writeString(this.name);
        dest.writeValue(this.servings);
        dest.writeList(this.steps);
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.image = in.readString();
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.name = in.readString();
        this.servings = (Long) in.readValue(Long.class.getClassLoader());
        this.steps = new ArrayList<Step>();
        in.readList(this.steps, Step.class.getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
