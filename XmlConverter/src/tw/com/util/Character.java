package tw.com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import sun.util.logging.resources.logging;

public class Character {

	public static String null2str(Object object) {
		return object == null ? "" : object.toString().trim();
	}

	/**
	 * return true if the String passed in is something like XML
	 *
	 *
	 * @param inString
	 *            a string that might be XML
	 * @return true of the string is XML, false otherwise
	 */
//	public static boolean isXMLLike(String inXMLStr) {
//
//		boolean retBool = false;
//		Pattern pattern;
//		Matcher matcher;
//
//		// REGULAR EXPRESSION TO SEE IF IT AT LEAST STARTS AND ENDS
//		// WITH THE SAME ELEMENT
//		final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";
//
//		// IF WE HAVE A STRING
//		if (inXMLStr != null && inXMLStr.trim().length() > 0) {
//
//			// IF WE EVEN RESEMBLE XML
//			if (inXMLStr.trim().startsWith("<")) {
//
//				pattern = Pattern.compile(XML_PATTERN_STR,
//						Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
//
//				// RETURN TRUE IF IT HAS PASSED BOTH TESTS
//				matcher = pattern.matcher(inXMLStr);
//				retBool = matcher.matches();
//			}
//			// ELSE WE ARE FALSE
//		}
//
//		return retBool;
//	}

	public static boolean isXMLLike(String inXMLStr) {

		boolean retBool = false;
		try {
			XMLConverter.getDocument(inXMLStr);
			retBool = true;
		} catch (Exception e) {
			return retBool;
		}
		return retBool;
	}
	public static boolean isJSONValid(String jsonInString) {
		try {
			new JSONObject(jsonInString);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
