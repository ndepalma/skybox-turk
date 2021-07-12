package utils;

/**
 * Pared down version of my utils I keep around. Unfortunately I only open sourced my safeSleep method
 *
 * @author ndepalma@media.mit.edu
 */
public class BasicUtils {

    /**
     * Safe sleep is a time.sleep that encapsulates the sleep method that can throw an error.
     * This is just for convienience.
     *
     * @param millis The number of milliseconds to sleep for.
     */
	public static void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(Exception e) {}
	}
}
