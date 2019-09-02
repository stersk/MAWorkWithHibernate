package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class ItemDAO {
  public static Item save(Item item) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Integer id = (Integer) session.save(item);
    session.getTransaction().commit();
    session.close();

    item.setId(id);

    return item;
  }

  public static Item update(Item item) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    session.update(item);
    session.getTransaction().commit();
    session.close();

    return item;
  }

  public static Item findById(Integer id) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Item item = session.find(Item.class, id);
    session.getTransaction().commit();
    session.close();

    return item;
  }

  public static List<Item> findAll(){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    String sql = "SELECT * FROM items";
    List<Item> items = session.createNativeQuery(sql, Item.class).getResultList();
    session.getTransaction().commit();
    session.close();

    return items;
  }

  public static List<Item> findByItemCode(String code) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    String sql = "SELECT * FROM items WHERE items.item_code = ?";

    NativeQuery nativeQuery = session.createNativeQuery(sql, Item.class);
    nativeQuery.setParameter(1, code);

    List<Item> items = nativeQuery.getResultList();
    session.getTransaction().commit();
    session.close();

    return items;
  }

  public static void delete(Item item){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    session.delete(item);
    session.getTransaction().commit();
    session.close();
  }
}
