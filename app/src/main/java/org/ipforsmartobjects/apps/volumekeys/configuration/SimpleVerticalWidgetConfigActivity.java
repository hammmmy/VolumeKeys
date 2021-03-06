package org.ipforsmartobjects.apps.volumekeys.configuration;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.databinding.SimpleWidgetVerticalConfigureBinding;
import org.ipforsmartobjects.apps.volumekeys.widget.SimpleVerticalWidgetProvider;

public class SimpleVerticalWidgetConfigActivity extends SimpleWidgetConfigActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleWidgetVerticalConfigureBinding mBinding = DataBindingUtil.setContentView(SimpleVerticalWidgetConfigActivity.this,
                R.layout.simple_widget_vertical_configure);

        mAddButton = mBinding.addButton;
        mBackgroundRecyclerView = mBinding.backgroundColorRecyclerView;
        mIconBackgroundRecyclerView = mBinding.iconBackgroundColorRecyclerView;
        mLayoutContainer = mBinding.widgetLayout.layoutContainer;
        mMuteButton = mBinding.widgetLayout.mute;
        mVolumeUpButton = mBinding.widgetLayout.volumeUp;
        mVolumeDownButton = mBinding.widgetLayout.volumeDown;
        mMoreButton = mBinding.widgetLayout.actionButtonSettings;
        initConfigActivity(savedInstanceState);
    }

    @Override
    protected void updateAppWidget(Context context) {
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SimpleVerticalWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);
    }
}
