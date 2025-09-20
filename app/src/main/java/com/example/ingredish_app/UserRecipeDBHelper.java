package com.example.ingredish_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UserRecipeDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user_recipe.db";  // 유저용 DB 이름
    private static final int DB_VERSION = 1;

    public UserRecipeDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    //삭제
    public boolean deleteRecipeByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("recipes", "menu = ?", new String[]{name});
        return result > 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "menu TEXT, " +
                "ingredients TEXT, " +
                "recipe TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }


    public boolean insertRecipe(String menu, String ingredients, String recipe) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("menu", menu);
        values.put("ingredients", ingredients);
        values.put("recipe", recipe);

        long result = db.insert("recipes", null, values);
        return result != -1;
    }

    public List<String> getRecipeSimpleList() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT menu, ingredients, recipe FROM recipes", null);

        while (cursor.moveToNext()) {
            String menu = cursor.getString(0);
            String ingredients = cursor.getString(1);
            String recipe = cursor.getString(2);
            list.add(menu + "   " + ingredients + "   " + recipe);
        }
        cursor.close();
        return list;
    }
}
