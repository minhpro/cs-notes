package my_group.web.filter;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my_group.myapp.JWTService;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

/**
 * Parse jwtToken from the authorization header of the request to get the token's subject.
 * Return http 401 if the parse fails.
 */
public class JWTFilter extends HttpFilter {
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_ATTRIBUTE_SUBJECT = "AUTH_ID";
    private JWTService jwtService;

     public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        Optional<String> tokenObject = parseTokenSubject(req);
        if (tokenObject.isPresent()) {
            req.setAttribute(AUTH_ATTRIBUTE_SUBJECT, tokenObject.get());
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private Optional<String> parseTokenSubject(HttpServletRequest req) {
        String authHeaderValue = req.getHeader(AUTH_HEADER);
        if (authHeaderValue != null && authHeaderValue.startsWith("Bearer ")) {
            String token = authHeaderValue.substring(7);
            return jwtService.parseTokenSubject(token);
        }
        return Optional.empty();
    }
}
