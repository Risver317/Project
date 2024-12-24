package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(255) NOT NULL, " +
            "userlastname VARCHAR(255) NOT NULL, " +
            "age INT NOT NULL)";

    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS users";

    private static final String CLEAN_USER_TABLE = "TRUNCATE TABLE users";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery(CREATE_USER_TABLE).executeUpdate();
            tx.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery(DROP_USER_TABLE).executeUpdate();
            tx.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx.commit();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (HibernateException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeQuery(CLEAN_USER_TABLE).executeUpdate();
            tx.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
