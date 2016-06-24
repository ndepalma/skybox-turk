package utils;

public class BasicUtils {

	public static void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(Exception e) {}
	}
}
