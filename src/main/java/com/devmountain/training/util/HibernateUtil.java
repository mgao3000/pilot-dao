package com.devmountain.training.util;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HibernateUtil {
    private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private HibernateUtil() {}

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = loadSessionFatory();
        } catch (Exception e) {
            logger.error("Exception while initializing hibernate util..., error={}",
                    e.getMessage());
        }
    }
    private static SessionFactory loadSessionFatory() {
        String dbDriver = "org.postgresql.Driver";
        String dbDialect = "org.hibernate.dialect.PostgreSQL9Dialect";
        String dbUrl = "jdbc:postgresql://localhost:5432/pilot";
        String dbUser = "admin";
        String dbPassword = "password";

        String[] modelPackages = {"com.devmountain.training.entity"};
//        String dbDriver = System.getProperty("database.driver");
//        String dbDialect = System.getProperty("database.dialect");
//        String dbUrl = System.getProperty("database.url");
//        String dbUser = System.getProperty("database.user");
//        String dbPassword = System.getProperty("database.password");

        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, dbDriver);
        settings.put(Environment.DIALECT, dbDialect);
        settings.put(Environment.URL, dbUrl);
        settings.put(Environment.USER, dbUser);
        settings.put(Environment.PASS, dbPassword);
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "validate");
        configuration.setProperties(settings);

        EntityScanner.scanPackages(modelPackages).addTo(configuration);
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        ServiceRegistry serviceRegistry = registryBuilder.applySettings(configuration.getProperties()).build();
        SessionFactory localSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return localSessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() throws HibernateException {

        Session retSession=null;
        try {
            retSession = sessionFactory.openSession();
        }catch(Throwable t){
            logger.error("Exception while getting session.. ");
            t.printStackTrace();
        }
        if(retSession == null) {
            logger.error("session is discovered null");
        }

        return retSession;
    }

    /**
     * This main method is implemented as a testing point to verify your Hibernate configuration is correct
     * @param args
     */
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Success generate sessionFactory ={}", sessionFactory.toString());
        logger.info("Success generate sessionFactory, hashcode={}", sessionFactory.hashCode());
        Session session = sessionFactory.openSession();
        logger.info("The end");
        session.close();
    }

}
