package com.facturador.security;

import com.facturador.tenant.context.TenantContext;
import com.facturador.tenant.model.Tenant;
import com.facturador.tenant.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Api-Key";

    private final TenantService tenantService;

    public ApiKeyAuthenticationFilter(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String apiKey = resolveApiKey(request);
            if (apiKey != null) {
                Tenant tenant = tenantService.resolveByApiKey(apiKey);
                TenantContext.setTenant(tenant);
                User principal = new User(tenant.getName(), "", java.util.List.of());
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, apiKey, java.util.List.of());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (com.facturador.tenant.exception.TenantNotFoundException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        } finally {
            TenantContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private String resolveApiKey(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        if (header == null || header.isBlank()) {
            return null;
        }
        return header.trim();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }
}
