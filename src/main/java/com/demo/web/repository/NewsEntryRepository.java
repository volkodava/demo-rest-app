package com.demo.web.repository;

import com.demo.web.entity.NewsEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NewsEntryRepository extends JpaRepository<NewsEntry, Long> {

}
