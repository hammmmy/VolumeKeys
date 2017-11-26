package org.ipforsmartobjects.apps.volumekeys.configuration;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.data.WidgetColors;
import org.ipforsmartobjects.apps.volumekeys.databinding.SimpleWidgetConfigureBinding;
import org.ipforsmartobjects.apps.volumekeys.widget.SimpleWidgetProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SimpleWidgetConfigActivity extends AppCompatActivity implements SimpleWidgetConfigContract.View {

    SimpleWidgetConfigPresenter mActionsListener;

    SimpleWidgetConfigureBinding mBinding;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ArrayList<Integer> mBackgroundColors = new ArrayList<>();
    ArrayList<Integer> mIconBackgroundColors = new ArrayList<>();


    private static final String PREFS_NAME = "org.ipforsmartobjects.apps.volumekeys.configuration.SimpleWidgetConfigActivity";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String COLORS_PREF_PREFIX_KEY = "colors_appwidget_";

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO: 27/11/2017 get colors of the widget and save them
            mActionsListener.saveWidgetDetails();
        }
    };


    // Write the prefix to the SharedPreferences object for this widget
    private static void saveColorPrefs(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(COLORS_PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    private static ArrayList<Integer> fromJson(String jsonString) {
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        return new Gson().fromJson(jsonString, type);
    }
    static ArrayList<Integer> loadColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String colorsListJson = prefs.getString(COLORS_PREF_PREFIX_KEY + appWidgetId, null);
        if (colorsListJson != null) {
            return fromJson(colorsListJson);
        } else {
            return new ArrayList<>();
        }
    }

    static void deleteColorPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(COLORS_PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        mBinding = DataBindingUtil.setContentView(SimpleWidgetConfigActivity.this,
                R.layout.simple_widget_configure);
        initBackgroundColors();

        mBinding.addButton.setOnClickListener(mOnClickListener);

        mActionsListener = new SimpleWidgetConfigPresenter(this, getContentResolver());

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mActionsListener.loadDefaultColors(mAppWidgetId);
        RecyclerView backgroundRecyclerView = mBinding.backgroundColorRecyclerView;
        backgroundRecyclerView.setHasFixedSize(true);
        ColorPaletteAdapter adapter = new ColorPaletteAdapter(mBackgroundColors);
        backgroundRecyclerView.setAdapter(adapter);

        RecyclerView iconBackgroundRecyclerView = mBinding.iconBackgroundColorRecyclerView;
        iconBackgroundRecyclerView.setHasFixedSize(true);
        ColorPaletteAdapter iconAdapter = new ColorPaletteAdapter(mIconBackgroundColors);
        iconBackgroundRecyclerView.setAdapter(iconAdapter);
    }

    @Override
    public void updateBackgroundColor(Integer backgroundColor) {
        mBinding.widgetLayout.layoutContainer.setBackgroundColor(backgroundColor);
    }

    @Override
    public void updateIconBackgroundColor(Integer iconBackgroundColor, boolean isBlackIcon) {
        mBinding.widgetLayout.mute.setBackgroundColor(iconBackgroundColor);
        mBinding.widgetLayout.mute.setImageResource(isBlackIcon ? R.drawable.ic_volume_off_black : R.drawable.ic_volume_off_white);

        mBinding.widgetLayout.volumeUp.setBackgroundColor(iconBackgroundColor);
        mBinding.widgetLayout.volumeUp.setImageResource(isBlackIcon ? R.drawable.ic_volume_off_black : R.drawable.ic_volume_off_white);

        mBinding.widgetLayout.volumeDown.setBackgroundColor(iconBackgroundColor);
        mBinding.widgetLayout.volumeDown.setImageResource(isBlackIcon ? R.drawable.ic_volume_off_black : R.drawable.ic_volume_off_white);
    }

    @Override
    public void saveWidgetDetails(WidgetColors colors) {

        final Context context = SimpleWidgetConfigActivity.this;

        // When the button is clicked, store the string locally
        mActionsListener.saveWidgetDetails();

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SimpleWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    void initBackgroundColors() {

        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, android.R.color.transparent));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.red_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.pink_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.purple_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.deep_purple_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.indigo_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.blue_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.light_blue_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.cyan_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.teal_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.green_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.light_green_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.lime_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.yellow_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.amber_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.orange_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.deep_orange_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.brown_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.grey_primary));
        mBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.blue_grey_primary));

        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, android.R.color.transparent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.red_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.pink_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.purple_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.deep_purple_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.indigo_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.blue_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.light_blue_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.cyan_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.teal_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.green_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.light_green_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.lime_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.yellow_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.amber_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.orange_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.deep_orange_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.brown_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.grey_accent));
        mIconBackgroundColors.add(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.blue_grey_accent));
    }
}
