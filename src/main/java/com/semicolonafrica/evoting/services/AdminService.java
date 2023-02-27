package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.dto.request.AddCandidateRequest;
import com.semicolonafrica.evoting.dto.request.AddNonCandidateRequest;
import com.semicolonafrica.evoting.dto.request.ResultRequest;
import com.semicolonafrica.evoting.dto.response.AddCandidateResponse;
import com.semicolonafrica.evoting.dto.response.AddNonCandidateResponse;
import com.semicolonafrica.evoting.dto.response.ResultResponse;
import jakarta.mail.MessagingException;

public interface AdminService {
    AddCandidateResponse addCandidate(AddCandidateRequest addCandidateRequest) throws MessagingException;
    AddNonCandidateResponse addNonCandidate(AddNonCandidateRequest addNonCandidateRequest) throws MessagingException;

    ResultResponse displayResult(ResultRequest request);
}
