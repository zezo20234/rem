package com.homescreen.tv;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private LinearLayout root, appRow;
    private int white = Color.WHITE;
    private int muted = Color.rgb(175, 185, 205);
    private int blue = Color.rgb(75, 135, 255);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        buildScreen();
    }

    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(radius); return d;
    }

    private TextView label(String text, int size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(text); v.setTextSize(size); v.setTextColor(color);
        v.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return v;
    }

    private void buildScreen() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(58, 34, 58, 28);
        root.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,
            new int[]{Color.rgb(12,20,42), Color.rgb(20,31,61), Color.rgb(9,13,28)}));
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = label("HOME SCREEN", 25, white, true);
        top.addView(title, new LinearLayout.LayoutParams(0, 64, 1));
        TextView date = label(new SimpleDateFormat("EEE, d MMM  •  h:mm a", Locale.getDefault()).format(new Date()), 17, muted, false);
        date.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        top.addView(date);
        root.addView(top);

        TextView greeting = label("Your TV. Your space.", 34, white, true);
        LinearLayout.LayoutParams gp = new LinearLayout.LayoutParams(-1, 76);
        gp.topMargin = 26;
        root.addView(greeting, gp);

        TextView section = label("YOUR APPS", 15, muted, true);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, 38);
        sp.topMargin = 16;
        root.addView(section, sp);

        ScrollView scroll = new ScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);
        appRow = new LinearLayout(this);
        appRow.setOrientation(LinearLayout.HORIZONTAL);
        appRow.setPadding(4, 12, 4, 18);
        scroll.addView(appRow);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 180));
        loadApps();

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER_VERTICAL);
        bottom.setPadding(0, 18, 0, 0);
        TextView hint = label("Use your remote to move and select an app", 15, muted, false);
        bottom.addView(hint, new LinearLayout.LayoutParams(0, 55, 1));
        TextView settings = label("⚙  SETTINGS", 16, white, true);
        settings.setGravity(Gravity.CENTER);
        settings.setPadding(22, 0, 22, 0);
        settings.setBackground(bg(Color.rgb(38,53,83), 24));
        settings.setFocusable(true);
        settings.setOnClickListener(v -> {
            try { startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)); }
            catch (Exception ignored) {}
        });
        bottom.addView(settings, new LinearLayout.LayoutParams(190, 55));
        root.addView(bottom);
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        if (apps == null || apps.isEmpty()) {
            intent.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = pm.queryIntentActivities(intent, 0);
        }
        for (ResolveInfo info : apps) {
            final String pkg = info.activityInfo.packageName;
            String name = info.loadLabel(pm).toString();
            LinearLayout tile = new LinearLayout(this);
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setGravity(Gravity.CENTER);
            tile.setPadding(12, 10, 12, 10);
            tile.setBackground(bg(Color.rgb(35,49,78), 20));
            tile.setFocusable(true);
            tile.setClickable(true);
            TextView icon = label("", 38, white, true);
            icon.setGravity(Gravity.CENTER);
            try { icon.setCompoundDrawables(null, info.loadIcon(pm), null, null); } catch (Exception ignored) {}
            tile.addView(icon, new LinearLayout.LayoutParams(-1, 76));
            TextView appName = label(name, 14, white, true);
            appName.setGravity(Gravity.CENTER);
            appName.setMaxLines(2);
            tile.addView(appName, new LinearLayout.LayoutParams(-1, 48));
            tile.setOnClickListener(v -> {
                Intent launch = pm.getLaunchIntentForPackage(pkg);
                if (launch != null) startActivity(launch);
            });
            tile.setOnFocusChangeListener((v, focused) -> {
                v.setBackground(bg(focused ? blue : Color.rgb(35,49,78), 20));
                v.setScaleX(focused ? 1.06f : 1f);
                v.setScaleY(focused ? 1.06f : 1f);
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(156, 142);
            lp.rightMargin = 18;
            appRow.addView(tile, lp);
        }
        if (appRow.getChildCount() > 0) appRow.getChildAt(0).requestFocus();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) { buildScreen(); return true; }
        return super.onKeyDown(keyCode, event);
    }
}
