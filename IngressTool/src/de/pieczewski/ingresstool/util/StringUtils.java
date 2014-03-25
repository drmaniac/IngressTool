package de.pieczewski.ingresstool.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	public static List<MatchResult> findAll(String searchString, String pattern) {
		List<MatchResult> results = new ArrayList<MatchResult>();
		for (Matcher m = Pattern.compile(pattern).matcher(searchString); m
				.find();) {
			results.add(m.toMatchResult());
		}
		return results;
	}
}
