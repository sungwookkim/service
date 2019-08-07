package util;

public class SinnakeStringUtil {
	/**
	 * 객체 NULL 검사 후 String형으로 반환
	 * @param o
	 * @return
	 */
	public static String objectIsNullByString(Object o) {
		return (o == null || o.toString().trim().equals("")) ? "" : o.toString().trim();
	}
		
	/**
	 * 객체 NULL 검사 후 int형으로 반환
	 * @param o 
	 * @param iniValue
	 * @return
	 */
	public static int objectIsNullByInt(Object o, int iniValue) {
		return (o == null || o.toString().trim().equals("")) ? iniValue : Integer.parseInt(o.toString().trim());
	}
	
	/**
	 * 객체 NULL 검사 후 long형으로 반환
	 * @param o 
	 * @param iniValue
	 * @return
	 */
	public static long objectIsNullByLong(Object o, long iniValue) {
		return (o == null || o.toString().trim().equals("")) ? iniValue :  Long.parseLong(o.toString().trim());
	}
	
	/**
	 * 객체 NULL 검사 후 byte형으로 반환
	 * @param o 
	 * @param iniValue
	 * @return
	 */
	public static byte objectIsNullByByte(Object o, byte iniValue) {
		return (o == null || o.toString().trim().equals("")) ? iniValue : Byte.parseByte(o.toString().trim());
	}	
	
	/**
	 * 객체 NULL 검사 후 boolean형으로 반환
	 * @param o
	 * @return
	 * Object가 Null이거나 공백이면 True 반환
	 */
	public static boolean objectIsNullByboolean(Object o) {
					
		if (o == null || o.toString().trim().equals(""))
			return true;
		else
			return false;		
	}
	
	/**
	 * 객체 NULL 검사 후 boolean형으로 반환
	 * @param o
	 * @return
	 * Object가 Null이거나 0이면 false 반환 그외 true
	 */
	public static boolean objectIsNullByBoolean(Object o) {
	
		if (o == null || o.toString().trim().equals("")){
			return false;
		}else if(o.toString().equals("1")){
			return true;
		}else if(o.toString().equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 객체 NULL 검사 후 boolean형으로 반환
	 * @param o
	 * @return
	 * 1.String 이 Null이거나 공백이면 false 반환  2.String 이 0 또는 false 이면 false, 1 또는 true 이면 true 반환
	 */
	public static boolean StringToboolean(String str) {
		
		if (str == null || str.trim().equals(""))
			return false;
		else{
			if(str.toString().toLowerCase().equals("true") || str.toLowerCase().equals("1")){
				return true;
			}else if(str.toString().toLowerCase().equals("false") || str.toLowerCase().equals("0")){
				return false;
			}	
			return false;
		}
	}
}
