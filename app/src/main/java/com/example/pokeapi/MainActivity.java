package com.example.pokeapi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokeapi.PokeApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private PokeApiService pokeApiService;
    private EditText editTextPokemon;
    private TextView nameTextView, typesTextView, weightTextView, heightTextView, pokedexIdTextView,
            baseXPTextView, moveTextView, abilityTextView;
    private ImageView imageView;

    private ListView listViewPokemons;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokeApiService = RetrofitClient.getClient().create(PokeApiService.class);

        editTextPokemon = findViewById(R.id.editTextPokemon);
        nameTextView = findViewById(R.id.nameTextView);
        typesTextView = findViewById(R.id.typesTextView);
        imageView = findViewById(R.id.imageView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        pokedexIdTextView = findViewById(R.id.pokedexIdTextView);
        baseXPTextView = findViewById(R.id.baseXPTextView);
        moveTextView = findViewById(R.id.moveTextView);
        abilityTextView = findViewById(R.id.abilityTextView);

        Button listViewClear = findViewById(R.id.listViewClear);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button clearPokemon = findViewById(R.id.clearPokemon);
        listViewPokemons = findViewById(R.id.listViewPokemons);

        // Initialize the ArrayList to store Pokemon names
        pokemonList = new ArrayList<>();

        // Initialize the ArrayAdapter and set it to the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pokemonList);
        listViewPokemons.setAdapter(adapter);

        listViewPokemons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedPokemon = pokemonList.get(position);
                fetchPokemon(selectedPokemon);
            }
        });

        listViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearListView();
            }
        });
        clearPokemon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                clearDisplay();
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editTextPokemon.getText().toString().trim();

                if (!input.isEmpty()) {
                    if (input.matches("\\d+")) {
                        // If the input contains only digits, treat it as Pokedex ID
                        int pokedexId = Integer.parseInt(input);
                        if(pokedexId < 1011 && pokedexId > 0) {
                            fetchPokemonById(pokedexId);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Error in digits", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (containsSpecialCharacters(input) != true){
                            // If it's not a number, treat it as a Pokemon name
                            String pokemonName = input.toLowerCase();
                            fetchPokemon(pokemonName);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Contains a special character", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a Pokemon name or Pokedex ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchPokemon(String pokemonName) {

        Call<Pokemon> call = pokeApiService.getPokemon(pokemonName);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    Pokemon pokemon = response.body();
                    displayPokemonData(pokemon);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch Pokemon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchPokemonById(int pokedexId) {
        Call<Pokemon> call = pokeApiService.getPokemonById(pokedexId);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    Pokemon pokemon = response.body();
                    displayPokemonData(pokemon);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch Pokemon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPokemonData(Pokemon pokemon) {
        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(pokemon.getName());
        addPokemonToList(pokemon.getName().toString().trim());
        // Displaying Types
        StringBuilder typesText = new StringBuilder("Types: ");
        for (Pokemon.Type type : pokemon.getTypes()) {
            typesText.append(type.getType().getName()).append(", ");
        }
        typesText.setLength(typesText.length() - 2); // Remove the last comma
        TextView typesTextView = findViewById(R.id.typesTextView);
        typesTextView.setText(typesText.toString());

        // Displaying Weight and Height
        TextView weightTextView = findViewById(R.id.weightTextView);
        weightTextView.setText("Weight: " + pokemon.getWeight() + " kg");

        TextView heightTextView = findViewById(R.id.heightTextView);
        heightTextView.setText("Height: " + pokemon.getHeight() + " m");

        // Displaying Pokedex ID and Base XP
        TextView pokedexIdTextView = findViewById(R.id.pokedexIdTextView);
        pokedexIdTextView.setText("Pokedex ID: " + pokemon.getId());

        TextView baseXPTextView = findViewById(R.id.baseXPTextView);
        baseXPTextView.setText("Base XP: " + pokemon.getBaseExperience());

        // Displaying one Move
        List<Pokemon.Move> moves = pokemon.getMoves();
        if (moves != null && !moves.isEmpty()) {
            TextView moveTextView = findViewById(R.id.moveTextView);
            moveTextView.setText("Move: " + moves.get(0).getMove().getName());
        }

        // Displaying one Ability
        List<Pokemon.Ability> abilities = pokemon.getAbilities();
        if (abilities != null && !abilities.isEmpty()) {
            TextView abilityTextView = findViewById(R.id.abilityTextView);
            abilityTextView.setText("Ability: " + abilities.get(0).getAbility().getName());
        }

        // Load and display the image using Glide (similar to the previous implementation)
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(pokemon.getSprites().getFrontDefault())
                .into(imageView);
    }
    private void addPokemonToList(String pokemonName) {
        if (pokemonList.contains(pokemonName)) {
            Toast.makeText(MainActivity.this, "Pokemon is already in the list", Toast.LENGTH_SHORT).show();
            return; // Exit the method if the Pokemon is already in the list
        }
        // Add the entered Pokemon name to the ArrayList
        pokemonList.add(pokemonName);
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
        Log.d("PokemonAdded", "Added: " + pokemonName);
        Log.d("ListSize", "List size: " + pokemonList.size());
    }
    public static boolean containsSpecialCharacters(String input) {
        // List of special characters to check for
        String specialChars = "-%&*(@)!;:<>";

        // Loop through each character in the input string
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            // Check if the current character exists in the list of special characters
            if (specialChars.indexOf(currentChar) != -1) {
                return true; // Found a special character, return true
            }
        }

        // No special characters found in the input string
        return false;
    }
    private void clearDisplay() {
        // Clear TextViews
        nameTextView.setText("");
        typesTextView.setText("");
        weightTextView.setText("");
        heightTextView.setText("");
        pokedexIdTextView.setText("");
        baseXPTextView.setText("");
        moveTextView.setText("");
        abilityTextView.setText("");

        Glide.with(this).clear(imageView);
    }
    private void clearListView() {
        pokemonList.clear(); // Clear the ArrayList
        adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

}
