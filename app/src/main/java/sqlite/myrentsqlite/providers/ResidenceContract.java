package sqlite.myrentsqlite.providers;

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
  public static final String AUTHORITY = "sqlite.myrentsqlite.providers.ResidenceProvider";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_RESIDENCES);
  public static final int RESIDENCE_ITEM = 1;
  public static final int RESIDENCE_DIR = 2;
  public static final String STATUS_TYPE_ITEM = "vnd.android.cursor.item/vnd.sqlite.myrentsqlite.providers.provider.status";
  public static final String STATUS_TYPE_DIR = "vnd.android.cursor.dir/vnd.sqlite.myrentsqlite.providers.provider.status";
  public static final String DEFAULT_SORT = Column.DATE + " DESC";

  public class Column
  {
    public static final String ID = BaseColumns._ID;
    public static final String UUID = "uuid";
    public static final String GEOLOCATION = "geolocation";
    public static final String DATE = "date";
    public static final String RENTED = "rented";
    public static final String TENANT = "tenant";
    public static final String ZOOM = "zoom";
    public static final String PHOTO = "photo";
  }

}
