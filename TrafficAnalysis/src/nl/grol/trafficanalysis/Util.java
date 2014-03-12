package nl.grol.trafficanalysis;

import java.text.DecimalFormat;

public class Util {
	private static final String decimalFormatStr = "0.0#";

	public static final String formatDouble(double doubleValue) {
		DecimalFormat df = new DecimalFormat(decimalFormatStr);
		return df.format(doubleValue);
	}

}
