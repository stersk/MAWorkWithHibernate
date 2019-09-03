package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.Cart;
import com.mainacad.hibernateSkeleton.model.Item;
import com.mainacad.hibernateSkeleton.model.Order;
import com.mainacad.hibernateSkeleton.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public class CartDAO {
  public static Cart save(Cart cart) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Integer id = (Integer) session.save(cart);
    session.getTransaction().commit();
    session.close();

    cart.setId(id);

    return cart;
  }

  public static Cart update(Cart cart) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    session.update(cart);
    session.getTransaction().commit();
    session.close();

    return cart;
  }

  public static Cart findById(Integer id) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Cart cart = session.find(Cart.class, id);
    session.getTransaction().commit();
    session.close();

    return cart;
  }

  public static List<Cart> findByUser(User user) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Cart> criteriaQuery = builder.createQuery(Cart.class);
    Root<Cart> root = criteriaQuery.from(Cart.class);
    criteriaQuery.select(root).where(builder.equal(root.get("user"), user));

    Query<Cart> query = session.createQuery(criteriaQuery);
    List<Cart> carts = query.getResultList();

    session.getTransaction().commit();
    session.close();

    return carts;
  }

  public static Cart findOpenCartByUser(User user) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Cart> criteriaQuery = builder.createQuery(Cart.class);
    Root<Cart> root = criteriaQuery.from(Cart.class);

    criteriaQuery.select(root);

    Predicate cartByUser = builder.equal(root.get("user"), user);
    Predicate openCart = builder.equal(root.get("closed"), false);
    criteriaQuery.where(builder.and(cartByUser));
    criteriaQuery.where(builder.and(openCart));

    Query<Cart> query = session.createQuery(criteriaQuery);
    Cart cart = query.stream().findFirst().orElse(null);

    session.getTransaction().commit();
    session.close();

    return cart;
  }

  public static Integer getCartSum(Cart cart) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Number> criteriaQuery = builder.createQuery(Number.class);
    Root<Order> root = criteriaQuery.from(Order.class);
    Join<Order, Item> join = root.join("item", JoinType.LEFT);

    criteriaQuery.select(builder.sum(builder.prod(root.get("amount"), join.get("price"))).alias("sum"));

    Query<Number> query = session.createQuery(criteriaQuery);
    Number sum = query.stream()
                       .map(summary -> Optional.ofNullable(summary))
                       .findFirst()
                       .orElse(null).orElse(null);

    session.getTransaction().commit();
    session.close();

    return sum == null ? null : sum.intValue();
  }

  public static void delete(Cart cart){
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    session.delete(cart);
    session.getTransaction().commit();
    session.close();
  }
}
