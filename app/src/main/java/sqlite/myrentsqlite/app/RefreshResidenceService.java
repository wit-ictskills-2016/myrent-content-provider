package sqlite.myrentsqlite.app;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import sqlite.myrentsqlite.models.Residence;

/**
 * Created by jfitzgerald on 18/07/2016.
 */
public class RefreshResidenceService extends IntentService
{
  public static final String TAG = "RefreshResidenceService";
  public static final String REFRESH = "refresh residence";
  public static final String ADD_RESIDENCE = "1";
  public static final String SELECT_RESIDENCE = "2";

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


  public void addResidence() {
    ContentValues values = new ContentValues();
    Residence residence = new Residence();

    values.put(ResidenceContract.Column.GEOLOCATION, residence.geolocation);
    values.put(ResidenceContract.Column.DATE, String.valueOf(residence.date.getTime()));
    values.put(ResidenceContract.Column.RENTED, residence.rented == true ? "yes" : "no");
    values.put(ResidenceContract.Column.TENANT, residence.tenant);
    values.put(ResidenceContract.Column.ZOOM, Double.toString(residence.zoom));
    values.put(ResidenceContract.Column.PHOTO, residence.photo);

    Uri uri = getContentResolver().insert(
        ResidenceContract.CONTENT_URI, values);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId)
  {
    String value = intent.getStringExtra(REFRESH);
    switch (value) {
      case ADD_RESIDENCE:
        addResidence();
        break;
      case SELECT_RESIDENCE:
        break;
    }
    return START_STICKY;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //switch(getArguments().getSerializable(EXTRA_REFRESH_RESIDENCE);

    Log.d(TAG, "onHandleIntent invoked");
  }
}

