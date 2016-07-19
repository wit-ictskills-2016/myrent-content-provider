package sqlite.myrentsqlite.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sqlite.myrentsqlite.models.Residence;

/**
 * Created by jfitzgerald on 18/07/2016.
 * Simulated cloud
 */
public class ResidenceCloud
{
  public static Residence residence() {
    return new Residence("52.4444,-7.187162", true, "Barney Gumble", 12.0, "photo1.jpeg");
  }

  public static List<Residence> residences() {
    ArrayList<Residence> list = new ArrayList<>();

    list.add(new Residence("52.4444,-7.187162", true, "Barney Gumble", 12.0, "photo1.jpeg"));
    list.add(new Residence("52.3333,-7.187162", true, "Ned Flanders", 16.0, "photo2.jpeg"));

    return list;
  }

}
