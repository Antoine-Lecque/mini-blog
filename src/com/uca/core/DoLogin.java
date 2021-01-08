package com.uca.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoLogin {

    private final static String TOKEN = "QVAlKTzo1zW9VwfGvJtrFZiSOzzEzEyb4Q4qdYIYncKqhd4l7Iasgq8LbesvH01Jk8kA49HNt9fq4M4Lpjpjvysyso7egZNlmHSU";
    private final static String TOKEN2 = "QVAlKTzo1zW9VwfGvJtrFZiSOzzEzEyb4Q4qdYIYncKqhd4l7Iasgq8LbesvH01Jk8kA49HNt9fq4M4Lpjpjvypyso7egZNlmHSU";

    public static Map<String, String> introspect(String token){
        Claims claims = Jwts.parser().setSigningKey(TOKEN2).parseClaimsJws(token).getBody();
        Map<String, String> map = new HashMap<>();
        map.put("sub", claims.get("sub", String.class));
        map.put("uuid", claims.get("uuid", String.class));
        map.put("email", claims.get("email", String.class));

        return map;
    }

    public static String createToken(String name, String uuid, String email){
        Map<String, String> content = new HashMap<>();
        content.put("sub", name);
        content.put("uuid", uuid);
        content.put("email", email);

        return Jwts.builder().setClaims(content)
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, TOKEN)
                .compact();
    }

}