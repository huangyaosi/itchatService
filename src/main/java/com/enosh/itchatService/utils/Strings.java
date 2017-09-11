package com.enosh.itchatService.utils;

public class Strings {
	public static final String COLON_DELIMITER = ":";
	public static final String SEMICOLON_DELIMITER = ":"; 
	public static final String COMMA_DELIMITER = ":"; 
	
	public static boolean isEmpty(String str) {
		return str !=null ? str.trim().length() == 0 : true;
	}
	
	public static double similarity(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater
											// length
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0; /* both strings are zero length */
		}
		/*
		 * // If you have StringUtils, you can use it to calculate the edit
		 * distance: return (longerLength -
		 * StringUtils.getLevenshteinDistance(longer, shorter)) / (double)
		 * longerLength;
		 */
		return (longerLength - editDistance(longer, shorter))
				/ (double) longerLength;

	}

	public static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}
	
	public static void append(StringBuilder sb, String str, String delimiter){
		if(!isEmpty(sb.toString())) {
			sb.append(delimiter).append(str);
		}
	}
	
	public static void main(String args[]) {
		String s1 = "dhahahahafffff";
		String s2 = "dhahahahafffff";
		System.out.println(String.format("%.3f is the similarity between \"%s\" and \"%s\"", similarity(s1, s2), s1, s2));
	}
}
