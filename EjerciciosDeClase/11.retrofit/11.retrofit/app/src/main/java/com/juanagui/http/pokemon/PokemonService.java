package com.juanagui.http.pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonService {
    @GET("pokemon-species")
    Call<SpeciesChunk> listSpecies(
            @Query("offset") int offset,
            @Query("limit") int limit);


    @GET("pokemon-species/{name}")
    Call<SpeciesDetails> speciesDetails(@Path("name") String name);
}