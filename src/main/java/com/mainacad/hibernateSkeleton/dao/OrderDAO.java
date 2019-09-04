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

  public static Order update(Order order) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    session.update(order);
    session.getTransaction().commit();
    session.close();

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

  public static Order findById(Integer id) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Order order = session.find(Order.class, id);
    session.getTransaction().commit();
    session.close();

    return order;
  }

  public static List<Order> findByCart(Cart cart) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
    Root<Order> root = criteriaQuery.from(Order.class);
    criteriaQuery.select(root).where(builder.equal(root.get("cart"), cart));

    Query<Order> query = session.createQuery(criteriaQuery);
    List<Order> orders = query.getResultList();

    session.getTransaction().commit();
    session.close();

    return orders;
  }

  public static List<Order> findClosedOrdersByUserAndPeriod(User user, Long from, Long to) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
    Root<Order> root = criteriaQuery.from(Order.class);
    Join<Order, Cart> join = root.join("cart", JoinType.LEFT);

    criteriaQuery.select(root);

    Predicate cartByUser = builder.equal(join.get("user"), user);
    Predicate cartByCreationTimeBetween = builder.between(join.get("creationTime"), from, to);
    Predicate closedCarts = builder.equal(join.get("closed"), true);

    Predicate finalWherePredicate = builder.and(cartByUser, cartByCreationTimeBetween);
    finalWherePredicate = builder.and(finalWherePredicate, closedCarts);

    criteriaQuery.where(finalWherePredicate);

    criteriaQuery.orderBy(builder.asc(join.get("creationTime")));

    Query<Order> query = session.createQuery(criteriaQuery);
    List<Order> orders = query.getResultList();

    session.getTransaction().commit();
    session.close();

    return orders;
  }

  public static Integer getSumOfAllOrdersByUserIdAndPeriod(User user, Long from, Long to) {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Number> criteriaQuery = builder.createQuery(Number.class);

    Root<Order> root = criteriaQuery.from(Order.class);
    Join<Order, Cart> cartJoin = root.join("cart", JoinType.LEFT);
    Join<Order, Item> itemJoin = root.join("item", JoinType.LEFT);

    criteriaQuery.select(builder.sum(builder.prod(root.get("amount"), itemJoin.get("price"))).alias("sum"));

    Predicate cartByUser = builder.equal(cartJoin.get("user"), user);
    Predicate cartByCreationTimeBetween = builder.between(cartJoin.get("creationTime"), from, to);
    Predicate closedCarts = builder.equal(cartJoin.get("closed"), true);

    Predicate finalWherePredicate = builder.and(cartByUser, cartByCreationTimeBetween);
    finalWherePredicate = builder.and(finalWherePredicate, closedCarts);

    criteriaQuery.where(finalWherePredicate);

    Query<Number> query = session.createQuery(criteriaQuery);
    Number sum = query.stream()
            .map(summary -> Optional.ofNullable(summary))
            .findFirst()
            .orElse(null).orElse(0);

    session.getTransaction().commit();
    session.close();

    return sum.intValue();
  }
}
