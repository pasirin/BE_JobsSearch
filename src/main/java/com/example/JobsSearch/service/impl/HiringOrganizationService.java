package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.User;
import com.example.JobsSearch.payload.Request.HrUpdateProfileRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.HiringOrganizationRepository;
import com.example.JobsSearch.repository.UserRepository;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.IHiringOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiringOrganizationService implements IHiringOrganizationService {

    @Autowired
    UserRepository userRepository;
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

    @Override
    public ResponseObject updateProfile(Authentication authentication, HrUpdateProfileRequest hrUpdateProfileRequest) {
        Long id = getHrIdFromContext(authentication);
        if (id == null) {
            return ResponseObject.message("There Aren't any HR_ID with this userId");
        }
        HiringOrganization hiringOrganization = hiringOrganizationRepository.findById(id).orElse(null);
        if (hiringOrganization == null) {
            return ResponseObject.message("There Aren't any HR with the provided Id");
        }

        hiringOrganization.setName(hrUpdateProfileRequest.getName());
        hiringOrganization.setPhone_number(hrUpdateProfileRequest.getPhone_number());
        hiringOrganization.setWebsite(hrUpdateProfileRequest.getWebsite());
        hiringOrganization.setAddress(hrUpdateProfileRequest.getAddress());
        hiringOrganization.setIntroduction(hrUpdateProfileRequest.getIntroduction());

        // lưu thay đổi organization vào DB
        hiringOrganizationRepository.save(hiringOrganization);
        return ResponseObject.ok();
    }

    public Long getHrIdFromContext(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        HiringOrganization hiringOrganization = hiringOrganizationRepository.findByUserId(userDetails.getId()).orElse(null);
        return hiringOrganization.getId();
    }
}
