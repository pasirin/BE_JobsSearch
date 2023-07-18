package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.HiringOrganizationRepository;
import com.example.JobsSearch.service.IHiringOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiringOrganizationService implements IHiringOrganizationService {

    @Autowired
    HiringOrganizationRepository hiringOrganizationRepository;

    @Override
    public List<HiringOrganization> getAll() {
        return hiringOrganizationRepository.findAll();
    }

    @Override
    public ResponseObject getProfile(Long id) {
        if (hiringOrganizationRepository.findByUserId(id).isEmpty()) {
            return ResponseObject.message("There Aren't any HR with the provided Id");
        }
        return ResponseObject.ok().setData(hiringOrganizationRepository.findByUserId(id));
    }

    @Override
    public ResponseObject getById(Long id) {
        if (hiringOrganizationRepository.findById(id).isEmpty()) {
            return ResponseObject.message("There Aren't any HR with the provided Id");
        }
        return ResponseObject.ok().setData(hiringOrganizationRepository.findById(id));
    }

    @Override
    public List<HiringOrganization> getByName(String name) {
        return hiringOrganizationRepository.findByNameLike("%" + name + "%");
    }

    @Override
    public ResponseObject delete(Long id) {
        if (!hiringOrganizationRepository.existsById(id)) {
            return ResponseObject.message("There Aren't any HR with the provided Id");
        }
        hiringOrganizationRepository.deleteById(id);
        return ResponseObject.ok();
    }

}
