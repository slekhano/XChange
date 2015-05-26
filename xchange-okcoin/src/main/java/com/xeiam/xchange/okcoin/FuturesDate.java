package com.xeiam.xchange.okcoin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FuturesDate
{
  private static class Formatter
  {
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Formatter()
    {
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
  }

  private final Date date;
  private final static Formatter formatter = new Formatter();

  public FuturesDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  public String formatDate() {
    return formatter.formatter.format(date);
  }
}
