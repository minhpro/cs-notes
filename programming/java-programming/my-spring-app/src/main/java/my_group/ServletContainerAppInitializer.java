package my_group;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import my_group.web.LogFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

/**
 * Use for deployment with an external Webserver
 */
public class ServletContainerAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        try {
            WebApplicationContext ctx = App.getWebApplicationContext();
            // Create and register the DispatcherServlet
            DispatcherServlet servlet = new DispatcherServlet(ctx);
            ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
            registration.setLoadOnStartup(1);
            registration.addMapping("/*");
            servletContext.addFilter("logFilter", LogFilter.class)
                    .addMappingForUrlPatterns(null, false, "/hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
