package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.models.Candidate;
import com.semicolonafrica.evoting.dto.request.VoteRequest;
import com.semicolonafrica.evoting.dto.response.VoteResponse;

public interface CandidateService extends VoterService{
    void addCandidate(Candidate candidate);
    boolean candidateExists(String email);
    Candidate findCandidate(Long id);



}
