package com.jacquessmuts.bakingforudacity.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.jacquessmuts.bakingforudacity.Activities.MainActivity;
import com.jacquessmuts.bakingforudacity.Activities.RecipeDetailActivity;
import com.jacquessmuts.bakingforudacity.Models.Ingredient;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.R;

/**
 * Created by smuts on 2017/09/14.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    //every time the widget is updated. Clicked. Added. Whatever
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RecipeWidgetService.updateWidgetWithRecipe(context, 0, 0);
    }

    //Generally only called after widget is first added. Initial one-time setup here
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                Recipe recipe, int ingredientIndex) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe, ingredientIndex);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                Recipe recipe, int ingredientIndex) {
        //Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        RemoteViews rv = getIngredientRemoteView(context, recipe, ingredientIndex);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getIngredientRemoteView(Context context, Recipe recipe, int ingredientIndex) {
        // Set the click handler to open the DetailActivity for that recipe
        // or the MainActivity if recipe is invalid
        Intent intent;
        if (recipe == null) {
            intent = new Intent(context, MainActivity.class);
        } else { // Set on click to open the corresponding detail activity
            intent = RecipeDetailActivity.getIntent(context, recipe);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        if (recipe != null) {
            // Update image and text
            //views.setImageViewResource(R.id.widget_plant_image, imgRes);
            views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
            Ingredient ingredient = recipe.getIngredients().get(ingredientIndex);
            if (ingredient != null) {
                views.setTextViewText(R.id.widget_ingredient_name, ingredient.getQuantity() + ingredient.getMeasure() + " " + ingredient.getIngredient());
            }

            views.setOnClickPendingIntent(R.id.next_ingredient, pendingIntent);
        }

        views.setOnClickPendingIntent(R.id.previous_recipe, PendingIntent.getService(context, 0,
                RecipeWidgetService.getIntent(context, -1, 0), PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.next_recipe, PendingIntent.getService(context, 1,
                RecipeWidgetService.getIntent(context, 1, 0), PendingIntent.FLAG_UPDATE_CURRENT));


        views.setOnClickPendingIntent(R.id.previous_ingredient, PendingIntent.getService(context, 2,
                RecipeWidgetService.getIntent(context, 0, -1), PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.next_ingredient, PendingIntent.getService(context, 3,
                RecipeWidgetService.getIntent(context, 0, 1), PendingIntent.FLAG_UPDATE_CURRENT));

        return views;
    }
}
