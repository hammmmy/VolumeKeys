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
        void updateIconBackgroundColor(Integer iconBackgroundColor, boolean isBlackIcon);
        void saveWidgetDetails(WidgetColors colors);
    }

    interface Presenter {
        WidgetColors loadDefaultColors(long widgetId);
        void onWidgetBackgroundColorSelected(Integer rgbColor);
        void onIconBackgroundColorSelected(Integer rgbColor);
        void saveWidgetDetails();
        void onWidgetDeleted();
    }

}
