package sqlite.myrentsqlite.app;

import android.app.Application;
import android.util.Log;

public class MyRentApp extends Application
{
  static final String TAG = "MyRentApp";

  //public DbHelper dbHelper = null;

  private static MyRentApp app;
  @Override
  public void onCreate()
  {
    super.onCreate();
    //dbHelper = new DbHelper(getApplicationContext());
    Log.d(TAG, "MyRent app launched");
    app = this;
  }

  public static MyRentApp getApp(){
    return app;
  }
}