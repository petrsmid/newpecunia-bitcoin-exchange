package com.newpecunia.thymeleaf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.newpecunia.thymeleaf.controllers.ThymeleafController;

public abstract class AbstractThymeleafServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Map<String, ThymeleafController> url2ControllerMapping = null; 
    private TemplateEngine templateEngine = null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        url2ControllerMapping = getUrl2ControllerMapping();
        initTemplateEnging();
    }
    
    protected abstract Map<String, ThymeleafController> getUrl2ControllerMapping();

	private void initTemplateEnging() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("LEGACYHTML5");
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("utf-8");
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        //write out headers
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //perform filling template and controlling
        ThymeleafController controller = getController(request);
        String result = controller.process(request, response, getServletContext(), templateEngine);

        //write out the result
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(result);
        } finally {
            out.close();
        }
    }

    protected ThymeleafController getController(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }
        
        String pathAfterContext = requestPath.substring(contextPath.length()).trim();
        
        return url2ControllerMapping.get(pathAfterContext);
    }

}
