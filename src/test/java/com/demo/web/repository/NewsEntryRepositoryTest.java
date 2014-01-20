package com.demo.web.repository;

import com.demo.web.repository.NewsEntryRepository;
import com.demo.web.entity.NewsEntry;
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
public class NewsEntryRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private NewsEntryRepository newsEntryRepository;

    @Test
    public void checkIfAnyNewsEntryExists() {

        List<NewsEntry> news = newsEntryRepository.findAll();
        logger.info("News: {}", news);

        assertThat(news, is(not(nullValue())));
        assertThat(news.size(), is(greaterThan(0)));
    }
}
