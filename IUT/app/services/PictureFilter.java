package services;

public class PictureFilter {
	
	private static final long maxSize = 16777216;
	private static final long minSize = 4096;

	public boolean isValidPicture(String name, String type, long size) {
		if((maxSize >= size && size >= minSize) && type.contains("image") && !(name.isEmpty())){
			return true;
			
		} else {
			return false;
		}
		
	}
	
}
