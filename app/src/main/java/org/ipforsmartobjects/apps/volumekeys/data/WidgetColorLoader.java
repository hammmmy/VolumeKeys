package org.ipforsmartobjects.apps.volumekeys.data;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import org.ipforsmartobjects.apps.volumekeys.data.local.WidgetColorsPersistenceContract;

import java.util.ArrayList;
import java.util.List;

public class WidgetColorLoader extends AsyncTaskLoader<List<WidgetColors>> {
    Context mContext;

    public WidgetColorLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<WidgetColors> loadInBackground() {
        List<WidgetColors> widgetColorsList = new ArrayList<>();
        try {


            Cursor c = mContext.getContentResolver().query(WidgetColorsPersistenceContract.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Long id = Long.parseLong(c.getString(c.getColumnIndexOrThrow(WidgetColorsPersistenceContract.TableWidgetColors.COL_ID)));
                    int bgColor = c.getInt(c.getColumnIndexOrThrow(WidgetColorsPersistenceContract.TableWidgetColors.COL_BACKGROUND_COLOR));
                    int iconBgColor = c.getInt(c.getColumnIndexOrThrow(WidgetColorsPersistenceContract.TableWidgetColors.COL_ICON_BACKGROUND_COLOR));
                    boolean isBlack = c.getInt(c.getColumnIndexOrThrow(WidgetColorsPersistenceContract.TableWidgetColors.COL_IS_BLACK)) == 1;

                    WidgetColors widgetColors = new WidgetColors();
                    widgetColors.setId(id);
                    widgetColors.setBackgroundColor(bgColor);
                    widgetColors.setIconBackgroundColor(iconBgColor);
                    widgetColors.setIsBlack(isBlack);
                    widgetColorsList.add(widgetColors);
                }
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widgetColorsList;
    }
}
