package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.models.NonCandidate;
import com.semicolonafrica.evoting.dto.request.IncreaseCandidateVoteRequest;

public interface NonCandidateService extends VoterService{
    void addNonCandidate(NonCandidate nonCandidate);
    boolean voterExists(String email);
    Long addVote(IncreaseCandidateVoteRequest increaseCandidateVoteRequest);

}
