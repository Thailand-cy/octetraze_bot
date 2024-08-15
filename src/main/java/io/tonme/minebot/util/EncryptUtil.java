package io.tonme.minebot.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson2.JSONObject;
import java.util.Base64;

public class EncryptUtil {

	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCExutUK0VuL3xtyl3ZCkI16eqo/iqAzjBr/Bfqgrlt+FIOz4naruB8JT+Kvu+vUmxR//ggfk4MfmGT7iOBq0VLkAh8S38NNAqM+ZXaPboIpGTnExDyRYZaA/L34DWfCJQAuMvhibCjdl3oulJgI1hsnLLiiEN5B2eRfoopJGE88wIDAQAB";
	public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAITG61QrRW4vfG3KXdkKQjXp6qj+KoDOMGv8F+qCuW34Ug7Pidqu4HwlP4q+769SbFH/+CB+Tgx+YZPuI4GrRUuQCHxLfw00Coz5ldo9ugikZOcTEPJFhloD8vfgNZ8IlAC4y+GJsKN2Xei6UmAjWGycsuKIQ3kHZ5F+iikkYTzzAgMBAAECgYARcNGbkj2B/jHReBoAtuTAkTPWM+CgX7Ujkg8QKlCyHcKlmGRKudrZ48incHIqbOC2mrFcj0qwQG+Pz2GRmE43MMh5+SzxmC1Li739woZuccW4+EZ8P0bjQO+vV9n272l+9i00VitPB5Z2d5wB7y/QDAX9Nz4ySaqjYeASoECk1QJBALuaRatzDUMIwmNDvkIKzfTKqxvQbw4ectZqGJiVdkz8biEUy6dqnEWhwx32qRWayrmlbrEQt0I5dCJe0A56NW0CQQC1L4w07tGXUgDMbZKEvdEQsTbs3/AFKNAXbR5HXRstB6i6PHIEIEE9+8UUNA7dRncN3AGBex+gqfyxQFAy9p/fAkEAuXeYPxrcVpRXUTmM48+4s7SVm0biNAwoLg/vxxGkRhIdcnkTqay9WB3HP1025lQE7ao+l7DV7BPO25fuixGItQJATaGBwWxSATPxg6WvEZEC8tP7ZKLOhGdoQaFPR+RssG6HrMQZVgs76QLm/jEP7V8zw4xwWLoYYw5yZr/XnP5uoQJAP5qfFvnHNbanwwPL+CVKcdte+wKbx/OjYC/iGNwJoHctjuqAKZRoLuc2onAp8VeodVANkUJpn4O1CuOlsSAnLQ==";
	public static final RSA rsa = new RSA(PRIVATE_KEY, PUBLIC_KEY);
	/**
	 * 公钥加密
	 * @param data 需要加密的消息(JSON | 字符串)
	 * @return 加密后的数据
	 */
	public static String encrypt(String data) {
		byte[] encryptedData = rsa.encrypt(data.getBytes(), KeyType.PublicKey);
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	/**
	 * 公钥加密
	 * @param data 需要加密的消息(JSON | 字符串)
	 * @return 加密后的数据
	 */
	public static String encrypt(Object data) {
		String signData = JSONObject.toJSONString(data);
		byte[] encryptedData = rsa.encrypt(signData.getBytes(), KeyType.PublicKey);
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	/**
	 * 私钥解密
	 * @param encryptBase64 加过密的
	 * @return
	 */
	public static String decrypt(String encryptBase64) {
		byte[] encryptedData = Base64.getDecoder().decode(encryptBase64);
		byte[] decryptedData = rsa.decrypt(encryptedData, KeyType.PrivateKey);
		return new String(decryptedData);
	}
//	public static void main(String[] args) {
//
//		// 原始数据
//		AuthDoLoginReq loginReq = new AuthDoLoginReq();
//		loginReq.setSing("aSDASDADASDASDASDASDASD");
//		loginReq.setIp("123.1231.123.123");
//		String data = JSONObject.toJSONString(loginReq);
//
//		String encrypt = encrypt(data);
//		System.out.println("Encrypted Data: " + encrypt);
//		String decrypt = decrypt(encrypt);
//		System.out.println("Decrypted Data: " + decrypt);
//	}
}
