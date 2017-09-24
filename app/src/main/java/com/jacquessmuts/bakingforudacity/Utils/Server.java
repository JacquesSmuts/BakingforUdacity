package com.jacquessmuts.bakingforudacity.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.jacquessmuts.bakingforudacity.Models.Ingredient;
import com.jacquessmuts.bakingforudacity.Models.Recipe;
import com.jacquessmuts.bakingforudacity.Models.Step;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by smuts on 2017/08/27.
 */

public class Server {

    private static final String TAG = "Network Operation";

    private static final String RECIPE_URL = "http://go.udacity.com/android-baking-app-json";

    private static OkHttpClient mClient = new OkHttpClient();

    public interface ServerListener{
        void serverResponse(String response);
    }

    /**
     * Returns the url for the thumbnail image from a given Step.
     * @param step
     * @return
     */
    public static String findThumbnailImageFromStep(Step step){
        return step.getThumbnailURL();
    }

    /**
     * This function gives an image for a recipe. Specifically the first image it can find in the stack.
     * Slightly pointless since all recipes are without images.
     * If you want to get the thumbnailURL for a given step, do not use this function.
     * Instead, use findThumbnailImageFromStep
     * @param recipe
     * @return
     */
    public static String findFirstImageInStack(Recipe recipe){

        String url = "";
        url = recipe.getImage();

        //sometimes (always) the recipe has no image. In that case:
        if (TextUtils.isEmpty(url)){ //recipe has no images
            //loop through steps
            if (recipe.getSteps() != null){
                for (Step step : recipe.getSteps()){
                    if (!TextUtils.isEmpty(step.getThumbnailURL())){ //if there is a thumbnail URL
                        return step.getThumbnailURL(); //set the image as the thumbnail URL
                    }
                }
            }
        }

        //In the case that both the recipe and one of the steps has a relevant image,
        // only the recipe image will display. That is by design.

        return url; //return an imageURL of whatever you can find
    }

    public static void getRecipes(ServerListener listener){
        doRequest(RECIPE_URL, listener);
    }

    private static void doRequest(String url, final ServerListener listener) {
        Log.i(TAG, "url=" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseString = response.body().string();
                Log.i(TAG, "response=" + responseString);
                listener.serverResponse(responseString);
            }
        });
    }
}
