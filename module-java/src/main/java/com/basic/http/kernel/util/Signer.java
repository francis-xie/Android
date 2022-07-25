package com.basic.http.kernel.util;

import com.basic.http.util.StreamUtil;
import com.basic.http.kernel.Constants;
import com.basic.http.utils.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SHA256WithRSA签名器
 *
 * @author zhongyu
 * @version $Id: Signer.java, v 0.1 2019年12月19日 9:10 PM zhongyu Exp $
 */
public class Signer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Signer.class);

    public static String getSignCheckContent(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
        }
        return content.toString();
    }

    /**
     * 验证签名
     *
     * @param content      待验签的内容
     * @param sign         签名值的Base64串
     * @param publicKeyPem 支付宝公钥
     * @return true：验证成功；false：验证失败
     */
    public static boolean verify(String content, String sign, String publicKeyPem) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(Constants.RSA);
            byte[] encodedKey = publicKeyPem.getBytes();
            encodedKey = Base64.decode(encodedKey);
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance(Constants.SHA_256_WITH_RSA);
            signature.initVerify(publicKey);
            signature.update(content.getBytes(Constants.DEFAULT_CHARSET));
            return signature.verify(Base64.decode(sign.getBytes()));
        } catch (Exception e) {
            String errorMessage = "验签遭遇异常，content=" + content + " sign=" + sign +
                    " publicKey=" + publicKeyPem + " reason=" + e.getMessage();
            LOGGER.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * 计算签名
     *
     * @param content       待签名的内容
     * @param privateKeyPem 私钥
     * @return 签名值的Base64串
     */
    public String sign(String content, String privateKeyPem) {
        try {
            byte[] encodedKey = privateKeyPem.getBytes();
            encodedKey = Base64.decode(encodedKey);
            PrivateKey privateKey = KeyFactory.getInstance(Constants.RSA).generatePrivate(new PKCS8EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance(Constants.SHA_256_WITH_RSA);
            signature.initSign(privateKey);
            signature.update(content.getBytes(Constants.DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return new String(Base64.encode(signed));
        } catch (Exception e) {
            String errorMessage = "签名遭遇异常，content=" + content + " privateKeySize=" + privateKeyPem.length() + " reason=" + e.getMessage();
            LOGGER.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * 对参数集合进行验签
     *
     * @param parameters 参数集合
     * @param publicKey  支付宝公钥
     * @return true：验证成功；false：验证失败
     */
    public static boolean verifyParams(Map<String, String> parameters, String publicKey) {
        String sign = parameters.get(Constants.SIGN_FIELD);
        parameters.remove(Constants.SIGN_FIELD);
        parameters.remove(Constants.SIGN_TYPE_FIELD);

        String content = getSignCheckContent(parameters);

        return verify(content, sign, publicKey);
    }

    //////////////////////////////////////////////////////////////////
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;


    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for(int i = 0; i < keys.size(); ++i) {
            String key = (String)keys.get(i);
            String value = (String)sortedParams.get(key);
            if (StringUtils.areNotEmpty(new String[]{key, value})) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    public static String rsaSign(String content, String privateKey, String charset, String signType) throws RuntimeException {
        if ("RSA".equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if ("RSA2".equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            throw new RuntimeException("Sign Type is Not Support : signType=" + signType);
        }
    }

    public static String rsa256Sign(String content, String privateKey, String charset) throws RuntimeException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(com.basic.http.util.codec.Base64.encodeBase64(signed));
        } catch (Exception var6) {
            throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, var6);
        }
    }

    public static String rsaSign(String content, String privateKey, String charset) throws RuntimeException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(com.basic.http.util.codec.Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException var6) {
            throw new RuntimeException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", var6);
        } catch (Exception var7) {
            throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, var7);
        }
    }

    public static String rsaSign(Map<String, String> params, String privateKey, String charset) throws RuntimeException {
        String signContent = getSignContent(params);
        return rsaSign(signContent, privateKey, charset);
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins != null && !StringUtils.isEmpty(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            byte[] encodedKey = StreamUtil.readText(ins).getBytes();
            encodedKey = com.basic.http.util.codec.Base64.decodeBase64(encodedKey);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static String getSignCheckContentV1(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            params.remove("sign");
            params.remove("sign_type");
            StringBuffer content = new StringBuffer();
            List<String> keys = new ArrayList(params.keySet());
            Collections.sort(keys);

            for(int i = 0; i < keys.size(); ++i) {
                String key = (String)keys.get(i);
                String value = (String)params.get(key);
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }

            return content.toString();
        }
    }

    public static String getSignCheckContentV2(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            params.remove("sign");
            StringBuffer content = new StringBuffer();
            List<String> keys = new ArrayList(params.keySet());
            Collections.sort(keys);

            for(int i = 0; i < keys.size(); ++i) {
                String key = (String)keys.get(i);
                String value = (String)params.get(key);
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }

            return content.toString();
        }
    }

    public static boolean rsaCheckV1(Map<String, String> params, String publicKey, String charset) throws RuntimeException {
        String sign = (String)params.get("sign");
        String content = getSignCheckContentV1(params);
        return rsaCheckContent(content, sign, publicKey, charset);
    }

    public static boolean rsaCheckV1(Map<String, String> params, String publicKey, String charset, String signType) throws RuntimeException {
        String sign = (String)params.get("sign");
        String content = getSignCheckContentV1(params);
        return rsaCheck(content, sign, publicKey, charset, signType);
    }

    public static boolean rsaCheckV2(Map<String, String> params, String publicKey, String charset) throws RuntimeException {
        String sign = (String)params.get("sign");
        String content = getSignCheckContentV2(params);
        return rsaCheckContent(content, sign, publicKey, charset);
    }

    public static boolean rsaCheckV2(Map<String, String> params, String publicKey, String charset, String signType) throws RuntimeException {
        String sign = (String)params.get("sign");
        String content = getSignCheckContentV2(params);
        return rsaCheck(content, sign, publicKey, charset, signType);
    }

    public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType) throws RuntimeException {
        if ("RSA".equals(signType)) {
            return rsaCheckContent(content, sign, publicKey, charset);
        } else if ("RSA2".equals(signType)) {
            return rsa256CheckContent(content, sign, publicKey, charset);
        } else {
            throw new RuntimeException("Sign Type is Not Support : signType=" + signType);
        }
    }

    public static boolean rsa256CheckContent(String content, String sign, String publicKey, String charset) throws RuntimeException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(pubKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return signature.verify(com.basic.http.util.codec.Base64.decodeBase64(sign.getBytes()));
        } catch (Exception var6) {
            throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, var6);
        }
    }

    public static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws RuntimeException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return signature.verify(com.basic.http.util.codec.Base64.decodeBase64(sign.getBytes()));
        } catch (Exception var6) {
            throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, var6);
        }
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(ins), writer);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = com.basic.http.util.codec.Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    public static String checkSignAndDecrypt(Map<String, String> params, String alipayPublicKey, String cusPrivateKey, boolean isCheckSign, boolean isDecrypt) throws RuntimeException {
        String charset = (String)params.get("charset");
        String bizContent = (String)params.get("biz_content");
        if (isCheckSign && !rsaCheckV2(params, alipayPublicKey, charset)) {
            throw new RuntimeException("rsaCheck failure:rsaParams=" + params);
        } else {
            return isDecrypt ? rsaDecrypt(bizContent, cusPrivateKey, charset) : bizContent;
        }
    }

    public static String checkSignAndDecrypt(Map<String, String> params, String alipayPublicKey, String cusPrivateKey, boolean isCheckSign, boolean isDecrypt, String signType) throws RuntimeException {
        String charset = (String)params.get("charset");
        String bizContent = (String)params.get("biz_content");
        if (isCheckSign && !rsaCheckV2(params, alipayPublicKey, charset, signType)) {
            throw new RuntimeException("rsaCheck failure:rsaParams=" + params);
        } else {
            return isDecrypt ? rsaDecrypt(bizContent, cusPrivateKey, charset) : bizContent;
        }
    }

    public static String encryptAndSign(String bizContent, String alipayPublicKey, String cusPrivateKey, String charset, boolean isEncrypt, boolean isSign) throws RuntimeException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(charset)) {
            charset = "GBK";
        }

        sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        String encrypted;
        if (isEncrypt) {
            sb.append("<alipay>");
            encrypted = rsaEncrypt(bizContent, alipayPublicKey, charset);
            sb.append("<response>" + encrypted + "</response>");
            sb.append("<encryption_type>RSA</encryption_type>");
            if (isSign) {
                String sign = rsaSign(encrypted, cusPrivateKey, charset);
                sb.append("<sign>" + sign + "</sign>");
                sb.append("<sign_type>RSA</sign_type>");
            }

            sb.append("</alipay>");
        } else if (isSign) {
            sb.append("<alipay>");
            sb.append("<response>" + bizContent + "</response>");
            encrypted = rsaSign(bizContent, cusPrivateKey, charset);
            sb.append("<sign>" + encrypted + "</sign>");
            sb.append("<sign_type>RSA</sign_type>");
            sb.append("</alipay>");
        } else {
            sb.append(bizContent);
        }

        return sb.toString();
    }

    public static String encryptAndSign(String bizContent, String alipayPublicKey, String cusPrivateKey, String charset, boolean isEncrypt, boolean isSign, String signType) throws RuntimeException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(charset)) {
            charset = "GBK";
        }

        sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        String encrypted;
        if (isEncrypt) {
            sb.append("<alipay>");
            encrypted = rsaEncrypt(bizContent, alipayPublicKey, charset);
            sb.append("<response>" + encrypted + "</response>");
            sb.append("<encryption_type>RSA</encryption_type>");
            if (isSign) {
                String sign = rsaSign(encrypted, cusPrivateKey, charset, signType);
                sb.append("<sign>" + sign + "</sign>");
                sb.append("<sign_type>");
                sb.append(signType);
                sb.append("</sign_type>");
            }

            sb.append("</alipay>");
        } else if (isSign) {
            sb.append("<alipay>");
            sb.append("<response>" + bizContent + "</response>");
            encrypted = rsaSign(bizContent, cusPrivateKey, charset, signType);
            sb.append("<sign>" + encrypted + "</sign>");
            sb.append("<sign_type>");
            sb.append(signType);
            sb.append("</sign_type>");
            sb.append("</alipay>");
        } else {
            sb.append(bizContent);
        }

        return sb.toString();
    }

    public static String rsaEncrypt(String content, String publicKey, String charset) throws RuntimeException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, pubKey);
            byte[] data = StringUtils.isEmpty(charset) ? content.getBytes() : content.getBytes(charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
                byte[] cache;
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(data, offSet, 117);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            byte[] encryptedData = com.basic.http.util.codec.Base64.encodeBase64(out.toByteArray());
            out.close();
            return StringUtils.isEmpty(charset) ? new String(encryptedData) : new String(encryptedData, charset);
        } catch (Exception var12) {
            throw new RuntimeException("EncryptContent = " + content + ",charset = " + charset, var12);
        }
    }

    public static String rsaDecrypt(String content, String privateKey, String charset) throws RuntimeException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, priKey);
            byte[] encryptedData = StringUtils.isEmpty(charset) ? com.basic.http.util.codec.Base64.decodeBase64(content.getBytes()) : com.basic.http.util.codec.Base64.decodeBase64(content.getBytes(charset));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
                byte[] cache;
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(encryptedData, offSet, 128);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            byte[] decryptedData = out.toByteArray();
            out.close();
            return StringUtils.isEmpty(charset) ? new String(decryptedData) : new String(decryptedData, charset);
        } catch (Exception var12) {
            throw new RuntimeException("EncodeContent = " + content + ",charset = " + charset, var12);
        }
    }
}