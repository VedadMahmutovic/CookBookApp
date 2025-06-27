package com.example.cookbook.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAiService {
    /*@Headers({
            "Content-Type: application/json",
            "Authorization: Bearer OPENAI_API_KEY"
    })*/
    @POST("v1/chat/completions")
    Call<OpenAiResponse> getChatCompletion(@Body OpenAiRequest request);
}

