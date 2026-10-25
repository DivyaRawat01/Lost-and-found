package com.lostfound;

import java.sql.Date;

/** Simple bean that carries one row of the items table to the JSP pages. */
public class Item {

    private final int id;
    private final int userId;
    private final String type;
    private final String itemName;
    private final String description;
    private final String location;
    private final Date date;
    private final String status;

    public Item(int id, int userId, String type, String itemName,
                String description, String location, Date date, String status) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.itemName = itemName;
        this.description = description;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    public int getId()             { return id; }
    public int getUserId()         { return userId; }
    public String getType()        { return type; }
    public String getItemName()    { return itemName; }
    public String getDescription() { return description; }
    public String getLocation()    { return location; }
    public Date getDate()          { return date; }
    public String getStatus()      { return status; }

    /** Escapes text before printing it in HTML (prevents script injection). */
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
