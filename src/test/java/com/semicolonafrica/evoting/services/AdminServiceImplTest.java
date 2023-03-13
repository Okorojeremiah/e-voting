package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.models.Admin;
import com.semicolonafrica.evoting.dto.request.AddCandidateRequest;
import com.semicolonafrica.evoting.dto.request.AddNonCandidateRequest;
import com.semicolonafrica.evoting.dto.request.AdminLoginRequest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    AdminService adminService;

    AdminLoginRequest admin;

    AddCandidateRequest candidateRequest;

    AddNonCandidateRequest nonCandidateRequest;

    @BeforeEach
    void setUp(){
        admin = new AdminLoginRequest();
        admin.setName("admin");
        admin.setPassword("admin");

        candidateRequest = new AddCandidateRequest();
        candidateRequest.setEmail("senatory@gmail.com");
        candidateRequest.setFullName("SENATOR K");

        nonCandidateRequest = new AddNonCandidateRequest();
        nonCandidateRequest.setFullName("HABIB");
        nonCandidateRequest.setEmail("habibAkeem@yahoo.com");



    }

    @Test
    void adminLogin() {
        var regAdmin =   adminService.adminLogin(admin);
        assertEquals(HttpStatus.OK ,regAdmin.getStatus());

    }

    @Test
    void addCandidate() throws MessagingException {
        var addCan =  adminService.addCandidate(candidateRequest);
        assertEquals(HttpStatus.CREATED,addCan.getStatus());
    }

    @Test
    void addNonCandidate() throws MessagingException {
        var nonCan = adminService.addNonCandidate(nonCandidateRequest);
        assertEquals(HttpStatus.CREATED,nonCan.getStatus());

    }

    @Test
    void displayResult() {
        assertEquals(2,adminService.displayResult().size());
    }
}