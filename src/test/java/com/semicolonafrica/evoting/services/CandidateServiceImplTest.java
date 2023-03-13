package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.models.Candidate;
import com.semicolonafrica.evoting.data.repository.CandidateRepo;
import com.semicolonafrica.evoting.dto.request.AddCandidateRequest;
import com.semicolonafrica.evoting.dto.request.IncreaseCandidateVoteRequest;
import com.semicolonafrica.evoting.dto.request.VoteRequest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CandidateServiceImplTest {
    @Autowired
    CandidateService candidateService;
    @Autowired
    CandidateRepo candidateRepo;
    VoteRequest voteRequest;
    @Autowired
    AdminService adminService;
    AddCandidateRequest candidateRequest;


    IncreaseCandidateVoteRequest increaseCandidateVoteRequest;



    @BeforeEach
    void setUp() {
        voteRequest = new VoteRequest();
        voteRequest.setEmail("senatorK@gmail.com");
        voteRequest.setToken("6873");

        increaseCandidateVoteRequest = new IncreaseCandidateVoteRequest();
    }

    @Test
    void candidateExists() {
        assertTrue(candidateService.candidateExists("senatory@gmail.com"));
    }

    @Test
    void vote() {
        var votedCan =   candidateService.vote(voteRequest);
        assertEquals(HttpStatus.OK,votedCan.getStatus());
    }

    @Test
    void findAllCandidates() {
        assertEquals(2,candidateService.findAllCandidates().size());
    }

    @Test
    void findCandidateById() {
        assertEquals(1,candidateService.findCandidateById(1L).getId());
    }
    @Test
    void addVote() throws MessagingException {

        candidateRequest = new AddCandidateRequest();
        candidateRequest.setEmail("Kapi@gmail.com");
        candidateRequest.setFullName("Kapi Egusi");
        var candidateResponse = adminService.addCandidate(candidateRequest);



        var candidate = candidateRepo.findById(candidateResponse.getId());

        assertEquals(0,candidate.get().getNoOfVotes());
        IncreaseCandidateVoteRequest increaseCandidateVoteRequest1 = new IncreaseCandidateVoteRequest();
        increaseCandidateVoteRequest1.setCandidateId(candidate.get().getId());

        var numberOfVote =   candidateService.addVote(increaseCandidateVoteRequest1);
        assertEquals(1,numberOfVote);
    }
}