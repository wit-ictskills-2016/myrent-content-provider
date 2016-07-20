package sqlite.myrentsqlite.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import sqlite.myrentsqlite.cloud.ResidenceCloud;
import sqlite.myrentsqlite.models.Residence;
import sqlite.myrentsqlite.providers.ResidenceContract;

/**
 * Created by jfitzgerald on 18/07/2016.
 */
public class RefreshResidenceService extends IntentService
{
  public static final String TAG = "RefreshResidenceService";
  public static final String REFRESH = "refresh residence";
  public static final String ADD_RESIDENCE = "1";
  public static final String SELECT_RESIDENCE = "2";
  public static final String DELETE_RESIDENCE = "3";
  public static final String SELECT_RESIDENCES = "4";
  public static final String DELETE_RESIDENCES = "5";
  public static final String UPDATE_RESIDENCE = "6";

  ResidenceCloud cloud = new ResidenceCloud();

  public RefreshResidenceService() {
    super("RefreshResidenceService");
  }

  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public RefreshResidenceService(String name) {
    super(name);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    String value = intent.getStringExtra(REFRESH);
    switch (value) {

      case ADD_RESIDENCE:
        addResidence(new Residence());
        break;

      case SELECT_RESIDENCE:
        selectResidence();
        break;

      case SELECT_RESIDENCES:
        selectResidences();
        break;

      case DELETE_RESIDENCE:
        deleteResidence();
        break;

      case DELETE_RESIDENCES:
        deleteResidences();
        break;

      case UPDATE_RESIDENCE:
        updateResidence();
        break;
    }

    return START_STICKY;
  }

  private void addResidence(Residence residence) {
    ContentValues values = new ContentValues();

    values.put(ResidenceContract.Column.UUID, residence.uuid.toString());
    values.put(ResidenceContract.Column.GEOLOCATION, residence.geolocation);
    values.put(ResidenceContract.Column.DATE, String.valueOf(residence.date.getTime()));
    values.put(ResidenceContract.Column.RENTED, residence.rented == true ? "yes" : "no");
    values.put(ResidenceContract.Column.TENANT, residence.tenant);
    values.put(ResidenceContract.Column.ZOOM, Double.toString(residence.zoom));
    values.put(ResidenceContract.Column.PHOTO, residence.photo);

    Uri uri = getContentResolver().insert(
        ResidenceContract.CONTENT_URI, values);
  }

  /**
   * Select a single residence.
   * To ensure the database is populated
   * we first create a default Residence object and add it to the database.
   */
  private void selectResidence() {
    Residence residence = ResidenceCloud.residence();
    addResidence(residence);
    selectResidence(residence.uuid);
  }

  /**
   * Test query method in ResidenceProvider by
   * obtaining a single residences from simulated cloud,
   * adding this residence as record to database,
   * querying database for this record and
   * checking result
   * Refer to ResidenceProvider.query for documentation query params
   */
  private Residence selectResidence(UUID uuid) {
    Residence residence = new Residence();
    String selection = ResidenceContract.Column.UUID + " = ?";
    String[] selectionArgs = new String[]{uuid + ""};

    // Query database
    Cursor cursor = getContentResolver().query(ResidenceContract.CONTENT_URI, null, selection, selectionArgs, null);

    if (cursor.getCount() > 0) {
      int columnIndex = 1; // Skip the 0th column - the _id
      cursor.moveToFirst();

      residence.uuid = UUID.fromString(cursor.getString(columnIndex++));
      residence.geolocation = cursor.getString(columnIndex++);
      residence.date = new Date(Long.parseLong(cursor.getString(columnIndex++)));
      residence.rented = cursor.getString(columnIndex++) == "yes" ? true : false;
      residence.tenant = cursor.getString(columnIndex++);
      residence.zoom = Double.parseDouble(cursor.getString(columnIndex++));
      residence.photo = cursor.getString(columnIndex++);

    }
    cursor.close();

    return residence;
  }

  /**
   * Test query method in ResidenceProvider by
   * obtaining a list of residences from simulated cloud,
   * adding each residence as record to database,
   * querying database for this list and
   * checking result
   */
  private void selectResidences() {
    populateSampleData();

    // Query the database
    List<Residence> residences = new ArrayList<Residence>();
    Cursor cursor = getContentResolver().query(ResidenceContract.CONTENT_URI, null, null, null, null);

    if (cursor.moveToFirst()) {
      int columnIndex = 1; // skip column 0, the _id
      do {
        Residence residence = new Residence();

        residence.uuid = UUID.fromString(cursor.getString(columnIndex++));
        residence.geolocation = cursor.getString(columnIndex++);
        residence.date = new Date(Long.parseLong(cursor.getString(columnIndex++)));
        residence.rented = cursor.getString(columnIndex++) == "yes" ? true : false;
        residence.tenant = cursor.getString(columnIndex++);
        residence.zoom = Double.parseDouble(cursor.getString(columnIndex++));
        residence.photo = cursor.getString(columnIndex++);

        columnIndex = 1;

        residences.add(residence);
      } while (cursor.moveToNext());
    }
    cursor.close();

  }


  /**
   * Add a list of residences to database
   * Pick on at random from the list and delete it from db
   */
  private void deleteResidence() {
    List<Residence> residenceList = populateSampleData();

    String uuid = residenceList.get(0).uuid.toString(); // Pick the first row (arbitrarily)
    String selection = ResidenceContract.Column.UUID + " = ?";
    String[] selectionArgs = new String[]{uuid + ""};
    int response = getContentResolver().delete(ResidenceContract.CONTENT_URI, selection, selectionArgs);
    Log.d(TAG, "delete record response: " + response);
  }


  /**
   * Delete all Residence records.
   */
  private void deleteResidences() {
    List<Residence> residenceList = populateSampleData();

    int response = getContentResolver().delete(ResidenceContract.CONTENT_URI, null, null);
    Log.d(TAG, "delete all records response: " + response);
  }


  /**
   * Test the update method
   */

  private void updateResidence() {
    // Populate database with sample residence
    Residence residence = ResidenceCloud.residence();
    addResidence(residence);

    // Modify the residence and update database copy
    residence.zoom = 40;
    residence.tenant = "A. N. Other";

    updateResidence(residence);

    Residence residenceUpdated = selectResidence(residence.uuid);

    boolean testResult = residence.zoom == residenceUpdated.zoom &&
        residence.tenant.equals(residenceUpdated.tenant);

    Log.d(TAG, "update residence attempt: " + testResult);
  }

  /**
   * Update a residence record
   */
  private void updateResidence(Residence residence) {
    String uuid = residence.uuid.toString();
    String selection = ResidenceContract.Column.UUID + " = ?";
    String[] selectionArgs = new String[]{uuid + ""};

      ContentValues values = new ContentValues();

      values.put(ResidenceContract.Column.GEOLOCATION, residence.geolocation);
      values.put(ResidenceContract.Column.DATE, String.valueOf(residence.date.getTime()));
      values.put(ResidenceContract.Column.RENTED, residence.rented == true ? "yes" : "no");
      values.put(ResidenceContract.Column.TENANT, residence.tenant);
      values.put(ResidenceContract.Column.ZOOM, Double.toString(residence.zoom));
      values.put(ResidenceContract.Column.PHOTO, residence.photo);

      getContentResolver().update(ResidenceContract.CONTENT_URI, values, selection, selectionArgs);

    }

  /**
   * Populate database with list sample residences
   * Used for testing
   */
  private List<Residence> populateSampleData() {
    List<Residence> residenceList = ResidenceCloud.residences();
    for (Residence residence : residenceList) {
      addResidence(residence);
    }
    return residenceList;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(TAG, "onHandleIntent invoked");
  }
}

