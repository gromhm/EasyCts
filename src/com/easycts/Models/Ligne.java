package com.easycts.Models;

public class Ligne 
{
	// Notez que l'identifiant est un long
	  private long id;
	  private String title;
	  private String direction1;
	  private String direction2;

	  public Ligne(long id, String title, String direction1, String direction2) {
	    super();
	    this.id = id;
	    this.title = title;
	    this.direction1 = direction1;
	    this.direction2 = direction2;
	  }
	 
	  public long getId() {
	    return id;
	  }
	 
	  public void setId(long id) {
	    this.id = id;
	  }
	 
	  public String getTitle() {
	    return title;
	  }
	 
	  public void setTitle(String title) {
	    this.title = title;
	  }
	 
	  public String getDirection1() {
		    return direction1;
	  }
	 
	  public void setDirection1(String direction) {
	    this.direction1 = direction;
	  }
	  
	  public String getDirection2() {
		    return direction2;
	  }
	 
	  public void setDirection2(String direction) {
	    this.direction2 = direction;
	  }
}
