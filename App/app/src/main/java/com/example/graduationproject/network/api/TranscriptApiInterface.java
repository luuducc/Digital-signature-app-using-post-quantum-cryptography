package com.example.graduationproject.network.api;

import com.example.graduationproject.data.remote.Transcript;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TranscriptApiInterface {
    @GET("/api/transcript/{userId}")
    Call<List<Transcript>> getTranscripts(
        @Path("userId") String userId,
        @Header("Authorization") String accessToken
    );
}
