package com.bom.zcloudbackend.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

@Component
public class EncryptUserUtil {


    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    private static final String SECRET = "AES";

    private static final String masterKey = "AES454-HTJSQ9-IT";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * aes加密字符串
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] keyBytes = masterKey.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, SECRET));
        byte[] doFinal = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(doFinal));
    }

    /**
     * aes解密字符串
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] keyBytes = masterKey.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, SECRET));
        byte[] doFinal = cipher.doFinal(Base64.getDecoder().decode(str));
        return new String(doFinal);
    }

}
