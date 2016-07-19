package sqlite.myrentsqlite.providers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper
{
  static final String TAG = "DbHelper";

  Context context;

  public DbHelper(Context context) {
    super(context, ResidenceContract.DATABASE_NAME, null, ResidenceContract.DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    db.execSQL("CREATE TABLE "
        + ResidenceContract.TABLE_RESIDENCES + " ("
        + ResidenceContract.Column.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
        + ResidenceContract.Column.UUID + " TEXT,"
        + ResidenceContract.Column.GEOLOCATION + " TEXT,"
        + ResidenceContract.Column.DATE + " TEXT,"
        + ResidenceContract.Column.RENTED + " TEXT,"
        + ResidenceContract.Column.TENANT + " TEXT,"
        + ResidenceContract.Column.ZOOM + " TEXT,"
        + ResidenceContract.Column.PHOTO + " TEXT"
        + ");");
  }

  /**
   * Invoked when schema changed.
   * This determined by comparison existing version and old version.
   *
   * @param db         The SQLite database
   * @param oldVersion The previous database version number.
   * @param newVersion The current database version number.
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists " + ResidenceContract.TABLE_RESIDENCES);
    Log.d(TAG, "onUpdated");
    onCreate(db);
  }
}