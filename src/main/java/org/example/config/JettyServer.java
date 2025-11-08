package org.example.config;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        // Используем ServletContextHandler для Jetty 11+
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/main/webapp");

        // Создаем Spring контекст
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(SpringConfig.class);

        // Создаем DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);

        // Регистрируем DispatcherServlet - для Jetty 11+ с Jakarta
        ServletHolder servletHolder = new ServletHolder("dispatcher", dispatcherServlet);
        servletHolder.setInitOrder(1);
        context.addServlet(servletHolder, "/*");

        // РЕГИСТРАЦИЯ ФИЛЬТРА ДЛЯ PATCH МЕТОДОВ
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        FilterHolder filterHolder = new FilterHolder(hiddenHttpMethodFilter);
        context.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));



        server.setHandler(context);

        try {
            server.start();
            System.out.println("✅ Jetty 11+ сервер запущен успешно!");
            System.out.println("📚 Доступ к книгам: http://localhost:8080/books/");
            server.join();
        } catch (Exception e) {
            System.err.println("❌ Ошибка запуска Jetty: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}