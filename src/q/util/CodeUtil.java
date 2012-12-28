package q.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CodeUtil {

	/**
	 * DES加密
	 * @param key 密钥，必须8位。
	 * @param text 待加密文本
	 */
	public static final String desEncode(String key, String text) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(new byte[]{1,2,3,4,5,6,7,8});
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, zeroIv);
		byte[] encryptedData = cipher.doFinal(text.getBytes());
		return base64Encode(encryptedData);
	}
	
	
	/**
	 * DES解密
	 * @param key 密钥，必须8位。
	 * @param text 待解密文本
	 */
	public static final String desDecode(String key, String text) throws Exception {
		byte[] byteMi = base64Decode(text);
		IvParameterSpec zeroIv = new IvParameterSpec(new byte[]{1,2,3,4,5,6,7,8});
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}
	
	/**
	 * MD5编码，如果不支持该编码则返回原始字符
	 */
	public static String md5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return str;
		}
		md.update(str.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	public static final String base64Encode(byte[] data) {
    	final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    

    public static final byte[] base64Decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            base64Decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            System.err.println("Error while decoding BASE64: " + ex.toString());
        }
        return decodedBytes;
    }

    private static final void base64Decode(String s, OutputStream os) throws IOException {
        int i = 0;
        int len = s.length();
        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (base64Decode(s.charAt(i)) << 18)
                    + (base64Decode(s.charAt(i + 1)) << 12)
                    + (base64Decode(s.charAt(i + 2)) << 6)
                    + (base64Decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }
    
    private static final int base64Decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
            default:
                throw new RuntimeException("unexpected code: " + c);
            }
    }
}
