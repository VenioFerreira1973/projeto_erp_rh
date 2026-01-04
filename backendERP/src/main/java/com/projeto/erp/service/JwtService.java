package com.projeto.erp.service;

import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.dtos.UsuarioSecurityDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION;

    public String gerarToken(UsuarioSecurityDTO usuario) {

        List<PermissaoDTO> permissoes = usuario.permissoes();

        return Jwts.builder()
                .setSubject(usuario.login())
                .claim("permissoes", permissoes)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean tokenValido(String token, String login) {
        return extrairUsername(token).equals(login);
    }

    public Set<String> extrairPermissoes(String token) {
        List<?> permissoesRaw = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("permissoes", List.class);


        return permissoesRaw.stream()
                .filter(p -> p instanceof String)
                .map(p -> (String) p)
                .collect(Collectors.toSet());
    }
}
