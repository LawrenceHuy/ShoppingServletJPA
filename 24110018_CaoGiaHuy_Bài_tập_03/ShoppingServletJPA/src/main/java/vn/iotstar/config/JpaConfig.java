package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/** Tạo EntityManagerFactory một lần duy nhất (tốn kém) và cấp EntityManager cho từng thao tác. */
public class JpaConfig {
    private static final String PERSISTENCE_UNIT = "jpa-hibernate-sqlserver";
    private static EntityManagerFactory factory;

    public static synchronized EntityManagerFactory getFactory() {
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    public static synchronized void shutdown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
        factory = null;
    }
}
