package com.mike.cathaybk.interview.Util;

import java.math.BigDecimal;

public class Utils {
	
	public static BigDecimal chgBigDecimal(Object obj) {
		BigDecimal result = null;
		
		if(obj instanceof BigDecimal)
			result = (BigDecimal) obj;
		else if(obj instanceof String)
			result = new BigDecimal((String)obj); 
		else
			throw new ClassCastException(String.format("%s cant not be cast BigDecimal", obj));
		
		return result;
	}
	
}
