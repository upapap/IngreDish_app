package com.example.ingredish_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WriteActivity extends AppCompatActivity {

    private UserRecipeDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_write);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.write), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new UserRecipeDBHelper(this);

        EditText writeName = findViewById(R.id.writeName);
        EditText writeIngre = findViewById(R.id.writeIngre);
        EditText writeRecipe = findViewById(R.id.writeRecipe);
        Button writeButton = findViewById(R.id.write_button);

        writeButton.setOnClickListener(v -> {
            String name = writeName.getText().toString().trim();
            String ingredients = writeIngre.getText().toString().trim();
            String recipe = writeRecipe.getText().toString().trim();

            if (name.isEmpty() || ingredients.isEmpty() || recipe.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertRecipe(name, ingredients, recipe);
            if (inserted) {
                Toast.makeText(this, "레시피 저장 성공!", Toast.LENGTH_SHORT).show();
                writeName.setText("");
                writeIngre.setText("");
                writeRecipe.setText("");

                Intent intent = new Intent(this, MyRecipeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "레시피 저장 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_back) {
            finish();
            return true;
        } else if (id == R.id.btn_next) {
            Intent intent = new Intent(this, MyRecipeActivity.class);
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