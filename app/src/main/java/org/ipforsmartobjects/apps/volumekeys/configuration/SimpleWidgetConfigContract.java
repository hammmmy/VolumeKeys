package org.ipforsmartobjects.apps.volumekeys.configuration;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.volumekeys.data.WidgetColors;

/**
 * This specifies the contract between the view, presenter, and the model.
 * reference : https://codelabs.developers.google.com/codelabs/android-testing/index.html
 */
public interface SimpleWidgetConfigContract {

    interface View {
        void updateBackgroundColor(Integer backgroundColor);
        void updateIconBackgroundColor(Integer iconBackgroundColor);
        void saveWidgetDetails(int bgColor, int iconBgColor);
    }

    interface Presenter {
        void loadDefaultColors();
        void onWidgetBackgroundColorSelected(Integer rgbColor);
        void onIconBackgroundColorSelected(Integer rgbColor);
        void saveWidgetDetails(int widgetId);
        void onWidgetDeleted(int widgetId);
    }

}
