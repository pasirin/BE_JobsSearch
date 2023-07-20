package com.example.JobsSearch.service;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.payload.Request.HrUpdateProfileRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;

public interface IHiringOrganizationService {
    Collection<HiringOrganization> getAll();
    ResponseObject getProfile(Long id);

    ResponseObject getById(Long id);

    List<HiringOrganization> getByName(String name);

    ResponseObject delete(Long id);

    ResponseObject updateProfile(Authentication authentication, HrUpdateProfileRequest hrUpdateProfileRequest);
}
