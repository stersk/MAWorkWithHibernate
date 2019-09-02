package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class UserDAO {
  public static User save(User user) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Integer id = (Integer) session.save(user);
    session.getTransaction().commit();
    session.close();

    user.setId(id);

    return user;
  }

  public static User update(User user) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    session.update(user);
    session.getTransaction().commit();
    session.close();

    return user;
  }

  public static User findById(Integer id) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    User user = session.find(User.class, id);
    session.getTransaction().commit();
    session.close();

    return user;
  }

  public static List<User> findAll(){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    String sql = "SELECT * FROM users";
    List<User> users = session.createNativeQuery(sql, User.class).getResultList();
    session.getTransaction().commit();
    session.close();

    return users;
  }

  public static List<User> findByLoginAndPassword(String login, String password){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    String sql = "SELECT * FROM users WHERE login=? AND password=?";
    NativeQuery query = session.createNativeQuery(sql, User.class);
    query.setParameter(1, login);
    query.setParameter(2, password);
    List<User> users = query.getResultList();
    session.getTransaction().commit();
    session.close();

    return users;
  }

  public static void delete(User user){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    session.delete(user);
    session.getTransaction().commit();
    session.close();
  }
}
