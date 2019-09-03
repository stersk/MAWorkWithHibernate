package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.Cart;
import com.mainacad.hibernateSkeleton.model.Item;
import com.mainacad.hibernateSkeleton.model.Order;
import com.mainacad.hibernateSkeleton.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartDAOTest {
  private static List<Cart> carts = new ArrayList<>();
  private static List<User> users = new ArrayList<>();
  private static List<Item> items = new ArrayList<>();
  private static List<Order> orders = new ArrayList<>();
  private static final Long NEW_CREATION_TIME = 1565024869119L;

  @BeforeEach
  void setUp() {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();

    User user = new User("test_user_login", "test_pass", "test_name", "test_surname");
    Integer id = (Integer) session.save(user);
    user.setId(id);
    users.add(user);

    Item item = new Item("testCode", "Kellys Spider 40 (2014)", 1400000);
    id = (Integer) session.save(item);
    item.setId(id);
    items.add(item);

    Cart cart = new Cart(1565024867119L, false, user);
    id = (Integer) session.save(cart);
    cart.setId(id);
    carts.add(cart);

    session.getTransaction().commit();
    session.close();
  }

  @AfterEach
  void tearDown() {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();

    for (Order order: orders) {
      if (order.getId() != null) {
        session.delete(order);
      }
    }
    orders.clear();

    for (Cart cart: carts) {
      if (cart.getId() != null) {
        session.delete(cart);
      }
    }
    carts.clear();

    for (Item item: items) {
      if (item.getId() != null) {
        session.delete(item);
      }
    }
    items.clear();

    for (User user: users) {
      if (user.getId() != null) {
        session.delete(user);
      }
    }
    users.clear();

    session.getTransaction().commit();
    session.close();
  }

  @Test
  void testSave() {
    Cart checkedCart = new Cart(1565024867119L, false, users.get(0));
    CartDAO.save(checkedCart);
    carts.add(checkedCart);

    assertNotNull(checkedCart);
    assertNotNull(checkedCart.getId());

    Integer chechedCartId = checkedCart.getId();
    checkedCart = CartDAO.findById(chechedCartId);
    assertNotNull(checkedCart);
    assertEquals(chechedCartId, checkedCart.getId());
  }

  @Test
  void testUpdate() {
    Cart checkedCart = carts.get(0);
    assertNotEquals(checkedCart.getCreationTime(), NEW_CREATION_TIME);

    checkedCart.setCreationTime(NEW_CREATION_TIME);
    CartDAO.update(checkedCart);

    checkedCart = CartDAO.findById(checkedCart.getId());
    assertNotNull(checkedCart);
    assertEquals(checkedCart.getCreationTime(), NEW_CREATION_TIME);
  }

  @Test
  void testFindById() {
    Cart checkedCart = CartDAO.findById(carts.get(0).getId());
    assertNotNull(checkedCart);
    assertEquals(carts.get(0).getId(), checkedCart.getId());
  }

  @Test
  void testDelete() {
    CartDAO.delete(carts.get(0));

    Cart checkedCart = CartDAO.findById(carts.get(0).getId());
    assertNull(checkedCart);

    carts.remove(0);
  }

  @Test
  void testFindByUser() {
    List<Cart> checkedCarts = CartDAO.findByUser(users.get(0));
    assertNotNull(checkedCarts);
    assertFalse(checkedCarts.isEmpty());

    CartDAO.delete(carts.get(0));
    carts.clear();

    checkedCarts = CartDAO.findByUser(users.get(0));
    assertNotNull(checkedCarts);
    assertTrue(checkedCarts.isEmpty());
  }

  @Test
  void testFindOpenCartByUser() {
    Cart checkedCart = CartDAO.findOpenCartByUser(users.get(0));
    assertNotNull(checkedCart);

    checkedCart.setClosed(true);
    CartDAO.update(checkedCart);

    checkedCart = CartDAO.findOpenCartByUser(users.get(0));
    assertNull(checkedCart);
  }

  @Test
  void testGetCartSum() {
    Order firstOrder = new Order(items.get(0), 1, carts.get(0));
    firstOrder = OrderDAO.save(firstOrder);
    orders.add(firstOrder);

    Order secondOrder = new Order(items.get(0), 2, carts.get(0));
    secondOrder = OrderDAO.save(secondOrder);
    orders.add(secondOrder);

    Integer checkedSum = CartDAO.getCartSum(carts.get(0));
    assertNotNull(checkedSum);
    assertEquals(4200000, checkedSum);

    OrderDAO.delete(firstOrder);
    OrderDAO.delete(secondOrder);

    checkedSum = CartDAO.getCartSum(carts.get(0));
    assertNull(checkedSum);

    orders.clear();
  }
}