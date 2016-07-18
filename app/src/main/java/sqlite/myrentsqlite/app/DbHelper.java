package sqlite.myrentsqlite.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import sqlite.myrentsqlite.models.Residence;

public class DbHelper extends SQLiteOpenHelper
{
  static final String TAG = "DbHelper";
  static final String DATABASE_NAME = "residences.db";
  static final int DATABASE_VERSION = 1;
//  static final String TABLE_RESIDENCES = "tableResidences";

//  static final String PRIMARY_KEY = "uuid";
//  static final String GEOLOCATION = "geolocation";
//  static final String DATE = "date";
//  static final String RENTED = "rented";
//  static final String TENANT = "tenant";
//  static final String ZOOM = "zoom";
//  static final String PHOTO = "photo";

  Context context;

  public DbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql = String.format("create table %s (%s text primary key, %s text, %s text, %s text, %s text, %s text, %s text)",
        ResidenceContract.TABLE_RESIDENCES,
        ResidenceContract.Column.PRIMARY_KEY,
        ResidenceContract.Column.GEOLOCATION,
        ResidenceContract.Column.DATE,
        ResidenceContract.Column.RENTED,
        ResidenceContract.Column.TENANT,
        ResidenceContract.Column.ZOOM,
        ResidenceContract.Column.PRIMARY_KEY);
    db.execSQL(sql);
    Log.d(TAG, "DbHelper.onCreated: " + sql);
  }

  /**
   * @param residence Reference to Residence object to be added to database
   */
  public void addResidence(Residence residence) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ResidenceContract.Column.PRIMARY_KEY, residence.uuid.toString());
    values.put(ResidenceContract.Column.GEOLOCATION, residence.geolocation);
    values.put(ResidenceContract.Column.DATE, String.valueOf(residence.date.getTime()));
    values.put(ResidenceContract.Column.RENTED, residence.rented == true ? "yes" : "no");
    values.put(ResidenceContract.Column.TENANT, residence.tenant);
    values.put(ResidenceContract.Column.ZOOM, Double.toString(residence.zoom));
    values.put(ResidenceContract.Column.PHOTO, residence.photo);

    // Insert record
    db.insert(ResidenceContract.TABLE_RESIDENCES, null, values);
    db.close();
  }

  public Residence selectResidence(UUID resId) {
    Residence residence;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;

    try {
      residence = new Residence();
      String query = "SELECT * FROM " + ResidenceContract.TABLE_RESIDENCES + " WHERE " + ResidenceContract.Column.PRIMARY_KEY + " = ?";
      cursor = db.rawQuery(query, new String[]{resId.toString() + ""});

      if (cursor.getCount() > 0) {
        int columnIndex = 0;
        cursor.moveToFirst();
        residence.uuid = UUID.fromString(cursor.getString(columnIndex++));
        residence.geolocation = cursor.getString(columnIndex++);
        residence.date = new Date(Long.parseLong(cursor.getString(columnIndex++)));
        residence.rented = cursor.getString(columnIndex++) == "yes" ? true : false;
        residence.tenant = cursor.getString(columnIndex++);
        residence.zoom = Double.parseDouble(cursor.getString(columnIndex++));
        residence.photo = cursor.getString(columnIndex++);
      }
    }
    finally {
      cursor.close();
    }
    return residence;
  }

  public void deleteResidence(Residence residence) {
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.delete(ResidenceContract.TABLE_RESIDENCES, ResidenceContract.Column.PRIMARY_KEY + "=?", new String[]{residence.uuid.toString() + ""});
    }
    catch (Exception e) {
      Log.d(TAG, "delete residence failure: " + e.getMessage());
    }
  }

  /**
   * Query database and select entire tableResidences.
   *
   * @return A list of Residence object records
   */
  public List<Residence> selectResidences() {
    List<Residence> residences = new ArrayList<Residence>();
    String query = "SELECT * FROM " + ResidenceContract.TABLE_RESIDENCES;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);
    if (cursor.moveToFirst()) {
      int columnIndex = 0;
      do {
        Residence residence = new Residence();
        residence.uuid = UUID.fromString(cursor.getString(columnIndex++));
        residence.geolocation = cursor.getString(columnIndex++);
        residence.date = new Date(Long.parseLong(cursor.getString(columnIndex++)));
        residence.rented = cursor.getString(columnIndex++) == "yes" ? true : false;
        residence.tenant = cursor.getString(columnIndex++);
        residence.zoom = Double.parseDouble(cursor.getString(columnIndex++));
        residence.photo = cursor.getString(columnIndex++);
        columnIndex = 0;

        residences.add(residence);
      } while (cursor.moveToNext());
    }
    cursor.close();
    return residences;
  }

  /**
   * Delete all records
   */
  public void deleteResidences() {
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.execSQL("delete from " + ResidenceContract.TABLE_RESIDENCES);
    }
    catch (Exception e) {
      Log.d(TAG, "delete residences failure: " + e.getMessage());
    }
  }


  /**
   * Queries the database for the number of records.
   *
   * @return The number of records in the dataabase.
   */
  public long getCount() {
    SQLiteDatabase db = this.getReadableDatabase();
    long numberRecords = DatabaseUtils.queryNumEntries(db, ResidenceContract.TABLE_RESIDENCES);
    db.close();
    return numberRecords;
  }

  /**
   * Update an existing Residence record.
   * All fields except record uuid updated.
   *
   * @param residence The Residence record being updated.
   */
  public void updateResidence(Residence residence) {
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      ContentValues values = new ContentValues();
      values.put(ResidenceContract.Column.GEOLOCATION, residence.geolocation);
      values.put(ResidenceContract.Column.DATE, String.valueOf(residence.date.getTime()));
      values.put(ResidenceContract.Column.RENTED, residence.rented == true ? "yes" : "no");
      values.put(ResidenceContract.Column.TENANT, residence.tenant);
      values.put(ResidenceContract.Column.ZOOM, Double.toString(residence.zoom));
      values.put(ResidenceContract.Column.PHOTO, residence.photo);
      db.update(ResidenceContract.TABLE_RESIDENCES, values, ResidenceContract.Column.PRIMARY_KEY + "=?", new String[]{residence.uuid.toString() + ""});
    }
    catch (Exception e) {
      Log.d(TAG, "update residences failure: " + e.getMessage());
    }
  }

  /**
   * SQLite generates a long rowid. We require access to this because the UUID uuid is not
   * compatible with the CursorAdapter class that we use for the ContentProvider feature.
   *
   * @param resId The UUID field in the Residence model object, present in the database.
   * @return The long rowid corresponding to the record whose UUID field is resID, the actual parameter.
   */
  public long getRowId(UUID resId) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;
    long rowid = -1;
    try {
      cursor = db.rawQuery("SELECT rowid FROM " + ResidenceContract.TABLE_RESIDENCES + " WHERE " + ResidenceContract.Column.PRIMARY_KEY + " = ?", new String[]{resId.toString() + ""});
      if (cursor.moveToFirst()) {
        rowid = cursor.getLong(0);
        Log.d(TAG, "rowid: " + rowid);
        return rowid;
      }
    }
    finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return rowid;
  }

  /**
   * Invoked when schema changed.
   * This determined by comparison existing version and old version.
   * @param db The SQLite database
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