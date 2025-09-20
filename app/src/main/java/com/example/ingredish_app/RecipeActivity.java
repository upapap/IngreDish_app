package com.example.ingredish_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeActivity extends AppCompatActivity {

    LinearLayout recipeContainer;
    TextView recipeDetailView;
    SharedPreferences prefs;
    Set<String> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recipe), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipeContainer = findViewById(R.id.recipe_contain);

        prefs = getSharedPreferences("favorites", MODE_PRIVATE);
        favorites = new HashSet<>(prefs.getStringSet("recipes", new HashSet<>()));

        ArrayList<String> ingredients = getIntent().getStringArrayListExtra("selectedIngredients");
        DBHelper dbHelper = new DBHelper(this);
        List<String> recipes = dbHelper.getRecipesByIngredients(ingredients);

        if (recipes.isEmpty()) {
            TextView noResult = new TextView(this);
            noResult.setText("레시피 없음");
            recipeContainer.addView(noResult);
        } else {
            for (String recipe : recipes) {
                LinearLayout rowLayout = new LinearLayout(this);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView recipeItem = new TextView(this);
                recipeItem.setText(recipe);
                recipeItem.setTextSize(18);
                recipeItem.setPadding(0, 16, 0, 16);
                recipeItem.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                recipeItem.setClickable(true);

                ImageButton starButton = new ImageButton(this);
                boolean isFavorite = favorites.contains(recipe);
                starButton.setImageResource(isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
                starButton.setBackgroundColor(Color.TRANSPARENT);

                starButton.setOnClickListener(v -> {
                    if (favorites.contains(recipe)) {
                        favorites.remove(recipe);
                        starButton.setImageResource(android.R.drawable.btn_star_big_off);
                    } else {
                        favorites.add(recipe);
                        starButton.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                    prefs.edit().putStringSet("recipes", favorites).apply();
                });

                recipeItem.setOnClickListener(v -> {
                    for (int i = 0; i < recipeContainer.getChildCount(); i++) {
                        recipeContainer.getChildAt(i).setVisibility(View.GONE);
                    }

                    if (recipeDetailView == null) {
                        recipeDetailView = new TextView(this);
                        recipeDetailView.setTextSize(17);
                        recipeDetailView.setPadding(16, 24, 16, 24);
                        recipeDetailView.setBackgroundColor(Color.WHITE);
                        recipeContainer.addView(recipeDetailView);
                    }

                    String fullRecipe = dbHelper.getFullRecipeByMenu(recipe);
                    recipeDetailView.setText(fullRecipe + "\n\n(이 화면을 클릭하면 뒤로 돌아가기)");
                    recipeDetailView.setVisibility(View.VISIBLE);

                    recipeDetailView.setOnClickListener(view -> {
                        recipeDetailView.setVisibility(View.GONE);
                        for (int i = 0; i < recipeContainer.getChildCount(); i++) {
                            View child = recipeContainer.getChildAt(i);
                            if (child != recipeDetailView) {
                                child.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                });

                rowLayout.addView(recipeItem);
                rowLayout.addView(starButton);
                recipeContainer.addView(rowLayout);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_back) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.btn_star) {
            startActivity(new Intent(this, StarActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClicked(View v) {
        Intent intent;

        if (v.getId() == R.id.btn_star) {
            intent = new Intent(this, StarActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_write) {
            intent = new Intent(this, WriteActivity.class);
            startActivity(intent);
        }
    }

}