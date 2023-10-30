package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.HelpRequest;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.HelpRequestRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "HelpRequest")
@RequestMapping("/api/helprequest")
@RestController
@Slf4j
public class HelpRequestController extends ApiController {

    @Autowired
    HelpRequestRepository helprequestRepository;

    @Operation(summary= "List all help requests")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<HelpRequest> allHelpRequests() {
        log.info("Fetching all help requests");
        Iterable<HelpRequest> requests = helprequestRepository.findAll();
        return requests;
    }

    @Operation(summary= "Create a new help request")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public HelpRequest postHelpRequest(
            @Parameter(name="requesterEmail", description="Email of the requester", example="user@ucsb.edu") @RequestParam String requesterEmail,
            @Parameter(name="teamId", description="ID of the team", example="s22-5pm-3") @RequestParam String teamId,
            @Parameter(name="tableOrBreakoutRoom", description="Table or Breakout room number", example="7") @RequestParam String tableOrBreakoutRoom,
            @Parameter(name="requestTime", description="Time of the request in iso format; see https://en.wikipedia.org/wiki/ISO_8601", example="YYYY-mm-ddTHH:MM:SS") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime requestTime,
            @Parameter(name="explanation", description="Explanation for the help request", example="Need help with Swagger-ui") @RequestParam String explanation,
            @Parameter(name="solved", description="Is the request solved?", example="false") @RequestParam boolean solved) {
        
        log.info("Creating new help request by email: {}", requesterEmail);

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setRequesterEmail(requesterEmail);
        helpRequest.setTeamId(teamId);
        helpRequest.setTableOrBreakoutRoom(tableOrBreakoutRoom);
        helpRequest.setRequestTime(requestTime);
        helpRequest.setExplanation(explanation);
        helpRequest.setSolved(solved);

        HelpRequest savedHelpRequest = helprequestRepository.save(helpRequest);
        log.info("Help request saved with ID: {}", savedHelpRequest.getId());
        return savedHelpRequest;
    }
}
