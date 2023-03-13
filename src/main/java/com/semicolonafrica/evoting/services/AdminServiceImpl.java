package com.semicolonafrica.evoting.services;

import com.semicolonafrica.evoting.data.models.Admin;
import com.semicolonafrica.evoting.data.models.Candidate;
import com.semicolonafrica.evoting.data.models.NonCandidate;
import com.semicolonafrica.evoting.data.repository.AdminRepo;
import com.semicolonafrica.evoting.dto.request.AddCandidateRequest;
import com.semicolonafrica.evoting.dto.request.AddNonCandidateRequest;
import com.semicolonafrica.evoting.dto.request.AdminLoginRequest;
import com.semicolonafrica.evoting.dto.request.ResultRequest;
import com.semicolonafrica.evoting.dto.response.AddCandidateResponse;
import com.semicolonafrica.evoting.dto.response.AddNonCandidateResponse;
import com.semicolonafrica.evoting.dto.response.AdminLoginResponse;
import com.semicolonafrica.evoting.dto.response.ResultResponse;
import com.semicolonafrica.evoting.email.EmailSenderService;
import com.semicolonafrica.evoting.exceptions.UserExistsException;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private NonCandidateService nonCandidateService;
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private AdminRepo adminRepo;
    private final Admin admin = new Admin();
    @Override
    public AdminLoginResponse adminLogin(AdminLoginRequest adminLoginRequest) {
        admin.setName("admin");
        admin.setPassword(hashPassword("admin"));
        adminRepo.save(admin);
        Admin foundAdmin = adminRepo.findByName(adminLoginRequest.getName());
        AdminLoginResponse response = new AdminLoginResponse();
        if (confirmPassword(adminLoginRequest.getPassword(), foundAdmin.getPassword())){
            response.setStatus(HttpStatus.OK);
            response.setMessage("Login successful");
        }else throw new IllegalStateException("Invalid password");
        return response;
    }
    private static String hashPassword(String userPassword){
        String password = userPassword;
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static boolean confirmPassword(String candidate, String password){
        return BCrypt.checkpw(candidate, password);
    }
    @Override
    public AddCandidateResponse addCandidate(AddCandidateRequest addCandidateRequest) throws MessagingException {
        validateCandidate(addCandidateRequest);
        Candidate candidate = new Candidate();
        candidate.setFullName(addCandidateRequest.getFullName());
        String token = generateToken();
        candidate.setToken(token);
        candidate.setEmail(addCandidateRequest.getEmail());
        candidateService.addCandidate(candidate);
        emailSenderService.send(addCandidateRequest.getEmail(), buildEmail(addCandidateRequest.getEmail(), token));
        AddCandidateResponse addCandidateResponse = new AddCandidateResponse();
        addCandidateResponse.setId(candidate.getId());
        addCandidateResponse.setStatus(HttpStatus.CREATED);
        addCandidateResponse.setMessage("Candidate added successfully");
        return addCandidateResponse;
    }

    @Override
    public AddNonCandidateResponse addNonCandidate(AddNonCandidateRequest addNonCandidateRequest) throws MessagingException {
        validateNonCandidate(addNonCandidateRequest);
        NonCandidate nonCandidate = new NonCandidate();
        nonCandidate.setFullName(addNonCandidateRequest.getFullName());
        String token = generateToken();
        nonCandidate.setToken(token);
        nonCandidate.setEmail(addNonCandidateRequest.getEmail());
        nonCandidateService.addNonCandidate(nonCandidate);
        emailSenderService.send(addNonCandidateRequest.getEmail(), buildEmail(addNonCandidateRequest.getEmail(), token));
        AddNonCandidateResponse addNonCandidateResponse = new AddNonCandidateResponse();
        addNonCandidateResponse.setId(nonCandidate.getId());
        addNonCandidateResponse.setStatus(HttpStatus.CREATED);
        addNonCandidateResponse.setMessage("NonCandidate added successfully");
        return addNonCandidateResponse;

    }

    @Override
    public List<Candidate> displayResult() {
        return candidateService.findAllCandidates();
    }

//    private AddCandidateResponse getAddCandidateResponse() {
//        AddCandidateResponse addCandidateResponse = new AddCandidateResponse();
//        addCandidateResponse.setStatus(HttpStatus.CREATED);
//        addCandidateResponse.setMessage("Candidate added successfully");
//        return addCandidateResponse;
//    }
//    private AddNonCandidateResponse getAddNonCandidateResponse() {
//        AddNonCandidateResponse addNonCandidateResponse = new AddNonCandidateResponse();
//        addNonCandidateResponse.setStatus(HttpStatus.CREATED);
//        addNonCandidateResponse.setMessage("NonCandidate added successfully");
//        return addNonCandidateResponse;
//    }

    private void validateCandidate(AddCandidateRequest addCandidateRequest) {
        if (candidateService.candidateExists(addCandidateRequest.getEmail()))
            throw new UserExistsException("Candidate exists");
    }

    private void validateNonCandidate(AddNonCandidateRequest addNonCandidateRequest) {
        if (nonCandidateService.voterExists(addNonCandidateRequest.getEmail()))
            throw new UserExistsException("NonCandidate exists");
    }



    private String generateToken() {
        StringBuilder tok = new StringBuilder();
        SecureRandom number = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            int num = number.nextInt(9);
            tok.append(num);
        }
        StringBuilder token = new StringBuilder(tok.toString());
        return token.toString();
    }

    protected String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Your vote token</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please use the token below to vote: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">" +
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" +
                " <p>"+ link + " </p> </p></blockquote>. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
