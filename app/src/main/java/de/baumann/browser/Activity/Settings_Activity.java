package de.baumann.browser.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import de.baumann.browser.Fragment.Fragment_settings;
import de.baumann.browser.Ninja.R;
import de.baumann.browser.Unit.HelperUnit;

public class Settings_Activity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        HelperUnit.setTheme(this);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Fragment_settings fragment = new Fragment_settings();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getInt("restart_changed", 1) == 1) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
