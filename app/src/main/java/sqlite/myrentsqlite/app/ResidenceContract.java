package sqlite.myrentsqlite.app;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jfitzgerald on 18/07/2016.
 */
public class ResidenceContract
{
  // Database specific constants

  static final String TAG = "ResidenceContract";
  static final String DATABASE_NAME = "residences.db";
  static final int DATABASE_VERSION = 1;
  static final String TABLE_RESIDENCES = "tableResidences";

  // Provider specific constants
  public static final String AUTHORITY = "sqlite.myrentsqlite.app.StatusProvider";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_RESIDENCES);
  public static final int STATUS_ITEM = 1;
  public static final int STATUS_DIR = 2;
  public static final String STATUS_TYPE_ITEM = "vnd.android.cursor.item/vnd.sqlite.myrentsqlite.app.provider.status";
  public static final String STATUS_TYPE_DIR = "vnd.android.cursor.dir/vnd.sqlite.myrentsqlite.app.provider.status";
  public static final String DEFAULT_SORT = Column.DATE + " DESC";

  public class Column
  {
    static final String PRIMARY_KEY = "uuid";
    static final String GEOLOCATION = "geolocation";
    static final String DATE = "date";
    static final String RENTED = "rented";
    static final String TENANT = "tenant";
    static final String ZOOM = "zoom";
    static final String PHOTO = "photo";
  }

}
