package model;

public class Fragment {
	private String value;
	
	public Fragment(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getSize() {
		return value.length();
	}

}
