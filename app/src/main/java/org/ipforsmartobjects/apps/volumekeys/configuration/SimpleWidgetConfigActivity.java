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
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.data.WidgetColorLoader;
import org.ipforsmartobjects.apps.volumekeys.data.WidgetColors;
import org.ipforsmartobjects.apps.volumekeys.databinding.SimpleWidgetConfigureBinding;
import org.ipforsmartobjects.apps.volumekeys.widget.SimpleWidgetProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleWidgetConfigActivity extends AppCompatActivity implements SimpleWidgetConfigContract.View {

    SimpleWidgetConfigPresenter mActionsListener;

//    SimpleWidgetConfigureBinding mBinding;
    protected int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ArrayList<Integer> mBackgroundColors = new ArrayList<>();
    ArrayList<Integer> mIconBackgroundColors = new ArrayList<>();
    HashMap<Integer, Boolean> mIconColorMap = new HashMap<>();


    private static final String PREFS_NAME = "org.ipforsmartobjects.apps.volumekeys.configuration.SimpleWidgetConfigActivity";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String BG_COLORS_PREF_PREFIX_KEY = "bg_colors_appwidget_";
    private static final String ICON_BG_COLORS_PREF_PREFIX_KEY = "icon_bg_colors_appwidget_";
    private static final String IS_BLACK_PREF_PREFIX_KEY = "is_black_colors_appwidget_";


    protected final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            mActionsListener.saveWidgetDetails(mAppWidgetId);
        }
    };
    protected Button mAddButton;
    protected RecyclerView mBackgroundRecyclerView;
    protected RecyclerView mIconBackgroundRecyclerView;
    protected View mLayoutContainer;
    protected ImageView mMuteButton;
    protected ImageView mVolumeUpButton;
    protected ImageView mVolumeDownButton;
    protected ImageView mMoreButton;


    // Write the prefix to the SharedPreferences object for this widget
    private static void saveColorPrefs(Context context, int appWidgetId, int bgColor, int iconBgColor, boolean isBlack) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(BG_COLORS_PREF_PREFIX_KEY + appWidgetId, bgColor);
        prefs.putInt(ICON_BG_COLORS_PREF_PREFIX_KEY + appWidgetId, iconBgColor);
        prefs.putBoolean(IS_BLACK_PREF_PREFIX_KEY + appWidgetId, isBlack);

        prefs.apply();
    }
    public static Integer loadBackgrooundColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(BG_COLORS_PREF_PREFIX_KEY + appWidgetId, -1);
    }

    public static Integer loadIconBackgrooundColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(ICON_BG_COLORS_PREF_PREFIX_KEY + appWidgetId, -1);
    }

    public static boolean loadIsBlackColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(IS_BLACK_PREF_PREFIX_KEY + appWidgetId, false);
    }

    public static void deleteColorPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(BG_COLORS_PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(ICON_BG_COLORS_PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(IS_BLACK_PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        SimpleWidgetConfigureBinding mBinding = DataBindingUtil.setContentView(SimpleWidgetConfigActivity.this,
                R.layout.simple_widget_configure);

        mAddButton = mBinding.addButton;
        mBackgroundRecyclerView = mBinding.backgroundColorRecyclerView;
        mIconBackgroundRecyclerView = mBinding.iconBackgroundColorRecyclerView;
        mLayoutContainer = mBinding.widgetLayout.layoutContainer;
        mMuteButton = mBinding.widgetLayout.mute;
        mVolumeUpButton = mBinding.widgetLayout.volumeUp;
        mVolumeDownButton = mBinding.widgetLayout.volumeDown;
        mMoreButton = mBinding.widgetLayout.actionButtonSettings;

        initConfigActivity();
    }

    protected void initConfigActivity() {
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        initBackgroundColors();

        mAddButton.setOnClickListener(mOnClickListener);

        mActionsListener = new SimpleWidgetConfigPresenter(this, getContentResolver(),
                getSupportLoaderManager(),
                new WidgetColorLoader(this));

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

        int primaryColor = ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.colorPrimary);
        int colorAccent = ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.colorAccent);

        mActionsListener.loadDefaultColors(primaryColor, colorAccent);

        mBackgroundRecyclerView.setHasFixedSize(true);
        ColorPaletteAdapter adapter = new ColorPaletteAdapter(mBackgroundColors, new ColorItemListener(){

            @Override
            public void onColorClick(int color) {
                mActionsListener.onWidgetBackgroundColorSelected(color);
            }
        });
        mBackgroundRecyclerView.setAdapter(adapter);

        mIconBackgroundRecyclerView.setHasFixedSize(true);
        ColorPaletteAdapter iconAdapter = new ColorPaletteAdapter(mIconBackgroundColors, new ColorItemListener(){

            @Override
            public void onColorClick(int color) {
                mActionsListener.onIconBackgroundColorSelected(color);
            }
        });
        mIconBackgroundRecyclerView.setAdapter(iconAdapter);
    }

    @Override
    public void updateBackgroundColor(Integer backgroundColor) {
        mLayoutContainer.setBackgroundColor(backgroundColor);
    }

    @Override
    public void updateIconBackgroundColor(Integer iconBackgroundColor) {
        mMuteButton.setBackgroundColor(iconBackgroundColor);
        mMuteButton.setImageResource(mIconColorMap.get(iconBackgroundColor) ? R.drawable.ic_volume_off_black : R.drawable.ic_volume_off_white);

        mVolumeUpButton.setBackgroundColor(iconBackgroundColor);
        mVolumeUpButton.setImageResource(mIconColorMap.get(iconBackgroundColor) ? R.drawable.ic_add_circle_outline_black : R.drawable.ic_add_circle_outline_white);

        mVolumeDownButton.setBackgroundColor(iconBackgroundColor);
        mVolumeDownButton.setImageResource(mIconColorMap.get(iconBackgroundColor) ? R.drawable.ic_remove_circle_outline_black : R.drawable.ic_remove_circle_outline_white);

        mMoreButton.setBackgroundColor(iconBackgroundColor);
        mMoreButton.setImageResource(mIconColorMap.get(iconBackgroundColor) ? R.drawable.ic_more_black : R.drawable.ic_more_white);

    }

    @Override
    public void saveWidgetDetails(int bgColor, int iconBgColor) {

        final Context context = SimpleWidgetConfigActivity.this;

        // When the button is clicked, store the string locally
        saveColorPrefs(context, mAppWidgetId, bgColor, iconBgColor, mIconColorMap.get(iconBgColor));
        updateAppWidget(context);


        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    protected void updateAppWidget(Context context) {
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SimpleWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);
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

        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, android.R.color.transparent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.red_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.pink_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.purple_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.deep_purple_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.indigo_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.blue_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.light_blue_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.cyan_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.teal_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.green_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.light_green_accent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.lime_accent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.yellow_accent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.amber_accent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.orange_accent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.deep_orange_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.brown_accent), false);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.grey_accent), true);
        mIconColorMap.put(ContextCompat.getColor(SimpleWidgetConfigActivity.this, R.color.blue_grey_accent), false);

    }


    interface ColorItemListener {
        void onColorClick(int color);
    }

}
