package my_group.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Reflect ServletOutputStream to an OutputStream such as {@code System.out}
 * Which response to client will write to the OutputStream also.
 */
public class ReflectResponseFilter extends HttpFilter {
    final static Logger LOG = LogManager.getLogger(ReflectResponseFilter.class);

    private final transient OutputStream targetStream;

    public ReflectResponseFilter(OutputStream targetStream) {
        this.targetStream = targetStream;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.info("Reflect response filter {}", request.getRequestURI());
        chain.doFilter(request, loggingResponseWrapper(response, targetStream));
    }

    private HttpServletResponse loggingResponseWrapper(HttpServletResponse response, OutputStream targetStream) {
        return new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new DelegateServletOutputStream(new TeeOutputStream(super.getOutputStream(), targetStream));
            }
        };
    }
}
