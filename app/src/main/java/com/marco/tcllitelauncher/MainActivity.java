package com.marco.tcllitelauncher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private LinearLayout dock;
    private TextView clock;
    private TextView date;
    private TextView selectedName;
    private String[] labels;
    private String[] packages;
    private TextView[] tiles;
    private int selected = 0;

    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    private TextView text(String value, int sp, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(sp);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        labels = getResources().getStringArray(R.array.dock_labels);
        packages = getResources().getStringArray(R.array.dock_packages);
        selected = getPreferences(MODE_PRIVATE).getInt("dock_index", 0);
        if (selected < 0 || selected >= labels.length) selected = 0;
        int white = Color.WHITE;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(42, 22, 42, 28);
        root.setBackgroundColor(Color.rgb(8, 11, 18));
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(top, new LinearLayout.LayoutParams(-1, 110));
        LinearLayout greeting = new LinearLayout(this);
        greeting.setOrientation(LinearLayout.VERTICAL);
        top.addView(greeting, new LinearLayout.LayoutParams(0, -1, 1));
        clock = text("", 42, white);
        clock.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        clock.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        greeting.addView(clock, new LinearLayout.LayoutParams(-1, 65));
        date = text("", 16, 0xffaeb6c5);
        date.setGravity(Gravity.LEFT);
        greeting.addView(date);
        addTop(top, "Entradas", "ACTION_INPUTS");
        addTop(top, "Wi-Fi", "ACTION_WIFI");
        addTop(top, "Ajustes", "ACTION_SETTINGS");

        View space = new View(this);
        root.addView(space, new LinearLayout.LayoutParams(1, 0, 1));
        selectedName = text("", 30, white);
        selectedName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        root.addView(selectedName, new LinearLayout.LayoutParams(-1, 75));

        dock = new LinearLayout(this);
        dock.setPadding(12, 14, 12, 14);
        dock.setBackground(bg(0xff202633, 24));
        root.addView(dock, new LinearLayout.LayoutParams(-1, 115));
        tiles = new TextView[labels.length];
        for (int i = 0; i < labels.length; i++) {
            final int index = i;
            TextView tile = text(labels[i], 17, white);
            tile.setFocusable(true);
            tile.setSingleLine(false);
            tile.setPadding(8, 8, 8, 8);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1, 1);
            lp.setMargins(4, 0, 4, 0);
            dock.addView(tile, lp);
            tiles[i] = tile;
            tile.setOnFocusChangeListener((v, focused) -> {
                if (focused) { selected = index; updateFocus(); }
            });
            tile.setOnClickListener(v -> launch(packages[index]));
        }
        refreshTime();
        root.postDelayed(new Runnable() {
            @Override public void run() {
                refreshTime();
                root.postDelayed(this, 30000);
            }
        }, 30000);
        tiles[selected].requestFocus();
        updateFocus();
    }

    private void addTop(LinearLayout parent, String label, String action) {
        TextView button = text(label, 15, Color.WHITE);
        button.setPadding(18, 8, 18, 8);
        button.setFocusable(true);
        button.setBackground(bg(0xff303745, 50));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, 48);
        lp.setMargins(6, 0, 0, 0);
        parent.addView(button, lp);
        button.setOnFocusChangeListener((v, focused) ->
            button.setBackground(bg(focused ? 0xff6b7d9b : 0xff303745, 50)));
        button.setOnClickListener(v -> launch(action));
    }

    private void refreshTime() {
        Date now = new Date();
        clock.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now));
        date.setText(new SimpleDateFormat("EEEE d MMMM yyyy", new Locale("es", "MX")).format(now));
    }

    private void updateFocus() {
        selectedName.setText(labels[selected]);
        getPreferences(MODE_PRIVATE).edit().putInt("dock_index", selected).apply();
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setBackground(bg(i == selected ? 0xff526582 : 0xff303745, 18));
            tiles[i].setScaleX(i == selected ? 1.04f : 1f);
            tiles[i].setScaleY(i == selected ? 1.04f : 1f);
        }
    }

    private void launch(String target) {
        try {
            if ("ACTION_SETTINGS".equals(target)) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            } else if ("ACTION_WIFI".equals(target)) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            } else if ("ACTION_INPUTS".equals(target)) {
                Intent input = new Intent("android.settings.TV_INPUT_SETTINGS");
                if (input.resolveActivity(getPackageManager()) != null) startActivity(input);
                else startActivity(new Intent(Settings.ACTION_SETTINGS));
            } else {
                Intent app = getPackageManager().getLeanbackLaunchIntentForPackage(target);
                if (app == null) app = getPackageManager().getLaunchIntentForPackage(target);
                if (app == null) Toast.makeText(this, "Aplicación no disponible: " + target, Toast.LENGTH_LONG).show();
                else startActivity(app);
            }
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir: " + target, Toast.LENGTH_LONG).show();
        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }
}
