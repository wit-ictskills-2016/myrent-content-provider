package sqlite.myrentsqlite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import sqlite.myrentsqlite.R;
import sqlite.myrentsqlite.services.RefreshResidenceService;;


public class MyRent extends AppCompatActivity implements View.OnClickListener
{

  private Button addResidence;
  private Button selectResidence;
  private Button deleteResidence;
  private Button selectResidences;
  private Button deleteResidences;
  private Button updateResidence;
  private Button getRowId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_myrent);

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

  /**
   * Start a RefreshResidence service, passing the intent message ADD_RESIDENCE
   * This determines what functionality is invoked in the service.
   */
  private void addResidence() {
    Intent intent = new Intent(getBaseContext(), RefreshResidenceService.class);
    intent.putExtra(RefreshResidenceService.REFRESH, RefreshResidenceService.ADD_RESIDENCE);
    startService(intent);
  }

  /**
   * This method demonstrates how to select a Residence record, identified by
   * its primary key, the UUID field.
   * Invoking addResidence() writes a Residence record to the database.
   * Additionally, it initializes this.residence field.
   * The id of this.residence is then used as a parameter in DbHelper.selectResidence.
   */
  public void selectResidence() {
    Intent intent = new Intent(getBaseContext(), RefreshResidenceService.class);
    intent.putExtra(RefreshResidenceService.REFRESH, RefreshResidenceService.SELECT_RESIDENCE);
    startService(intent);
  }


  public void selectResidences() {
    Intent intent = new Intent(getBaseContext(), RefreshResidenceService.class);
    intent.putExtra(RefreshResidenceService.REFRESH, RefreshResidenceService.SELECT_RESIDENCES);
    startService(intent);
  }

  public void deleteResidence() {
    Intent intent = new Intent(getBaseContext(), RefreshResidenceService.class);
    intent.putExtra(RefreshResidenceService.REFRESH, RefreshResidenceService.DELETE_RESIDENCE);
    startService(intent);
  }

  /**
   * Delete all records.
   * Count the number of rows in database following deletion -should be zero.
   * Provide user feed back in a toast.
   */
  public void deleteResidences() {
    Intent intent = new Intent(getBaseContext(), RefreshResidenceService.class);
    intent.putExtra(RefreshResidenceService.REFRESH, RefreshResidenceService.DELETE_RESIDENCES);
    startService(intent);
  }

  /**
   * Update a residence record.
   * Create and insert a test record.
   * Make some changes to its fields and update its copy in the database.
   * Verify and provide toast feedback.
   */
  public void updateResidence() {
    Intent intent = new Intent(getBaseContext(), RefreshResidenceService.class);
    intent.putExtra(RefreshResidenceService.REFRESH, RefreshResidenceService.UPDATE_RESIDENCE);
    startService(intent);
  }

  /**
   * For legacy reasons we use UUID as a primary key
   * SQLite generates a long row id - here we demo.
   */
  public void getRowId() {
//    addResidence();
//    long rowid = app.dbHelper.getRowId(residence.uuid);
//    Toast.makeText(this, "Row id : " + rowid, Toast.LENGTH_SHORT).show();
  }

}