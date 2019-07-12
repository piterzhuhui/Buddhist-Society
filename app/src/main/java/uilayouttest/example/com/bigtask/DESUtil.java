package uilayouttest.example.com.bigtask;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Hex;

public class DESUtil {
	//加密的key
	public static String key = "27650099-564A-4869-99B3-363F8129C0CD";



	/**
	 * UTF-8����
	 */
	public static final String ENCODED_UTF8 = "UTF-8";
	/**
	 * GBK����
	 */
	public static final String ENCODED_GBK = "GBK";
	/**
	 * GB2312����
	 */
	public static final String ENCODED_GB2312 = "GB2312";
	/**
	 * ISO8859-1����
	 */
	public static final String ENCODED_ISO88591 = "ISO8859-1";
	/**
	 * ASCII����
	 */
	public static final String ENCODED_ASCII = "ASCII";
	/**
	 * UNICODE����
	 */
	public static final String ENCODED_UNICODE = "UNICODE";
	/**
	 * CBC����ģʽ
	 */
	public static final String CIPHER_INSTANCE_CBC = "DES/CBC/PKCS5Padding";
	/**
	 * ECB����ģʽ
	 */
	public static final String CIPHER_INSTANCE_ECB = "DES/ECB/PKCS5Padding";

	/**
	 * DES����
	 * 
	 * @param HexString
	 *            �ַ�����16λ16�����ַ�����
	 * @param keyStr
	 *            ��Կ16��1
	 * @throws Exception
	 */
	public static String ENCRYPTMethod(String HexString, String keyStr)
			throws Exception {
		String jmstr = "";
		try {
			byte[] theKey = null;
			String jqstr = getstrByte(keyStr).substring(0, 8).toUpperCase();
			theKey = jqstr.getBytes(ENCODED_ASCII);
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_CBC);
			DESKeySpec desKeySpec = new DESKeySpec(theKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(theKey);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] theCph = cipher.doFinal(HexString.getBytes(ENCODED_GB2312));
			jmstr = toHexString(theCph).toUpperCase();
			jmstr = toHexString(theCph);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jmstr;
	}

	public static String getstrByte(String str) {
		if (null == str) {
			throw new IllegalArgumentException("str is null!");
		}
		MessageDigest messageDigest = getMessageDigest();
		byte[] digest;
		try {
			digest = messageDigest.digest(str.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("ASCII not supported!");
		}
		return new String(Hex.encodeHex(digest));
	}

	protected static final MessageDigest getMessageDigest() {
		String algorithm = "MD5";
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	/**
	 * DES����
	 * 
	 * @param HexString
	 *            �ַ�����16λ16�����ַ�����
	 * @param keyStr
	 *            ��Կ16��1
	 * @param keyENCODED
	 *            Keybyteת������
	 * @param HexStringENCODED
	 *            Ҫ����ֵ��ת��byte����
	 * @param CipherInstanceType
	 *            ��Ҫ��������
	 * @return
	 * @throws Exception
	 */
	public static String ENCRYPTMethod(String HexString, String keyStr,
			String keyENCODED, String HexStringENCODED,
			String CipherInstanceType) throws Exception {
		String jmstr = "";
		try {
			byte[] theKey = null;
			String jqstr = getstrByte(keyStr).substring(0, 8).toUpperCase();
			theKey = jqstr.getBytes(keyENCODED);
			Cipher cipher = Cipher.getInstance(CipherInstanceType);
			DESKeySpec desKeySpec = new DESKeySpec(theKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(theKey);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] theCph = cipher
					.doFinal(HexString.getBytes(HexStringENCODED));
			jmstr = toHexString(theCph).toUpperCase();
			jmstr = toHexString(theCph);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jmstr;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}

	/**
	 * DES���ܷ���
	 * 
	 * @param message
	 *            ��Ҫ�����ַ���
	 * @param key
	 *            ������Ҫ��KEY
	 * @param keyENCODED
	 *            ����KEYת������
	 * @param HexStringENCODED
	 *            �����ַ���ת������
	 * @param CipherInstanceType
	 *            ��������
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message, String key, String keyENCODED,
			String HexStringENCODED, String CipherInstanceType)
			throws Exception {

		byte[] bytesrc = convertHexString(message);
		byte[] theKey = null;
		String jqstr = getstrByte(key).substring(0, 8).toUpperCase();
		theKey = jqstr.getBytes(keyENCODED);
		Cipher cipher = Cipher.getInstance(CipherInstanceType);
		DESKeySpec desKeySpec = new DESKeySpec(theKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(theKey);

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, HexStringENCODED);
	}

	/**
	 * DES���ܷ���
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = convertHexString(message);
		byte[] theKey = null;
		String jqstr = getstrByte(key).substring(0, 8).toUpperCase();
		theKey = jqstr.getBytes(ENCODED_ASCII);
		Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_CBC);
		DESKeySpec desKeySpec = new DESKeySpec(theKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(theKey);

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, ENCODED_GB2312);
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}
}
