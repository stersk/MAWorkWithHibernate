package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private static List<User> users = new ArrayList<>();
    private static String TEST_USER_LOGIN = "test_user";

    @BeforeEach
    void setUp() {
        User user = new User(TEST_USER_LOGIN, "test_pass", "test_name", "test_surname");

        SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
        Session session = sessionFactory.openSession();

        session.getTransaction().begin();
        Integer id = (Integer) session.save(user);
        session.getTransaction().commit();
        session.close();

        user.setId(id);
        users.add(user);
    }

    @AfterEach
    void tearDown() {
        SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        for (User user: users) {
            if (user.getId() != null) {
                session.delete(user);
            }
        }

        session.getTransaction().commit();
        session.close();

        users.clear();
    }

    @Test
    void testSaveAndFindOneAndDelete() {
        User user = new User("ignatenko2207", "123456", "Alex", "Ignatenko");

        User savedUser = UserDAO.save(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());

        User dbUser = UserDAO.findById(savedUser.getId());
        assertNotNull(dbUser);

        UserDAO.delete(savedUser);

        dbUser = UserDAO.findById(savedUser.getId());
        assertNull(dbUser);
    }

    @Test
    void testSave() {
        User user = new User("ignatenko2207", "123456", "Alex", "Ignatenko");
        users.add(user);

        UserDAO.save(user);
        assertNotNull(user.getId());

        User checkedItem = UserDAO.findById(user.getId());
        assertNotNull(checkedItem);
        assertEquals(user.getId(), checkedItem.getId());
    }

    @Test
    void testUpdate() {
        User checkedUser = users.get(0);
        checkedUser.setPassword("new_password");
        checkedUser = UserDAO.update(checkedUser);

        User checkedUserFromDB = UserDAO.findById(checkedUser.getId());
        assertNotNull(checkedUserFromDB);
        assertEquals("new_password", checkedUserFromDB.getPassword());
    }

    @Test
    void testFindById() {
        User checkedUser = UserDAO.findById(users.get(0).getId());
        assertNotNull(checkedUser);
        assertNotNull(checkedUser.getId());
        assertEquals(users.get(0).getId(), checkedUser.getId(), "Wrong user found");
    }

    @Test
    void testFindByLoginAndPassword() {
        List<User> users = UserDAO.findByLoginAndPassword(TEST_USER_LOGIN, "test_pass");
        assertFalse(users.isEmpty());

        users = UserDAO.findByLoginAndPassword(TEST_USER_LOGIN, "wrong_test_pass");
        assertTrue(users.isEmpty());
    }

    @Test
    void testFindAll() {
        List<User> checkedUsers = UserDAO.findAll();

        assertNotNull(checkedUsers);
        assertFalse(checkedUsers.isEmpty());
    }

    @Test
    void testDelete() {
        UserDAO.delete(users.get(0));

        User checkedItem = UserDAO.findById(users.get(0).getId());
        assertNull(checkedItem);

        users.remove(0);
    }
}