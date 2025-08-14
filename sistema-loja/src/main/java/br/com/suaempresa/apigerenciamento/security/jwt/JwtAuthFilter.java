package br.com.suaempresa.apigerenciamento.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // Injeta as dependências via construtor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Extrai o header "Authorization"
        final String authHeader = request.getHeader("Authorization");

        // 2. Verifica se o header existe e se começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Se não, continua para o próximo filtro
            return;
        }

        // 3. Extrai o token JWT (remove o "Bearer ")
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        // 4. Se o usuário ainda não estiver autenticado no contexto de segurança
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário do banco de dados
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Cria um objeto de autenticação e o define no contexto de segurança
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) são nulas pois estamos usando JWT
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
