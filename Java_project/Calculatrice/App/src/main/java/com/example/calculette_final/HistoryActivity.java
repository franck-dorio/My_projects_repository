package com.example.calculette_final;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private static final String PREFS = "calc_prefs";
    private static final String KEY_HISTORY = "history_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView list = findViewById(R.id.history_list);
        ArrayList<String> liste = loadHistory();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, liste);
        list.setAdapter(adapter);
    }

    private ArrayList<String> loadHistory() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        String json = sp.getString(KEY_HISTORY, "[]");
        ArrayList<String> liste = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) liste.add(arr.getString(i));
        } catch (JSONException ignored) {}
        return liste;
    }
}
