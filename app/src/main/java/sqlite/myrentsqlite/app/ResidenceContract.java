package sqlite.myrentsqlite.app;

import android.net.Uri;
import android.provider.BaseColumns;

public class ResidenceContract
{
  // DB specific constants
  public static final String DB_NAME = "timeline.db"; //
  public static final int DB_VERSION = 1; //
  public static final String TABLE = "status"; //
  public static final String DEFAULT_SORT = Column.PRIMARY_KEY + " Key"; //

  public class Column
  { //
    static final String PRIMARY_KEY = "id";
    static final String GEOLOCATION = "geolocation";
  }
}
