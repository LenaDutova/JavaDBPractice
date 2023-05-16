package org.example;

import org.example.hibernate.MinionEntity;
import org.example.hibernate.VillainEntity;
import org.example.hibernate.VillainsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Необходимо подключить в pom.xml
 * hibernate-core и jakarta.persistence
 * к проекту (и все еще нужен JDBC-драйвер)
 *
 * Для данной реализации необходим файл resources/hibernate.cfg.xml
 */
public class SimpleHibernateSessions {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure();    // configure("hibernate.cfg.xml") as default
//        cfg.addAnnotatedClass(ContractEntity.class) // Annotated entity here or in "hibernate.cfg.xml"
//                .addAnnotatedClass(MinionEntity.class)
//                .addAnnotatedClass(VillainEntity.class);

        try (SessionFactory sessionFactory = cfg.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            // region 1) Список всех злодеев (поиск)
            System.out.println("Список всех злодеев: ");
//            session.createQuery("SELECT v FROM VillainsEntity v", VillainsEntity.class)
            session.createNamedQuery("find_all", VillainsEntity.class)
                    .getResultList()
                    .forEach(System.out::println);
            System.out.println();
            // endregion

            // region 2) Описание конкретного злодея (поиск по параметру)
            System.out.println("Описание конкретного злодея: ");
            VillainEntity villain = session.get(VillainEntity.class, "Грю");
            System.out.println(villain);
            System.out.println();
            // endregion

            // region 3) Описание конкретного пособника (поиск по параметру)
            System.out.println("Описание конкретного пособника: ");
            MinionEntity minion = session.get(MinionEntity.class, "Доктор Нефарио");
            System.out.println(minion);

//            minion = session.get(MinionEntity.class, "Карл");
//            System.out.println(minion);
            System.out.println();
            // endregion

            // region 4) Заключение контракта (добавление)
            System.out.println("Заключение контракта:");
            if (villain != null && minion != null && !villain.minions.contains(minion)){
                if (villain.minions.add(minion)){
                    session.beginTransaction();
                    session.persist(villain);   // необходима открытая транзакция
                    session.getTransaction().commit();
                    System.out.println("Добавлен контракт");
                } // else - уже был контракт
            }
            System.out.println();
            // endregion

            // region 6) Разрыв контракта (удаление)
            System.out.println("Разрыв контракта:");
            if (villain != null && minion != null && villain.minions.contains(minion)){
                if (villain.minions.remove(minion)){
                    session.beginTransaction();
                    session.persist(villain);   // необходима открытая транзакция
//                    session.remove(villain);  // или удалить кортеж и все связанные с ним если прописан CASCADE.ALL
                    session.getTransaction().commit();
                    System.out.println("Контракт разорван");
                } // else - уже был контракт
            }
            System.out.println();
            // endregion

        } // session.close()
    }
}
