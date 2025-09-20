package com.example.ingredish_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class MyRecipeActivity extends AppCompatActivity {

    private UserRecipeDBHelper dbHelper;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> recipeList;
    private String selectedRecipeName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        dbHelper = new UserRecipeDBHelper(this);
        listView = findViewById(R.id.recipe_listview);
        Button deleteButton = findViewById(R.id.delete_button);

        recipeList = dbHelper.getRecipeSimpleList(); // 메뉴 이름 + 재료 + 레시피
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, recipeList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 단일 선택 활성화

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = recipeList.get(position);
            selectedRecipeName = selectedItem.split(" ")[0]; // 메뉴 이름만 추출
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        deleteButton.setOnClickListener(v -> {
            if (selectedRecipeName != null) {
                boolean deleted = dbHelper.deleteRecipeByName(selectedRecipeName);
                if (deleted) {
                    Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                    refreshList();
                } else {
                    Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "삭제할 항목을 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshList() {
        recipeList.clear();
        recipeList.addAll(dbHelper.getRecipeSimpleList());
        adapter.notifyDataSetChanged();
        selectedRecipeName = null;
        listView.clearChoices();
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
}
