package com.example.ingredish_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "recipe_dp.db";
    private static String DB_PATH = "";
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        copyDatabase();
    }

    private void copyDatabase() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            this.getReadableDatabase();
            this.close();
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(DB_PATH);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<String> getRecipesByIngredients(List<String> selectedIngredients) {
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (selectedIngredients == null || selectedIngredients.isEmpty()) {
            return result;
        }

        // SQL 쿼리 동적 생성
        StringBuilder query = new StringBuilder("SELECT menu FROM recipes WHERE 1=1");

        for (int i = 0; i < selectedIngredients.size(); i++) {
            query.append(" AND ingredients LIKE ?");
        }

        String[] params = new String[selectedIngredients.size()];
        for (int i = 0; i < selectedIngredients.size(); i++) {
            params[i] = "%" + selectedIngredients.get(i) + "%";
        }

        Cursor cursor = db.rawQuery(query.toString(), params);
        while (cursor.moveToNext()) {
            String menu = cursor.getString(0);
            result.add(menu);
        }
        cursor.close();

        return result;
    }

    public String getFullRecipeByMenu(String menu) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT recipe FROM recipes WHERE menu = ?", new String[]{menu});
        String result = "레시피 없음";
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        cursor.close();
        return result;
    }
}