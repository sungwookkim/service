package member;

import util.SinnakeAES256Util;

public class test {

	public static void main(String[] args) throws Exception {
		System.out.println(new SinnakeAES256Util("sinnakeAes256@$%PW").aesDecode("gxh2mS7pEieZTC2o0fbUMF1VpBYNljHlz3Txn3A3Z3xCwl2d4tohVbbf4VO4OSgD"));
		System.out.println(new SinnakeAES256Util("sinnakeAes256@$%PW").aesEncode("jdbc:mysql://192.168.0.33:3306/sinnake"));
	}
}
