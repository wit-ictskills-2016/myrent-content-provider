package sqlite.myrentsqlite.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by jfitzgerald on 18/07/2016.
 * Adapted from code in Learning Android Edition 2
 * Authors: Gargenta & Nakamura
 */
public class ResidenceProvider extends ContentProvider
{
  private static final String TAG = ResidenceProvider.class.getSimpleName();
  private DbHelper dbHelper;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    uriMatcher.addURI(ResidenceContract.AUTHORITY, ResidenceContract.TABLE_RESIDENCES, ResidenceContract.RESIDENCE_DIR);
    uriMatcher.addURI(ResidenceContract.AUTHORITY, ResidenceContract.TABLE_RESIDENCES + "/#", ResidenceContract.RESIDENCE_ITEM);
  }

  @Override
  public boolean onCreate() {

    dbHelper = new DbHelper(getContext());
    Log.d(TAG, "onCreated");
    return true;
  }

  /**
   * Refer to page 195 Learning Android 2nd Edition for more detailed informtion + following:
   * Android SQLite Database (wish the url not so long).
   * Vogella: http://www.vogella.com/tutorials/AndroidSQLite/article.html#base-uri-of-the-content-provider
   *
   * @param uri Universal Resource Identifier
   * @param projection The set of columns to be returned - null to return all
   * @param selection Filter to specify which rows to return - null to return all
   * @param selectionArgs Replace ?s in selection by strings
   * @param sortOrder How to sort rows - null for default sort
   * @return
   */
  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables( ResidenceContract.TABLE_RESIDENCES ); // Specify table
    switch (uriMatcher.match(uri)) { // Determine uri type
      case ResidenceContract.RESIDENCE_DIR:
        break;
      case ResidenceContract.RESIDENCE_ITEM: // uri contains record id
        qb.appendWhere(ResidenceContract.Column.ID + "=" + uri.getLastPathSegment());
        break;
      default:
        throw new IllegalArgumentException("Illegal uri: " + uri);
    }

    String orderBy = (TextUtils.isEmpty(sortOrder))
        ? ResidenceContract.DEFAULT_SORT
        : sortOrder; // Specify sort order of returned data

    SQLiteDatabase db = dbHelper.getReadableDatabase();
    Cursor cursor = qb.query(db, projection, selection, selectionArgs,
        null, null, orderBy);

    // register for uri changes
    cursor.setNotificationUri(getContext().getContentResolver(), uri); // Cursor data refresh
    Log.d(TAG, "queried records: " + cursor.getCount());
    return cursor;
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case ResidenceContract.RESIDENCE_DIR:
        Log.d(TAG, "gotType: " + ResidenceContract.STATUS_TYPE_DIR);
        return ResidenceContract.STATUS_TYPE_DIR;
      case ResidenceContract.RESIDENCE_ITEM:
        Log.d(TAG, "gotType: " + ResidenceContract.STATUS_TYPE_ITEM);
        return ResidenceContract.STATUS_TYPE_ITEM;
      default:
        throw new IllegalArgumentException("Illegal uri: " + uri);
    }
  }

  @Nullable
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    Uri ret = null;
    // Assert correct uri
    if (uriMatcher.match(uri) != ResidenceContract.RESIDENCE_DIR) {
      throw new IllegalArgumentException("Illegal uri: " + uri);
    }
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    long rowId = db.insertWithOnConflict(ResidenceContract.TABLE_RESIDENCES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    // Was insert successful?
    if (rowId != -1) {
      //Integer id = values.getAsInteger(ResidenceContract.Column.ID);
      //ret = ContentUris.withAppendedId(uri, id);
      ret = ContentUris.withAppendedId(uri, rowId);
      Log.d(TAG, "inserted uri: " + ret);
      // Notify that data for this uri has changed
      getContext().getContentResolver()
          .notifyChange(uri, null);
    }
    return ret;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    String where;
    switch (uriMatcher.match(uri)) {
      case ResidenceContract.RESIDENCE_DIR:
        // so we count deleted rows
        where = (selection == null) ? "1" : selection;
        break;
      case ResidenceContract.RESIDENCE_ITEM:
        long id = ContentUris.parseId(uri);
        where = ResidenceContract.Column.ID
            + "="
            + id
            + (TextUtils.isEmpty(selection) ? "" : " and ( "
            + selection + " )");
        break;
      default:
        throw new IllegalArgumentException("Illegal uri: " + uri);
    }
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int ret = db.delete(ResidenceContract.TABLE_RESIDENCES, where, selectionArgs);
    if(ret > 0) {
      // Notify that data for this uri has changed
      getContext().getContentResolver().notifyChange(uri, null);
    }
    Log.d(TAG, "deleted records: " + ret);
    return ret;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {
    String where;
    switch (uriMatcher.match(uri)) {
      case ResidenceContract.RESIDENCE_DIR:
        // so we count updated rows
        where = selection; 
        break;
      case ResidenceContract.RESIDENCE_ITEM:
        long id = ContentUris.parseId(uri);
        where = ResidenceContract.Column.ID
            + "="
            + id
            + (TextUtils.isEmpty(selection) ? "" : " and ( "
            + selection + " )");
        break;
      default:
        throw new IllegalArgumentException("Illegal uri: " + uri);
    }
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int ret = db.update(ResidenceContract.TABLE_RESIDENCES, values,
        where, selectionArgs);
    if(ret > 0) {
      // Notify that data for this URI has changed
      getContext().getContentResolver().notifyChange(uri, null);
    }
    Log.d(TAG, "updated records: " + ret);
    return ret;
  }

}
