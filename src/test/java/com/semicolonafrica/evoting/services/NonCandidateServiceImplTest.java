package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.repository.CandidateRepo;
import com.semicolonafrica.evoting.data.repository.NonCandidateRepo;
import com.semicolonafrica.evoting.dto.request.AddCandidateRequest;
import com.semicolonafrica.evoting.dto.request.AddNonCandidateRequest;
import com.semicolonafrica.evoting.dto.request.IncreaseCandidateVoteRequest;
import com.semicolonafrica.evoting.dto.request.VoteRequest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class NonCandidateServiceImplTest {

    @Autowired
    NonCandidateService nonCandidateService;

    @Autowired
    NonCandidateRepo nonCandidateRepo;
    VoteRequest voteRequest;

    AddCandidateRequest candidateRequest;
    @Autowired
    CandidateRepo candidateRepo;

    AddNonCandidateRequest nonCandidateRequest;
    @Autowired
    AdminService adminService;

    @BeforeEach
    void setUp() {
        voteRequest = new VoteRequest();
        voteRequest.setEmail("DerekAkeem@yahoo.com");
        voteRequest.setToken("6358");
    }


    @Test
    void voterExists() {
        assertTrue(nonCandidateService.voterExists("DerekAkeem@yahoo.com"));
    }

    @Test
    void vote() {
        var votedNonCan =    nonCandidateService.vote(voteRequest);
        assertEquals(HttpStatus.OK,votedNonCan.getStatus());
    }

    @Test
    void addVote() throws MessagingException {

        nonCandidateRequest = new AddNonCandidateRequest();
        nonCandidateRequest.setEmail("Derek@gmail.com");
        nonCandidateRequest.setFullName("Derek");

        var nonCandidateResponse =    adminService.addNonCandidate(nonCandidateRequest);



        var candidate = candidateRepo.findByEmail("Kapi@gmail.com");

        assertEquals(1,candidate.get().getNoOfVotes());
        IncreaseCandidateVoteRequest increaseCandidateVoteRequest1 = new IncreaseCandidateVoteRequest();
        increaseCandidateVoteRequest1.setCandidateId(candidate.get().getId());

        var numberOfVotes =  nonCandidateService.addVote(increaseCandidateVoteRequest1);
        assertEquals(2,numberOfVotes);
    }
}