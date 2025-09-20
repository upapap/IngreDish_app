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

import java.util.HashSet;
import java.util.Set;

public class StarActivity extends AppCompatActivity {

    LinearLayout starContainer;
    TextView recipeDetailView;
    SharedPreferences prefs;
    Set<String> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_star);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.star), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        starContainer = findViewById(R.id.star_container);
        recipeDetailView = findViewById(R.id.star_recipe_detail);

        prefs = getSharedPreferences("favorites", MODE_PRIVATE);
        favorites = new HashSet<>(prefs.getStringSet("recipes", new HashSet<>()));

        DBHelper dbHelper = new DBHelper(this);

        if (favorites.isEmpty()) {
            TextView noStar = new TextView(this);
            noStar.setText("즐겨찾기된 요리가 없습니다.");
            noStar.setTextSize(18);
            noStar.setPadding(16, 32, 16, 32);
            starContainer.addView(noStar);
        } else {
            for (String recipe : favorites) {
                LinearLayout itemRow = new LinearLayout(this);
                itemRow.setOrientation(LinearLayout.HORIZONTAL);

                TextView recipeItem = new TextView(this);
                recipeItem.setText(recipe);
                recipeItem.setTextSize(18);
                recipeItem.setTextColor(Color.BLACK);
                recipeItem.setPadding(0, 16, 0, 16);
                recipeItem.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                recipeItem.setClickable(true);

                ImageButton removeStar = new ImageButton(this);
                removeStar.setImageResource(android.R.drawable.btn_star_big_on);
                removeStar.setBackgroundColor(Color.TRANSPARENT);
                removeStar.setOnClickListener(v -> {
                    favorites.remove(recipe);
                    prefs.edit().putStringSet("recipes", favorites).apply();
                    starContainer.removeView(itemRow);
                });

                recipeItem.setOnClickListener(v -> {
                    String fullRecipe = dbHelper.getFullRecipeByMenu(recipe);
                    recipeDetailView.setText(fullRecipe + "\n\n(이 화면을 클릭하면 뒤로 돌아갑니다)");
                    recipeDetailView.setVisibility(View.VISIBLE);
                    starContainer.setVisibility(View.GONE);

                    recipeDetailView.setOnClickListener(view -> {
                        recipeDetailView.setVisibility(View.GONE);
                        starContainer.setVisibility(View.VISIBLE);
                    });
                });

                itemRow.addView(recipeItem);
                itemRow.addView(removeStar);
                starContainer.addView(itemRow);
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
