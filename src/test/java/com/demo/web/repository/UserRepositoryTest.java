package com.demo.web.repository;

import com.demo.web.repository.UserRepository;
import com.demo.web.entity.User;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/context.xml" })
public class UserRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private UserRepository userRepository;

    @Test
    public void checkIfAnyUserExists() {

        List<User> users = userRepository.findAll();
        logger.info("Users: {}", users);

        assertThat(users, is(not(nullValue())));
        assertThat(users.size(), is(greaterThan(0)));
    }
}
