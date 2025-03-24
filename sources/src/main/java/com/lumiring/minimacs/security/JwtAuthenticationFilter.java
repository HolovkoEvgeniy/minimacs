package com.lumiring.minimacs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.constant.JwtErrorCode;
import com.lumiring.minimacs.domain.response.error.DomainError;
import com.lumiring.minimacs.domain.response.error.ErrorResponse;
import com.lumiring.minimacs.services.auth.JwtProvider;
import com.lumiring.minimacs.services.user.UserDetailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailService userDetailService;
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        try {
            var authHeader = request.getHeader(HEADER_NAME);
            var path = request.getRequestURI();


            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX) || path.startsWith("/api/v1/auth/") || path.startsWith("/actuator/")) {
                filterChain.doFilter(request, response);
                return;
            }


            var jwt = authHeader.substring(BEARER_PREFIX.length());
            JwtErrorCode code = jwtProvider.validateAccessToken(jwt);

            if (code != JwtErrorCode.OK) {
                wrapResponseWithErrorInfo(code, response);
                return;
            }

            Claims claims = jwtProvider.getAccessClaims(jwt);
            var username = claims.getSubject();

            UserDetails details = userDetailService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    details,
                    null,
                    details.getAuthorities()
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occurred in authentication filter : user taken from JWT subject part was not found");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(DomainError.builder()
                            .code(Code.JWT_EXPIRED)
                            .message("User taken from JWT subject part was not found")
                            .build())
                    .build();
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
    }



    private void wrapResponseWithErrorInfo(JwtErrorCode jwtErrorCode, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = createErrorResponse(jwtErrorCode.getVale());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        response.setStatus(BAD_REQUEST.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
    }

    private ErrorResponse createErrorResponse(String message) {
        return ErrorResponse.builder().error(DomainError.builder().code(Code.JWT_EXPIRED).message(message).build()).build();
    }

}
