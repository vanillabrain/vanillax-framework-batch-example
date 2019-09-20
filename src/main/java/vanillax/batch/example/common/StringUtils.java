package vanillax.batch.example.common;

import java.security.MessageDigest;
import java.util.UUID;

public class StringUtils {
    public static String hash(String prefix, String str)throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((prefix+str).getBytes("UTF-8"));
        byte[] digest = md.digest();
        return  byteArrayToHex(digest);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02X", b));
        return sb.toString();
    }

    public static String makeToken(){
        UUID one = UUID.randomUUID();
        return one.toString().toUpperCase().replaceAll("-","");
    }
}
