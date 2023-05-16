package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.hibernate.VillainEntity;

/**
 * Необходимо подключить в pom.xml
 * hibernate-core и jakarta.persistence
 * к проекту (и все еще нужен JDBC-драйвер)
 *
 * Для данной реализации необходим файл resources/META-INF/
 */
public class SimpleHibernateEntityManager {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("head_hunter");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();

        // region 1) Список всех злодеев (поиск)
        System.out.println("Список всех злодеев: ");
        entityManager
                .createQuery("SELECT v FROM VillainEntity v", VillainEntity.class).getResultList()
                .forEach(System.out::println);
        System.out.println();
        // endregion

        entityManagerFactory.close();   // WARNING! Not autocloseable
    }
}
