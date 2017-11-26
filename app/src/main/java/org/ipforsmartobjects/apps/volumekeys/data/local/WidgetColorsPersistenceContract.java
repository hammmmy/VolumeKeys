package org.ipforsmartobjects.apps.volumekeys.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hamid on 5/4/2017.
 */

public class WidgetColorsPersistenceContract {

    // Database schema information
    public static final String TABLE_COLORS = "colors";
    // Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "org.ipforsmartobjects.apps.volumekeys.data";
    // Base content Uri for accessing the provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_COLORS)
            .build();

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private WidgetColorsPersistenceContract() {
    }

    public static final class TableWidgetColors implements BaseColumns {
        public static final String COL_ID = "_id";
        public static final String COL_BACKGROUND_COLOR = "background_color";
        public static final String COL_ICON_BACKGROUND_COLOR = "icon_background_color";
        public static final String COL_IS_BLACK = "is_black";

    }
}
