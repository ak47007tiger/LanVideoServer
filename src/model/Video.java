package model;

public class Video extends SimpleFile{
	
	String thum;
	String[] thumbs;

	public String getThum() {
		return thum;
	}

	public void setThum(String thum) {
		this.thum = thum;
	}

	public String[] getThumbs() {
		return thumbs;
	}

	public void setThumbs(String[] thumbs) {
		this.thumbs = thumbs;
	}

	@Override
	public int getType() {
		return type_video;
	}

}
