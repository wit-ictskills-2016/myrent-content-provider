package sqlite.myrentsqlite.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import sqlite.myrentsqlite.R;
import sqlite.myrentsqlite.app.MyRentApp;
import sqlite.myrentsqlite.models.Residence;

public class MyRent extends AppCompatActivity implements View.OnClickListener
{

  private Button addResidence;
  private Button selectResidence;
  private Button deleteResidence;
  private Button selectResidences;
  private Button deleteResidences;
  private Button updateResidence;
  private Button getRowId;

  MyRentApp app;
  Residence residence;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_myrent);

    app = MyRentApp.getApp();

    addResidence = (Button) findViewById(R.id.addResidence);
    addResidence.setOnClickListener(this);

    selectResidence = (Button) findViewById(R.id.selectResidence);
    selectResidence.setOnClickListener(this);

    deleteResidence = (Button) findViewById(R.id.deleteResidence);
    deleteResidence.setOnClickListener(this);

    selectResidences = (Button) findViewById(R.id.selectResidences);
    selectResidences.setOnClickListener(this);

    deleteResidences = (Button) findViewById(R.id.deleteResidences);
    deleteResidences.setOnClickListener(this);

    updateResidence = (Button) findViewById(R.id.updateResidence);
    updateResidence.setOnClickListener(this);

    getRowId = (Button) findViewById(R.id.getRowId);
    getRowId.setOnClickListener(this);


  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.addResidence:
        addResidence();
        break;

      case R.id.selectResidence:
        selectResidence();
        break;

      case R.id.deleteResidence:
        deleteResidence();
        break;

      case R.id.selectResidences:
        selectResidences();
        break;

      case R.id.deleteResidences:
        deleteResidences();
        break;

      case R.id.updateResidence:
        updateResidence();
        break;

      case R.id.getRowId:
        getRowId();
        break;
    }
  }

  private void addResidence() {
    residence = new Residence();

    app.dbHelper.addResidence(residence);

  }

  /**
   * This method demonstrates how to select a Residence record, identified by
   * its primary key, the UUID field.
   * Invoking addResidence() writes a Residence record to the database.
   * Additionally, it initializes this.residence field.
   * The id of this.residence is then used as a parameter in DbHelper.selectResidence.
   */
  public void selectResidence() {
    addResidence();
    UUID uuid = residence.uuid;
    Residence selectedResidence = app.dbHelper.selectResidence(uuid);
    if (residence != null && residence.uuid.toString().equals(selectedResidence.uuid.toString())) {
      Toast.makeText(this, "Residence record selected(id: " + residence.uuid, Toast.LENGTH_SHORT).show();
    }
    else {
      Toast.makeText(this, "Failed to select Residence record", Toast.LENGTH_SHORT).show();
    }
  }

  public void deleteResidence() {
    if (residence == null) {
      addResidence();
    }
    Residence res = app.dbHelper.selectResidence(residence.uuid);
    app.dbHelper.deleteResidence(res);
    Toast.makeText(this, "Deleted Residence (id: " + res.uuid + ")", Toast.LENGTH_SHORT).show();
  }

  public void selectResidences() {
    List<Residence> residences = app.dbHelper.selectResidences();
    Toast.makeText(this, "Retrieved residence list containing  " + residences.size() + " records", Toast.LENGTH_SHORT).show();
  }

  /**
   * Delete all records.
   * Count the number of rows in database following deletion -should be zero.
   * Provide user feed back in a toast.
   */
  public void deleteResidences() {
    app.dbHelper.deleteResidences();
    Toast.makeText(this, "Number of records in database " + app.dbHelper.getCount(), Toast.LENGTH_SHORT).show();

  }

  /**
   * Update a residence record.
   * Create and insert a test record.
   * Make some changes to its fields and update its copy in the database.
   * Verify and provide toast feedback.
   */
  public void updateResidence() {
    addResidence(); // This initializes the instance variable Residence residence
    Residence res = app.dbHelper.selectResidence(residence.uuid);
    // Makes some distinguishing changes to res fields
    res.tenant = "Barney Gumble";
    res.rented = true;
    res.zoom = 20;

    app.dbHelper.updateResidence(res);

    // Read the updated rrow and verify it's correct.
    Residence res2 = app.dbHelper.selectResidence(res.uuid);
    boolean b = res.zoom == res2.zoom;
    if (b == true) {
      Toast.makeText(this, "Update succeeded", Toast.LENGTH_SHORT).show();
    }
    else {
      Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * For legacy reasons we use UUID as a primary key
   * SQLite generates a long row id - here we demo.
   */
  public void getRowId()
  {
    addResidence();
    long rowid = app.dbHelper.getRowId(residence.uuid);
    Toast.makeText(this, "Row id : " + rowid, Toast.LENGTH_SHORT).show();
  }
}