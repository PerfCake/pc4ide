package org.perfcake.ide.core.utils;

/**
 * Utility class for manipulating with strings
 *
 * @author jknetl
 */
public class StringUtils {

	private StringUtils() {
	}

	/**
	 * Convert first character in the string to uppercase.
	 *
	 * @param s
	 */
	public static String firstToUpperCase(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		final StringBuilder builder = new StringBuilder()
				.append(Character.toUpperCase(s.charAt(0)));
		if (s.length() > 1) {
			builder.append(s.substring(1));
		}

		return builder.toString();
	}

	/**
	 * Convert first character in string to lowercase.
	 *
	 * @param s
	 */
	public static String firstToLowerCase(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		final StringBuilder builder = new StringBuilder()
				.append(Character.toLowerCase(s.charAt(0)));
		if (s.length() > 1) {
			builder.append(s.substring(1));
		}

		return builder.toString();
	}
}
