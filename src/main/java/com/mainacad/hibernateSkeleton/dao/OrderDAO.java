package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OrderDAO {
  public static Order save(Order order) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Integer id = (Integer) session.save(order);
    session.getTransaction().commit();
    session.close();

    order.setId(id);

    return order;
  }

  public static void delete(Order order){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    session.delete(order);
    session.getTransaction().commit();
    session.close();
  }
}
