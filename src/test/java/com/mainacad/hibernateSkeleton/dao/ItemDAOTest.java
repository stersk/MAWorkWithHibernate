package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {
  private static List<Item> items = new ArrayList<>();
  private static String TEST_ITEM_CODE = "qwerty12345";
  private static Integer TEST_NEW_PRICE = 1450000;

  @BeforeEach
  void setUp() {
    Item item = new Item(TEST_ITEM_CODE, "Kellys Spider 40 (2014)", 1400000);

    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();

    session.getTransaction().begin();
    Integer id = (Integer) session.save(item);
    session.getTransaction().commit();
    session.close();

    item.setId(id);
    items.add(item);
  }

  @AfterEach
  void tearDown() {
    SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();

    for (Item item: items) {
      if (item.getId() != null) {
        session.delete(item);
      }
    }

    session.getTransaction().commit();
    session.close();

    items.clear();
  }

  @Test
  void testSave() {
    Item item = new Item(TEST_ITEM_CODE + "_", "Kellys Spider 50 (2015)", 1500000);
    items.add(item);

    ItemDAO.save(item);
    assertNotNull(item.getId());

    Item checkedItem = ItemDAO.findById(item.getId());
    assertNotNull(checkedItem);
    assertEquals(item.getId(), checkedItem.getId());
  }

  @Test
  void testUpdate() {
    items.get(0).setPrice(TEST_NEW_PRICE);
    ItemDAO.update(items.get(0));

    Item checkedItem = ItemDAO.findById(items.get(0).getId());
    assertNotNull(checkedItem);
    assertEquals(TEST_NEW_PRICE, checkedItem.getPrice());
  }

  @Test
  void testFindById() {
    Item checkedItem = ItemDAO.findById(items.get(0).getId());
    assertNotNull(checkedItem);
    assertEquals(items.get(0).getId(), checkedItem.getId());
  }

  @Test
  void testFindAll() {
    List<Item> checkedList = ItemDAO.findAll();
    assertNotNull(checkedList);
    assertFalse(checkedList.isEmpty());
  }

  @Test
  void testFindByItemCode() {
    List<Item> checkedList = ItemDAO.findByItemCode(TEST_ITEM_CODE);

    assertNotNull(checkedList);
    assertFalse(checkedList.isEmpty());

    checkedList = checkedList.stream().filter(item -> !item.getItemCode().equals(TEST_ITEM_CODE)).collect(Collectors.toList());
    assertTrue(checkedList.isEmpty());
  }

  @Test
  void testDelete() {
    ItemDAO.delete(items.get(0));

    Item checkedItem = ItemDAO.findById(items.get(0).getId());
    assertNull(checkedItem);

    items.remove(0);
  }
}