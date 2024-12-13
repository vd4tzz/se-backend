package com.example.demo.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtUtils;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    JwtUtils jwtUtils;

    UserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    public JwtFilter() {}

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) 
                                throws IOException, ServletException {
                                                                                 
        String authField =  request.getHeader("Authorization");
        String token = null;
        String username = null;
        String role = null;
                                

        if(authField != null && authField.startsWith("Bearer ")) {
            token = authField.substring(7);
            // username = jwtUtils.extractUsername(token);
            try {
                username = jwtUtils.extractUsername(token);
            } catch(JwtException e) {
                System.out.println(e);
            }
        }
        
        if(username != null) {
            
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                role = userDetails.getAuthorities().iterator().next().getAuthority();

                UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch(UsernameNotFoundException e) {
                System.out.println(e);
            }
            
            if(jwtUtils.isUnder10Mins(token))
                response.addHeader("Authorization", jwtUtils.generateToken(username, role));
        }

        System.out.println("Filter is working ");
        filterChain.doFilter(request, response);
       
    }
    
}
