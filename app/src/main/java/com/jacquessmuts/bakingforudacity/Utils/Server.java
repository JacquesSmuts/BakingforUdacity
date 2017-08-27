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
                    if (TextUtils.isEmpty(step.getThumbnailURL())){
                        return step.getThumbnailURL();
                    }
                }
            }
        }

        return url;
    }


    private static void doRequest(String url, int pageNumber, final ServerListener listener) {
        Log.i(TAG, "url=" + url);
//        String finalUrl = url+API_KEY_APPEND;
//        if (pageNumber > 1){
//            finalUrl += PAGE_NUMBER + pageNumber;
//        }
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

                Headers responseHeaders = response.headers();
//                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                    //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
                String responseString = response.body().string();
                Log.i(TAG, "response=" + responseString);
                listener.serverResponse(responseString);
            }
        });
    }
}
