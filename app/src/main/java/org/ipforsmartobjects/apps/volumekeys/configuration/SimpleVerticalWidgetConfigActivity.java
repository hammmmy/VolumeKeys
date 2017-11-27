package org.ipforsmartobjects.apps.volumekeys.configuration;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.data.WidgetColorLoader;

public class SimpleVerticalWidgetConfigActivity extends SimpleWidgetConfigActivity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        mBinding = DataBindingUtil.setContentView(SimpleVerticalWidgetConfigActivity.this,
                R.layout.simple_widget_vertical_configure);
        initConfigActivity();
    }
}
