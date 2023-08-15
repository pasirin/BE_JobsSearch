package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.News;
import com.example.JobsSearch.payload.Request.NewsRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.NewsRepository;
import com.example.JobsSearch.service.ServiceCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsService implements ServiceCRUD<NewsRequest, News> {
  @Autowired private NewsRepository newsRepository;

  @Override
  public ResponseObject create(NewsRequest newsRequest) {
    News news =
        new News(
            newsRequest.getOpensAt(),
            newsRequest.getExpiresAt(),
            newsRequest.getTitle(),
            newsRequest.getSubTitle(),
            newsRequest.getCategory(),
            newsRequest.getBody(),
            newsRequest.getEventPageUrl(),
            newsRequest.getEventStartAt(),
            newsRequest.getEventEndAt());
    newsRepository.save(news);
    return ResponseObject.ok();
  }

  @Override
  public ResponseObject update(Long id, NewsRequest newsRequest) {
    if (newsRepository.findById(id).isEmpty()) {
      return ResponseObject.message("The Requested News Id does not exist");
    }
    News news = newsRepository.findById(id).get();
    news.setOpensAt(newsRequest.getOpensAt());
    news.setExpiresAt(newsRequest.getExpiresAt());
    news.setTitle(newsRequest.getTitle());
    news.setSubTitle(newsRequest.getSubTitle());
    news.setCategory(newsRequest.getCategory());
    news.setBody(newsRequest.getBody());
    news.setEventPageUrl(newsRequest.getEventPageUrl());
    news.setEventStartAt(newsRequest.getEventStartAt());
    news.setEventEndAt(newsRequest.getEventEndAt());
    newsRepository.save(news);
    return ResponseObject.ok();
  }

  @Override
  public ResponseObject delete(Long id) {
    if (!newsRepository.existsById(id)) {
      return ResponseObject.message("The Requested News Id does not exist");
    }
    newsRepository.deleteById(id);
    return ResponseObject.ok();
  }

  @Override
  public Collection<News> getAll() {
    List<News> newsCollection = newsRepository.findByOrderByUpdatedAtDesc();
    newsCollection.sort(Comparator.comparing(News::getUpdatedAt));
    return newsCollection;
  }

  @Override
  public Optional<News> getById(Long id) {
    return newsRepository.findById(id);
  }
}
