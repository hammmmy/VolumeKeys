package org.ipforsmartobjects.apps.volumekeys.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WidgetColorsDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Colors.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = WidgetColorsDBHelper.class.getSimpleName();

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE_COLORS = "CREATE TABLE " +
            WidgetColorsPersistenceContract.TABLE_COLORS + " (" +
            WidgetColorsPersistenceContract.TableWidgetColors._ID + TEXT_TYPE + PRIMARY_KEY + COMMA_SEP +
            WidgetColorsPersistenceContract.TableWidgetColors.COL_BACKGROUND_COLOR + INTEGER_TYPE + COMMA_SEP +
            WidgetColorsPersistenceContract.TableWidgetColors.COL_ICON_BACKGROUND_COLOR + INTEGER_TYPE + COMMA_SEP +
            WidgetColorsPersistenceContract.TableWidgetColors.COL_IS_BLACK + INTEGER_TYPE +
            " )";

    public WidgetColorsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_COLORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WidgetColorsPersistenceContract.TABLE_COLORS);
        onCreate(db);
    }
}
