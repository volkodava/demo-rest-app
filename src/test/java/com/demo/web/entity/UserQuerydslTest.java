package com.demo.web.entity;

import static com.mysema.query.collections.CollQueryFactory.from;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests to show the usage of Querydsl predicates to filter collections.
 *
 */
public class UserQuerydslTest {

    private static final QUser $ = QUser.user;

    private User user1, user2, user3, admin;

    List<User> users;

    @Before
    public void setUp() {

        user1 = new User("user1", "111");
        user2 = new User("user2", "222");
        user3 = new User("user3", "333");
        admin = new User("admin", "444");

        users = Arrays.asList(user1, user2, user3, admin);
    }

    @Test
    public void findsAllUsers() {

        List<User> result = from($, users).where($.name.contains("user")).list($);

        assertThat(result, hasSize(3));
        assertThat(result, hasItems(user1, user2, user3));
    }

    @Test
    public void findsAllUserNames() {

        List<String> result = from($, users).where($.name.contains("user")).list($.name);

        assertThat(result, hasSize(3));
        assertThat(result, hasItems(user1.getName(), user2.getName(), user3.getName()));
    }

    @Test
    public void findsAdmins() {

        List<User> result = from($, users).where($.name.contains("admin")).list($);

        assertThat(result, hasSize(1));
        assertThat(result, hasItems(admin));
    }
}
