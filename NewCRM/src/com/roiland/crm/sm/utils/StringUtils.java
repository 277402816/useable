package com.roiland.crm.sm.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.roiland.crm.sm.GlobalConstant;


/**
 * @author Chunji Li
 *
 */
public class StringUtils {
    private static final String tag = Log.getTag(StringUtils.class);
    private static final char[] HEX =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private final static String UNITS[] = {" bytes", " KB", " MB", " GB", " TB"};
    private final static int MODEL = 1024;

    private static final String[] INVAILD = {"/", "\"", "\\", ":", "?", "*", "|", "[", "]"};
    
    private StringUtils() {
    }
    
    public static boolean isEmpty(String value){
    	boolean result = false;
    	if (value == null || value.trim().length() == 0 || value.equalsIgnoreCase("null")) {
    		result = true;
    	}
    	return result;
    }
    
    public static String trimNull(String value) {
    	if (!isEmpty(value))
    		return value;
    	return null;
    }

    public static boolean isIp(String value) {
        if (TextUtils.isEmpty(value)) {
            Log.d(tag, value + " is empty!");
            return false;
        }

        String[] fields = value.split("\\.");
        if (fields.length != 4) {
            return false;
        }

        boolean result = true;
        for (int i = 0; i < fields.length; i++) {
            try {
                int number = Integer.parseInt(fields[i]);
                result &= (number >= 0 && number <= 255);
            } catch (NumberFormatException e) {
                result = false;
            }

            if (!result) {
                break;
            }
        }
        return result;
    }

    public static String toHex(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            int v = b[i];
            builder.append(HEX[(0xF0 & v) >> 4]);
            builder.append(HEX[0x0F & v]);
        }
        return builder.toString();
    }

    public static byte[] hexToBytes(String str) {
        int len = str.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < b.length; i++) {
            int start = i * 2;
            String v = str.substring(start, start + 2);
            try {
                b[i] = (byte) Integer.parseInt(v, 16);
            } catch (NumberFormatException e) {
                Log.d(tag, e.getMessage(), e);
                return null;
            }
        }
        return b;
    }

    public static boolean isEquals(String str1, String str2) {
        boolean result = false;
        if (str1 != null) {
            result = str1.equals(str2);
        } else if (str2 == null) {
            result = true;
        }
        return result;
    }

    public static String toSqliteStr(String str) {
        String result = null;
        if (str != null) {
            result = str.replaceAll("'", "''");
            result = result.replaceAll("%", "/%");
            result = result.replaceAll("_", "/_");
        }
        return result;
    }

    public static String notNull(String str) {
    	if (isEmpty(str)) {
    		return "";
    	}
    	return str;
    }

    public static String notKMNull(String str) {
    	if (isEmpty(str) || "null".equals(str)) {
    		return "";
    	}
    	return str;
    }
    
    public static String convertNull(String str) {
    	if (isEmpty(str)) {
    		return null;
    	}
    	return str;
    }
    
    public static String md5(String string) {
        try {
            MessageDigest digester = MessageDigest.getInstance(GlobalConstant.ALGORITHM);
            byte[] buffer = string.getBytes(GlobalConstant.DEFAULT_CHARSET);
            digester.update(buffer);

            buffer = digester.digest();
            string = toHex(buffer);
        } catch (NoSuchAlgorithmException e) {
            Log.e(tag, e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            Log.e(tag, e.getMessage(), e);
        }
        return string;
    }

    public static String toFileSize(long size) {
        int lastIndex = 0;
        long value = size;
        long last = 0L;

        for (int i = 0; i < UNITS.length;i++) {
            long newValue = value / MODEL;
            if (newValue <= 0) {
                break;
            }

            last = value % MODEL;
            value = newValue;
            lastIndex = i + 1;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(value);
        if (last > 0L) {
            float f = (last + 0.0f) / (MODEL + 0.0f);
            int i = Math.round(f * 100);
            if (i > 0) {
            	if (f < 0.1f) {
            		builder.append(".0").append(i);
            	} else {
            		builder.append(".").append(i);
            	}
            }
        }
        builder.append(UNITS[lastIndex]);
        return builder.toString();
    }

    public static String booleanToString(Boolean flag) {
    	if (flag == null) return "false";
    	
		return flag.toString();
    }
    
    public static Boolean stringToBoolean(String flag) {
    	if (flag == null) return Boolean.FALSE;
    	return Boolean.valueOf(flag);
    }
    
    public static String getDateString(long time) {
    	return (String) DateFormat.format("MM/dd/yyyy h:mmaa",time*1000);
    }
    
    public static long getDateLongForSkyDriver(String time){
    	
    	long date = -1l;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	
    	try{
    		date = sdf.parse(time).getTime() / 1000L;
    	}catch(Exception e){
    		
    		Log.e(tag, "formate time: "+time+" error", e);
    	}
    	
    	return date;
    	
    }
    
    public static long getDateLong(String time) {
    	long date = -1;
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
    	try {
			date = simpleDateFormat.parse(time).getTime() / 1000L;
		} catch (ParseException e) {
			Log.e(tag, "getDateLong", e);
		}
    	return date;
    }

    public static int nullSafeStringComparator(final String one, final String two) {
        if (one == null ^ two == null) {
            return (one == null) ? -1 : 1;
        }

        if (one == null && two == null) {
            return 0;
        }
        return one.compareToIgnoreCase(two);
    }

    public static byte[] toBytes(String str) {
        int len = str.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < b.length; i++) {
            int start = i * 2;
            String v = str.substring(start, start + 2);
            try {
                b[i] = (byte) Integer.parseInt(v, 16);
            } catch (NumberFormatException e) {
                Log.d(tag, e.getMessage(), e);
                return null;
            }
        }
        return b;
    }

    public static String[] split(String string, String delimiters) {
        StringTokenizer st = new StringTokenizer(string, delimiters);
        String[] fields = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            String token = st.nextToken();
            fields[i] = token;
        }

        return fields;
    }

    public static int versionCompare(String version1, String version2) {
        if (version1 == null && version2 == null) {
            return 0;
        } else if (version1 == null) {
            return -1;
        } else if (version2 == null) {
            return 1;
        } else {
            String[] fields1 = split(version1, ".");
            String[] fields2 = split(version2, ".");
            int loop = Math.min(fields1.length, fields2.length);

            int result = 0;
            for (int i = 0; i < loop; i++) {
                result = fields1[i].compareTo(fields2[i]);
                if (result != 0) {
                    return result;
                }
            }

            return fields1.length - fields2.length;
        }
    }

    public static String formatGB(String format, long usedSize, long availableSize){
    	return String.format(format, usedSize/(1024.0* 1024.0 * 1024.0), availableSize/(1024.0* 1024.0 * 1024.0));
    }
    
	public static String getMD5HashCode(String str) {
		try {
			return getMD5HashCode(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.e(tag, e.getMessage(), e);
			return null;
		}
	}

	public static String getMD5HashCode(byte[] b) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(b);
		} catch (NoSuchAlgorithmException e) {
			Log.e(tag, "NoSuchAlgorithmException caught!", e);
			return null;
		}

		byte[] byteArray = messageDigest.digest();
		return StringUtils.toHex(byteArray);
	}
	
	public static boolean containsInvalidCharacters(String name) {
		for (int i = 0; i < INVAILD.length; i++) {
			if (name.contains(INVAILD[i]))
				return true;
		}
		
		return false;
	}
	
	public static String replaceInvalidCharacter(String name) {
		String replaced = name;
		do {
			for (int i = 0; i < INVAILD.length; i++) {
				if (replaced.contains(INVAILD[i])) {
					replaced = replaced.replace(INVAILD[i], "x");
				}
			}
		} while (containsInvalidCharacters(replaced));
		return replaced;
	}
	
	public static String listToString(List<String> list, String split) {
		StringBuffer sb = new StringBuffer();
		if(list!= null && list.size() > 0){
			for(int i = 0; i < list.size(); i++) {
				if(i!=0) sb.append(split);
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}
	
	public  static int compareVersions(String v1, String v2) {
        v1 = v1.replaceAll("\\s", "");
        v2 = v2.replaceAll("\\s", "");
        String[] a1 = v1.split("\\.");
        String[] a2 = v2.split("\\.");
        List<String> l1 = Arrays.asList(a1);
        List<String> l2 = Arrays.asList(a2);


        int i=0;
        while(true){
            Double d1 = null;
            Double d2 = null;

            try{
                d1 = Double.parseDouble(l1.get(i));
            }catch(IndexOutOfBoundsException e){
            }

            try{
                d2 = Double.parseDouble(l2.get(i));
            }catch(IndexOutOfBoundsException e){
            }

            if (d1 != null && d2 != null) {
                if (d1.doubleValue() > d2.doubleValue()) {
                    return 1;
                } else if (d1.doubleValue() < d2.doubleValue()) {
                    return -1;
                }
            } else if (d2 == null && d1 != null) {
                if (d1.doubleValue() > 0) {
                    return 1;
                }
            } else if (d1 == null && d2 != null) {
                if (d2.doubleValue() > 0) {
                    return -1;
                }
            } else {
                break;
            }
            i++;
        }
        return 0;
    }

	public static String arrayToString(String...args) {
		if (args == null) return null;
		StringBuffer strBuffer = new StringBuffer();
		for (int i=0; i<args.length; i++) {
			strBuffer.append(args[i]);
			if (i!=0) strBuffer.append(",");
		}
		return strBuffer.toString();
	}
	
	public static Long toLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String extractSuffix(String name) {
		if (isEmpty(name) || name.lastIndexOf(".") == -1)
			return "";
		int index = name.lastIndexOf(".") + 1;
		if (index < name.length()) {
			return name.substring(index).toUpperCase();
		} 
		return "";
	}
	
	public static String ellipsisStr(String str, int len) {
		if (isEmpty(str)) {
			return "";
		} else if (str.length() <= len) {
			return str;
		}
		str = str.substring(0, len);
		return str + "...";
	}
	
	public static String changeNum(String str){
			return String.valueOf(Long.parseLong(str));
	}
	
	/**
	 * 
	 * <pre>
	 * 判断字符串是否全部是数字
	 * </pre>
	 *
	 * @param str value
	 * @return
	 */
	public static boolean allNumber(String str){
	    String s = "(?=^.{1,10}$)\\d+(\\.\\d{1,2})?";
		Pattern pattern = Pattern.compile(s); 
	    return pattern.matcher(str).matches();
	}
	
	/**
	 * 
	 * <pre>
	 * 判断字符串是否是整数
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static boolean integerNumber(String str){
	   try{
	       if(str.length()<=9){
	           Long.valueOf(str);
	           return true;
	       }else{
	           return false;
	       }
	   }catch (Exception e){
	      return false;
	   }
	}
	/**
	 * 
	 * <pre>
	 * 用于把String型转成Long型
	 * </pre>
	 *
	 * @param value 要判断的值
	 * @return
	 */
	public static Long getDateTrimNullLong(String value){
        if(value != null && value != "" && value !="null"){
           return Long.parseLong(value) ;
       }else{
           return Long.parseLong("0");
       }
	}
	   /**
     * 
     * <pre>
     * 去除String中间空格
     * </pre>
     *
     * @param str 去空格的参数
     * @return
     */
	public static String deleteBlank(String str) {
	    if(str == null){
	        return "";
	    }
        String newStr = "";
        newStr  = str.replaceAll(" ", "");
        return newStr;
        
    }
}