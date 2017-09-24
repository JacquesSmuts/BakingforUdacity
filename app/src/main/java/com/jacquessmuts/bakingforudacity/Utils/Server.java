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

    public static String findFirstImageInStack(Recipe recipe){

        String url = "";
        url = recipe.getImage();

        if (TextUtils.isEmpty(url)){
            //loop through steps
            if (recipe.getSteps() != null){
                for (Step step : recipe.getSteps()){
                    if (!TextUtils.isEmpty(step.getThumbnailURL())){
                        return step.getThumbnailURL();
                    }
                }
            }
        }

        return url;
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
