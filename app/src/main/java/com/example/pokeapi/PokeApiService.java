package com.example.pokeapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeApiService {
    @GET("pokemon/{name}")
    Call<Pokemon> getPokemon(@Path("name") String name);
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") int pokedexId);
}
