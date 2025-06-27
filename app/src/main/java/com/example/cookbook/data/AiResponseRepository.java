package com.example.cookbook.data;

import android.util.Log;

import com.example.cookbook.network.OpenAiRequest;
import com.example.cookbook.network.OpenAiResponse;
import com.example.cookbook.network.OpenAiService;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AiResponseRepository {

    private final OpenAiService api;

    public AiResponseRepository() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cookbook-ai.free.beeceptor.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.api = retrofit.create(OpenAiService.class);
    }

    public void askAi(String recipeText, String userQuestion, AiCallback callback) {
        OpenAiRequest request = new OpenAiRequest(
                "gpt-3.5-turbo",
                Arrays.asList(
                        new OpenAiRequest.Message("system", "Ti si kuhar za deserte. Pomozi korisniku sa receptom."),
                        new OpenAiRequest.Message("user", "Recept:\n" + recipeText),
                        new OpenAiRequest.Message("user", userQuestion)
                )
        );

        api.getChatCompletion(request).enqueue(new Callback<OpenAiResponse>() {
            @Override
            public void onResponse(Call<OpenAiResponse> call, Response<OpenAiResponse> response) {
                Log.e("AI", "Kod: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String answer = response.body().choices.get(0).message.content.trim();
                    callback.onSuccess(answer);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Log.e("AI", "Greška: " + response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onError("Neuspješan AI odgovor.");
                }
            }

            @Override
            public void onFailure(Call<OpenAiResponse> call, Throwable t) {
                callback.onError("Greška: " + t.getMessage());
            }
        });
    }

    public interface AiCallback {
        void onSuccess(String answer);
        void onError(String error);
    }
}
