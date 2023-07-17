package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

}
