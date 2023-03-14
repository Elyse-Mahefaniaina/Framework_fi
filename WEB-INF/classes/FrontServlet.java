package etu1784.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import etu1784.framework.Mapping;
import util.Util;
import etu1784.framework.MethodAnnotation;

public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> MappingUrls;
    protected Util util;

    @Override
    public void init() throws ServletException {
        this.util = new Util();
        this.MappingUrls = new HashMap<>();
        List<Class<?>> allClass = getClassesInPackage("model");
        Mapping mapping;
        Method[] allMethods;
        for(Class c : allClass) {
            allMethods = c.getMethods();

            for(Method m : allMethods) {
                if(m.isAnnotationPresent(MethodAnnotation.class)) {
                    mapping = new Mapping();
                    mapping.setClassName(c.getSimpleName());
                    mapping.setMethod(m.getAnnotation(MethodAnnotation.class).name());

                    MappingUrls.put(c.getSimpleName(), mapping);

                }
            }
        }
    }

    protected static List<Class<?>> getClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
                for (String file : new java.io.File(resource.toURI()).list()) {
                    if (file.endsWith(".class")) {
                        String className = packageName + '.' + file.substring(0, file.length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURL().toString();

        PrintWriter out = response.getWriter();
        out.print("url: " + util.processUrl(url, request.getContextPath()));
    }
}
