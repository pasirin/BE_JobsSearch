package com.example.JobsSearch.service;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.payload.Response.ResponseObject;

import java.util.List;

public interface IHiringOrganizationService {
    List<HiringOrganization> getAll();
    ResponseObject getProfile(Long id);
    ResponseObject getById(Long id);
    List<HiringOrganization> getByName(String name);
    ResponseObject delete(Long id);
}
