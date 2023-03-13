package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.models.Candidate;
import com.semicolonafrica.evoting.dto.request.IncreaseCandidateVoteRequest;
import com.semicolonafrica.evoting.dto.request.VoteRequest;
import com.semicolonafrica.evoting.dto.response.VoteResponse;

import java.util.List;

public interface CandidateService extends VoterService{
    void addCandidate(Candidate candidate);
    boolean candidateExists(String email);
    Candidate findCandidate(String email);

    List<Candidate> findAllCandidates();
    Candidate findCandidateById(Long id);
    Long addVote(IncreaseCandidateVoteRequest increaseCandidateVoteRequest);

}
