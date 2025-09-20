package com.example.ingredish_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class IngreActivity extends AppCompatActivity {

    int[] buttonIds = {
            R.id.meat1, R.id.meat2, R.id.meat3, R.id.meat4, R.id.meat5, R.id.meat6,
            R.id.vg1, R.id.vg2, R.id.vg3, R.id.vg4, R.id.vg5, R.id.vg6, R.id.vg7, R.id.vg8, R.id.vg9, R.id.vg10, R.id.vg11, R.id.vg12,
            R.id.fi1, R.id.fi2, R.id.fi3, R.id.fi4, R.id.fi5, R.id.fi6,
            R.id.ga1, R.id.ga2, R.id.ga3, R.id.ga4, R.id.ga5, R.id.ga6, R.id.ga7, R.id.ga8, R.id.ga9,
            R.id.gi1, R.id.gi2, R.id.gi3, R.id.gi4, R.id.gi5, R.id.gi6
    };

    String[] ingredientNames = {
            "돼지", "소고기", "닭", "계란", "오리", "",
            "양파", "대파", "당근", "마늘", "감자", "고구마", "무", "콩나물", "버섯", "파프리카", "애호박", "고추",
            "고등어", "멸치", "새우", "오징어", "", "",
            "참치캔", "베이컨", "스팸", "떡", "김치", "두부", "오뎅", "", "",
            "당면", "파스타면", "밥", "소면", "라면", "부침가루"
    };

    boolean[] isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingre);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ingre), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int defaultColor = Color.parseColor("#E4E1E1");
        int selectedColor = Color.parseColor("#9A9696");
        int textColor = Color.BLACK;

        isSelected = new boolean[buttonIds.length];

        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;
            Button button = findViewById(buttonIds[i]);
            if (button == null) continue;

            String buttonText = button.getText().toString().trim();
            if (buttonText.isEmpty()) {
                button.setEnabled(false);
                button.setVisibility(View.INVISIBLE);
                continue;
            }

            button.setBackgroundColor(defaultColor);
            button.setTextColor(textColor);

            button.setOnClickListener(v -> {
                if (!isSelected[index]) {
                    button.setBackgroundColor(selectedColor);
                    isSelected[index] = true;
                } else {
                    button.setBackgroundColor(defaultColor);
                    isSelected[index] = false;
                }
            });
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
        } else if (item.getItemId() == R.id.btn_next) {
            ArrayList<String> selectedIngredients = new ArrayList<>();
            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) selectedIngredients.add(ingredientNames[i]);
            }
            Intent intent = new Intent(IngreActivity.this, RecipeActivity.class);
            intent.putStringArrayListExtra("selectedIngredients", selectedIngredients);
            startActivity(intent);
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
