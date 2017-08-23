
package com.jacquessmuts.bakingforudacity.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private String ingredient;
    private String measure;
    private Long quantity;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ingredient);
        dest.writeString(this.measure);
        dest.writeValue(this.quantity);
    }

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        this.ingredient = in.readString();
        this.measure = in.readString();
        this.quantity = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
