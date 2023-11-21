package my_group.web.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaderWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> headerMap;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public RequestHeaderWrapper(HttpServletRequest request) {
        super(request);
        headerMap = new HashMap<>();
    }

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String value =  super.getHeader(name);
        if (value != null) {
            return value;
        }
        return headerMap.get(name);
    }
}
