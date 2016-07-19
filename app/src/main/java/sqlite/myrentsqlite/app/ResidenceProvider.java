package sqlite.myrentsqlite.app;

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
 */
public class ResidenceProvider extends ContentProvider
{
  private static final String TAG = ResidenceProvider.class.getSimpleName();
  private DbHelper dbHelper;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    uriMatcher.addURI(ResidenceContract.AUTHORITY, ResidenceContract.TABLE_RESIDENCES, ResidenceContract.STATUS_DIR);
    uriMatcher.addURI(ResidenceContract.AUTHORITY, ResidenceContract.TABLE_RESIDENCES + "/#", ResidenceContract.STATUS_ITEM);
  }

  @Override
  public boolean onCreate() {

    dbHelper = new DbHelper(getContext());
    Log.d(TAG, "onCreated");
    return true;
  }

  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder(); //
    qb.setTables( ResidenceContract.TABLE_RESIDENCES ); //
    switch (uriMatcher.match(uri)) { //
      case ResidenceContract.STATUS_DIR:
        break;
      case ResidenceContract.STATUS_ITEM:
        qb.appendWhere(ResidenceContract.Column.ID + "=" + uri.getLastPathSegment()); //
        break;
      default:
        throw new IllegalArgumentException("Illegal uri: " + uri);
    }
    String orderBy = (TextUtils.isEmpty(sortOrder))
        ? ResidenceContract.DEFAULT_SORT
        : sortOrder; //
    SQLiteDatabase db = dbHelper.getReadableDatabase(); //
    Cursor cursor = qb.query(db, projection, selection, selectionArgs,
        null, null, orderBy); //
// register for uri changes
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
//
    Log.d(TAG, "queried records: "+cursor.getCount());
    return cursor; //
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case ResidenceContract.STATUS_DIR:
        Log.d(TAG, "gotType: " + ResidenceContract.STATUS_TYPE_DIR);
        return ResidenceContract.STATUS_TYPE_DIR;
      case ResidenceContract.STATUS_ITEM:
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
    if (uriMatcher.match(uri) != ResidenceContract.STATUS_DIR) {
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
      case ResidenceContract.STATUS_DIR:
// so we count deleted rows
        where = (selection == null) ? "1" : selection;
        break;
      case ResidenceContract.STATUS_ITEM:
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
      case ResidenceContract.STATUS_DIR:
// so we count updated rows
        where = selection; 
        break;
      case ResidenceContract.STATUS_ITEM:
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
