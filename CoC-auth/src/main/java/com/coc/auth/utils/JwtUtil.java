package com.coc.auth.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.coc.auth.constant.JwtConstant;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class JwtUtil {

    /**
     * 由字符串生成加密key
     */
    private static SecretKey generalKey() {
        String stringKey = JwtConstant.GENERAL_KEY;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 创建jwt
     */
    public static String createJWT(String id, String subject, long amountToAdd, TemporalUnit unit) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expTime = nowTime.plus(amountToAdd, unit);
        Date now = Date.from(nowTime.toInstant(ZoneOffset.ofHours(8)));
        Date exp = Date.from(expTime.toInstant(ZoneOffset.ofHours(8)));
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setIssuer(JwtConstant.ISSUER)
                .setSubject(subject)
                .signWith(signatureAlgorithm, generalKey())
                .setExpiration(exp);
        return builder.compact();
    }

    /**
     * 解密jwt
     */
    public static Claims parseJWT(String jwt) throws ExpiredJwtException {
        SecretKey key = generalKey();
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
    }
}
