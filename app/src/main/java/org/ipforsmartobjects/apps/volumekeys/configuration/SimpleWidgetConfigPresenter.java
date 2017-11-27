package org.ipforsmartobjects.apps.volumekeys.configuration;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import org.ipforsmartobjects.apps.volumekeys.data.WidgetColorLoader;
import org.ipforsmartobjects.apps.volumekeys.data.WidgetColors;
import org.ipforsmartobjects.apps.volumekeys.data.local.WidgetColorsPersistenceContract;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class SimpleWidgetConfigPresenter implements SimpleWidgetConfigContract.Presenter {

    private final SimpleWidgetConfigContract.View mView;
    private final ContentResolver mContentResolver;
    Integer mDefaultBackgroundColor;
    Integer mDefaultBackgroundIconColor;
    Integer mSelectedBackgroundColor;
    Integer mSelectedIconColor;
    private static final int LOADER_ID = 1;

    private final LoaderManager mLoaderManager;
    private final WidgetColorLoader mWidgetColorLoader;

//    private WidgetColors mWidgetColors;

    public SimpleWidgetConfigPresenter(@NonNull SimpleWidgetConfigContract.View view,
                                       @NonNull ContentResolver contentResolver,
                                       @NonNull LoaderManager loaderManager,
                                       @NonNull WidgetColorLoader widgetColorLoader) {
        mView = checkNotNull(view, "view cannot be null!");
        mContentResolver = contentResolver;
        mLoaderManager = loaderManager;
        mWidgetColorLoader = widgetColorLoader;
    }

    @Override
    public void loadDefaultColors(int primaryColor, int primaryDarkColor) {
        setDefaultWidgetColors(primaryColor, primaryDarkColor);
    }

    @Override
    public void onWidgetBackgroundColorSelected(Integer rgbColor) {
        mSelectedBackgroundColor = rgbColor;
        mView.updateBackgroundColor(rgbColor);

    }

    @Override
    public void onIconBackgroundColorSelected(Integer rgbColor) {
        mSelectedIconColor = rgbColor;
        mView.updateIconBackgroundColor(rgbColor);
    }

    @Override
    public void saveWidgetDetails(int widgetId) {
        setWidgetColorsInProvider(widgetId, true);
        mView.saveWidgetDetails(mSelectedBackgroundColor, mSelectedIconColor);
    }

    @Override
    public void onWidgetDeleted(int widgetId) {
        setWidgetColorsInProvider(widgetId, false);
    }

    public void setWidgetColorsInProvider(int widgetId, boolean save) {

        if (save) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_ID, Long.toString(widgetId));
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_BACKGROUND_COLOR, mSelectedBackgroundColor);
            contentValues.put(WidgetColorsPersistenceContract.TableWidgetColors.COL_ICON_BACKGROUND_COLOR, mSelectedIconColor);
            boolean success = mContentResolver.insert(WidgetColorsPersistenceContract.CONTENT_URI, contentValues) != null;
        } else {
            boolean success = mContentResolver.delete(ContentUris.withAppendedId(WidgetColorsPersistenceContract.CONTENT_URI, widgetId), null, null) > 0;
        }
    }

    public void setDefaultWidgetColors(final int primaryColor, final int primaryDarkColor) {

        mLoaderManager.initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<WidgetColors>>() {
            @Override
            public Loader<List<WidgetColors>> onCreateLoader(int id, Bundle args) {
                return mWidgetColorLoader;
            }

            @Override
            public void onLoadFinished(Loader<List<WidgetColors>> loader, List<WidgetColors> colorsList) {
                if (colorsList != null && colorsList.size() > 1) {
                    WidgetColors color = colorsList.get(colorsList.size() - 1);

                    mSelectedBackgroundColor = mDefaultBackgroundColor = color.getBackgroundColor();
                    mSelectedIconColor = mDefaultBackgroundIconColor = color.getIconBackgroundColor();
                    mView.updateBackgroundColor(mSelectedBackgroundColor);
                    mView.updateIconBackgroundColor(mSelectedIconColor);
                } else {

                    mSelectedBackgroundColor = mDefaultBackgroundColor = primaryColor;
                    mSelectedIconColor = mDefaultBackgroundIconColor = primaryDarkColor;
                    mView.updateBackgroundColor(mSelectedBackgroundColor);
                    mView.updateIconBackgroundColor(mSelectedIconColor);

                }
            }

            @Override
            public void onLoaderReset(Loader<List<WidgetColors>> loader) {

            }
        });
    }
}
