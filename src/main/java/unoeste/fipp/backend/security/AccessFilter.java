package unoeste.fipp.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token= ((HttpServletRequest)servletRequest).getHeader("Authorization");
        //if(token!=null && JWTTokenProvider.verifyToken(token)) {
        if(0==0){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            ((HttpServletResponse) servletResponse).setStatus(500);
            servletResponse.getOutputStream().write("Não autorizado ".getBytes());
        }
    }
}
