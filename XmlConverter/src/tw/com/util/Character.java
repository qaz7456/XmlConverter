package tw.com.util;

public class Character {

	public static String null2str(Object object) {
		return object == null ? "" : object.toString().trim();
	}
}
