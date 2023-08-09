package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.util.SearchLabel;
import com.example.JobsSearch.payload.Request.SearchLabelRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SearchLabelRepository;
import com.example.JobsSearch.service.ServiceCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SearchLabelService implements ServiceCRUD<SearchLabelRequest, SearchLabel> {
  @Autowired private SearchLabelRepository searchLabelRepository;

  @Override
  public ResponseObject create(SearchLabelRequest searchLabelRequest) {
    if (searchLabelRepository.existsByName(searchLabelRequest.getName())) {
      return ResponseObject.message("There's already a label with that name");
    }
    SearchLabel searchLabel =
        new SearchLabel(searchLabelRequest.getName(), searchLabelRequest.getIs_enable());
    searchLabelRepository.save(searchLabel);
    return ResponseObject.ok();
  }

  @Override
  public ResponseObject update(Long id, SearchLabelRequest searchLabelRequest) {
    if (searchLabelRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any search label with the requested Id");
    }
    SearchLabel searchLabel = searchLabelRepository.findById(id).get();
    searchLabel.setName(searchLabelRequest.getName());
    searchLabel.setIs_enabled(searchLabelRequest.getIs_enable());
    searchLabelRepository.save(searchLabel);
    return ResponseObject.ok();
  }

  @Override
  public ResponseObject delete(Long id) {
    if(searchLabelRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any search label with the requested Id");
    }
    searchLabelRepository.deleteById(id);
    return ResponseObject.ok();
  }

  @Override
  public Collection<SearchLabel> getAll() {
    return searchLabelRepository.findAll();
  }

  @Override
  public Optional<SearchLabel> getById(Long id) {
    return searchLabelRepository.findById(id);
  }
}
