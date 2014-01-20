package com.demo.web.sample;

import java.util.Date;

import com.demo.web.entity.NewsEntry;
import com.demo.web.entity.User;
import com.demo.web.repository.NewsEntryRepository;
import com.demo.web.repository.UserRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class DatabaseInitializer {

    @Autowired(required = true)
    private NewsEntryRepository newsEntryRepository;

    @Autowired(required = true)
    private UserRepository userRepository;

    @Autowired(required = true)
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void generateData() {
        User userUser = new User("user", this.passwordEncoder.encode("user"));
        userUser.addRole("user");
        this.userRepository.save(userUser);

        User adminUser = new User("admin", this.passwordEncoder.encode("admin"));
        adminUser.addRole("user");
        adminUser.addRole("admin");
        this.userRepository.save(adminUser);

        long timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
        for (int i = 0; i < 10; i++) {
            NewsEntry newsEntry = new NewsEntry();
            newsEntry.setContent("This is example content " + i);
            newsEntry.setDate(new Date(timestamp));
            this.newsEntryRepository.save(newsEntry);
            timestamp += 1000 * 60 * 60;
        }
    }
}
