package model;

public class SimpleFile {
	public static final int type_dir = 0;
	public static final int type_noamal_file = 1;
	public static final int type_video = 2;
	
	public static final long kb = 1024;
	public static final long mb = kb * 1024;
	public static final long gb = mb * 1024;
	String path;
	String name;
	String size;
	int type;
	
	public int getType(){
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(long size) {
		if (size > gb) {
			this.size = size / gb + "gb";
			return;
		} else if (size > mb) {
			this.size = size / mb + "mb";
			return;
		} else if(size > kb) {
			this.size = size / kb + "kb";
		}else{
			this.size = size + "b";
		}
	}

}
