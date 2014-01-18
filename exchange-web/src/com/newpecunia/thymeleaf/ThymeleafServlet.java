package com.newpecunia.thymeleaf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet(name = "thymeleaf", urlPatterns = {"/buy/", "/sell/"})
public class ThymeleafServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Map<String, String> url2TemplateMap = new HashMap<>();
    static {
    	url2TemplateMap.put("/buy/", "/buy/buy.html");
    	url2TemplateMap.put("/sell/", "/sell/sell.html");
    };
    
    private TemplateEngine templateEngine;
    
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTemplateEnging();
    }
    
    private void initTemplateEnging() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("LEGACYHTML5");
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheable(true);
        templateResolver.setCacheTTLMs(3600000L);
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

        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        String templateName = getTemplateName(request);
        String result = templateEngine.process(templateName, ctx);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(result);
        } finally {
            out.close();
        }
    }

    protected String getTemplateName(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }
        
        String pathAfterContext = requestPath.substring(contextPath.length()).trim();
        
        return url2TemplateMap.get(pathAfterContext);
    }
}