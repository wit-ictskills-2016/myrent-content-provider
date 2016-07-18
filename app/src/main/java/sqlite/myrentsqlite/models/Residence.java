package sqlite.myrentsqlite.models;

import java.util.Date;
import java.util.UUID;


public class Residence
{
  public UUID uuid;
  public String geolocation;
  public Date date;
  public boolean rented;
  public String tenant;
  public double zoom;//zoom level of accompanying map
  public String photo;


  public Residence()
  {
    uuid = UUID.randomUUID();
    geolocation = "52.253456,-7.187162";
    date = new Date();
    rented = false;
    tenant = ": none presently";
    zoom = 16.0;
    photo = "photo";
  }

  public Residence(String geolocation, boolean rented, String tenant, double zoom, String photo)
  {
    this.geolocation = geolocation;
    this.rented = rented;
    this.tenant = tenant;
    this.zoom = zoom;
    this.photo = photo;
  }
}