package org.ipforsmartobjects.apps.volumekeys.configuration;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.volumekeys.data.WidgetColors;
import org.ipforsmartobjects.apps.volumekeys.data.local.WidgetColorsPersistenceContract;

import static com.google.common.base.Preconditions.checkNotNull;


public class SimpleWidgetConfigPresenter implements SimpleWidgetConfigContract.Presenter {

    private final SimpleWidgetConfigContract.View mView;
    private final ContentResolver mContentResolver;
    private WidgetColors mWidgetColors;

    public SimpleWidgetConfigPresenter(@NonNull SimpleWidgetConfigContract.View view,
                                @NonNull ContentResolver contentResolver) {
        mView = checkNotNull(view, "view cannot be null!");
        mContentResolver = contentResolver;
    }

    @Override
    public WidgetColors loadDefaultColors(long widgetId) {
        mWidgetColors = getWidgetColors(widgetId);
        return mWidgetColors;
    }

    @Override
    public void onWidgetBackgroundColorSelected(Integer rgbColor) {
        mView.updateBackgroundColor(rgbColor);

    }

    @Override
    public void onIconBackgroundColorSelected(Integer rgbColor) {
        mView.updateIconBackgroundColor(rgbColor, false); // TODO: 27/11/2017 add black icon support

    }

    @Override
    public void saveWidgetDetails() {
        setWidgetColorsInProvider(true);
    }

    @Override
    public void onWidgetDeleted() {
        setWidgetColorsInProvider(false);
    }

    public void setWidgetColorsInProvider(boolean save) {

        if (save) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_ID, Long.toString(mWidgetColors.getId()));
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_BACKGROUND_COLOR, mWidgetColors.getBackgroundColor());
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_ICON_BACKGROUND_COLOR, mWidgetColors.getIconBackgroundColor());
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_IS_BLACK, mWidgetColors.getIsBlack() == true ? 1 : 0);
            boolean success = mContentResolver.insert(WidgetColorsPersistenceContract.CONTENT_URI, contentValues) != null;
        } else {
            boolean success = mContentResolver.delete(ContentUris.withAppendedId(WidgetColorsPersistenceContract.CONTENT_URI, mWidgetColors.getId()), null, null) > 0;
        }

    }

    public WidgetColors getWidgetColors(Long id) {
        Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(WidgetColorsPersistenceContract.CONTENT_URI, id),
                null,
                null,
                null,
                null);

        WidgetColors colors = new WidgetColors();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String bgColor = cursor.getString(cursor.getColumnIndex(WidgetColorsPersistenceContract.TableWidgetColors.COL_BACKGROUND_COLOR));
            String iconBgColor = cursor.getString(cursor.getColumnIndex(WidgetColorsPersistenceContract.TableWidgetColors.COL_ICON_BACKGROUND_COLOR));
            boolean isBlack = (cursor.getInt(cursor.getColumnIndex(WidgetColorsPersistenceContract.TableWidgetColors.COL_IS_BLACK))) == 1;
            colors.setBackgroundColor(bgColor);
            colors.setId(id);
            colors.setIconBackgroundColor(iconBgColor);
            colors.setIsBlack(isBlack);
        }

        return colors;
    }


}
