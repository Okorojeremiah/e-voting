package com.semicolonafrica.evoting.services;


import com.semicolonafrica.evoting.data.models.Candidate;
import com.semicolonafrica.evoting.data.models.NonCandidate;
import com.semicolonafrica.evoting.data.repository.NonCandidateRepo;
import com.semicolonafrica.evoting.dto.request.IncreaseCandidateVoteRequest;
import com.semicolonafrica.evoting.dto.request.VoteRequest;
import com.semicolonafrica.evoting.dto.response.VoteResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NonCandidateServiceImpl implements NonCandidateService{
    @Autowired
    private NonCandidateRepo nonCandidateRepo;

    @Autowired
    private CandidateService candidateService;


    @Override
    public void addNonCandidate(NonCandidate nonCandidate) {
        nonCandidateRepo.save(nonCandidate);
    }

    @Override
    public boolean voterExists(String email) {
        return nonCandidateRepo.findByEmail(email).isPresent();
    }
    @Override
    public VoteResponse vote(VoteRequest voteRequest) {
        NonCandidate nonCandidate = nonCandidateRepo.findByEmail(voteRequest.getEmail()).orElseThrow(()-> new RuntimeException("nonCandidate does not exist"));
        VoteResponse voteResponse = new VoteResponse();
        if (nonCandidate.getToken().equals(voteRequest.getToken())){
            nonCandidate.setToken("***voted***");
            nonCandidateRepo.save(nonCandidate);
            voteResponse.setId(nonCandidate.getId());
            voteResponse.setMessage("Token confirmed");
            voteResponse.setStatus(HttpStatus.OK);
        }
        else throw new RuntimeException("Invalid token");
        return voteResponse;
    }

//    private static String hashToken(String token){
//        return BCrypt.hashpw(token, BCrypt.gensalt());
//    }

    @Override
    public Long addVote(IncreaseCandidateVoteRequest increaseCandidateVoteRequest){
        Optional<Candidate> candidate = Optional.ofNullable(candidateService.findCandidateById(increaseCandidateVoteRequest
                .getCandidateId()));

        long num = candidate.get().getNoOfVotes() + 1;
        candidate.get().setNoOfVotes(num);
        candidateService.addCandidate(candidate.get());
//        String hashToken = hashToken(nonCandidate.getToken());
//        nonCandidate.setToken(hashToken);
//        addNonCandidate(nonCandidate);
        return candidate.get().getNoOfVotes();

    }
}




