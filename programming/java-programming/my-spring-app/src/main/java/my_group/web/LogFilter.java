package my_group.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter
public class LogFilter extends HttpFilter {
    final static Logger LOG = LogManager.getLogger(LogFilter.class);

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.info("Log filter {}", request.getRequestURI());
        response.setHeader("myHeader", "myValue");
        var wrappedRequest = new RequestHeaderWrapper(request);
        wrappedRequest.addHeader("myHeader", "myValue");
        chain.doFilter(wrappedRequest, response);
    }
}
