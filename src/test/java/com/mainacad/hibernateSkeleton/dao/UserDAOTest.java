package com.mainacad.hibernateSkeleton.dao;

import com.mainacad.hibernateSkeleton.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

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
    void testFindByLoginAndPassword() {
        User user = new User("testLogin", "123456", "Alex", "Ignatenko");

        User savedUser = UserDAO.save(user);

        List<User> users = UserDAO.findByLoginAndPassword("testLogin", "123456");
        assertFalse(users.isEmpty());

        assertEquals(savedUser.getFirstName(), users.get(0).getFirstName());

        UserDAO.delete(savedUser);
    }
}