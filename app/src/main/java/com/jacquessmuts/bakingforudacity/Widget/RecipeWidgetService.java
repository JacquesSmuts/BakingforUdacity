package com.jacquessmuts.bakingforudacity.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;
import com.jacquessmuts.bakingforudacity.Utils.PreferencesManager;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RecipeWidgetService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.jacquessmuts.bakingforudacity.Widget.action.UPDATE_WIDGET";

    private static final String EXTRA_RECIPE_OPERATOR = "com.jacquessmuts.bakingforudacity.Widget.extra.RECIPE_OPERATOR";
    private static final String EXTRA_INGREDIENT_OPERATOR = "com.jacquessmuts.bakingforudacity.Widget.extra.INGREDIENT_OPERATOR";

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    public static Intent getIntent(Context context, int recipeOperator, int ingredientOperator){
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(RecipeWidgetService.ACTION_UPDATE_WIDGET);
        intent.putExtra(EXTRA_RECIPE_OPERATOR, recipeOperator);
        intent.putExtra(EXTRA_INGREDIENT_OPERATOR, ingredientOperator);
        return intent;
    }

    /**
     * Operators are -1, 0, 1, indicating whether to add, substract or ignore current index
     * @see IntentService
     */
    public static void updateWidgetWithRecipe(Context context, int recipeOperator, int ingredientOperator) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(EXTRA_RECIPE_OPERATOR, recipeOperator);
        intent.putExtra(EXTRA_INGREDIENT_OPERATOR, ingredientOperator);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                final int recipeOperator = intent.getIntExtra(EXTRA_RECIPE_OPERATOR, 0);
                final int ingredientOperator = intent.getIntExtra(EXTRA_INGREDIENT_OPERATOR, 0);
                updateWidget(recipeOperator, ingredientOperator);
            }
        }
    }

    private void updateWidget(int recipeOperator, int ingredientOperator) {

        ArrayList<Recipe> recipes = Recipe.getAllFromJson(this);
        PreferencesManager.initializeInstance(this);
        int recipeIndex = PreferencesManager.getInstance().getWidgetRecipeIndex();
        int ingredientIndex = PreferencesManager.getInstance().getWidgetIngredientIndex();

        recipeIndex += recipeOperator;
        ingredientIndex += ingredientOperator;

        if (recipeIndex >= recipes.size()){
            recipeIndex = 0;
        }
        if (recipeIndex < 0) {
            recipeIndex = recipes.size()-1;
        }
        Recipe recipe = recipes.get(recipeIndex);

        if (recipeOperator != 0){
            ingredientIndex = 0;
        }
        if (ingredientIndex >= recipe.getIngredients().size()){
            ingredientIndex = 0;
        }
        if (ingredientIndex < 0) {
            ingredientIndex = recipe.getIngredients().size()-1;
        }

        PreferencesManager.getInstance().setWidgetRecipeIndex(recipeIndex);
        PreferencesManager.getInstance().setWidgetIngredientIndex(ingredientIndex);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Now update all widgets
        RecipeWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds, recipe, ingredientIndex);
    }

}
