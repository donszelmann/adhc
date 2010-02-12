package ch.cern.atlas.adhc.client;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Image;

public class Entry {
	public String getTaskName() {
		return "Task "+Random.nextInt(999);
	}
	
	public Image getPicture() {
		Image image = new Image("https://aismedia.cern.ch/aismedia/cern.aismedia.DownloadServlet?file_id="+Random.nextInt(23020));
		image.setHeight("60px");
		return image;
	}
	
	public String getName() {
		return "John"+Random.nextInt(999)+" Doe"+Random.nextInt(999);
	}
	
	public String getPeriod() {
		return Random.nextBoolean() ? "7h-15h" : "0h-17h";
	}
}