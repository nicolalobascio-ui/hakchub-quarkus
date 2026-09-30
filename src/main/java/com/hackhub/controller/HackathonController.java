package com.hackhub.controller;

import com.hackhub.entity.Hackathon;
import com.hackhub.services.HackathonService;
import com.hackhub.services.HackathonSubscriptionService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/api/hackathons")
public class HackathonController {

    private final HackathonService hackathonService;
    private final HackathonSubscriptionService subscriptionService;
    @Context 
    private SecurityContext securityContext;

    public HackathonController(HackathonService hackathonService,
                               HackathonSubscriptionService subscriptionService) {
        this.hackathonService = hackathonService;
        this.subscriptionService = subscriptionService;
    }

    // GET /api/hackathons/me/subscription - Hackathon a cui sono iscritto (se presente)
    @GET
    @Path("/me/subscription")
    public Response getMySubscription() {
        return subscriptionService.getMySubscribedHackathon(securityContext.getUserPrincipal().getName())
        .map(h -> Response.ok(h).build())
        .orElseGet(() -> Response.noContent().build());
    }

    // POST /api/hackathons/{id}/subscription - Toggle iscrizione/disiscrizione
    @POST
    @Path("/{id}/subscription")
    public Response toggleSubscription(@PathParam("id") Long id) {
        HackathonSubscriptionService.ToggleResult result =
                subscriptionService.toggleSubscription(securityContext.getUserPrincipal().getName(), id);

       return switch (result.status()) {
        case SUBSCRIBED -> Response.ok(result.hackathon()).build();
        case UNSUBSCRIBED -> Response.noContent().build();
        case CONFLICT_ALREADY_SUBSCRIBED_TO_ANOTHER ->
            Response.status(409).entity("Sei già iscritto ad un altro hackathon").build();
        };
    }

    // GET /hackathons - Lista tutti gli hackathon
    @GET
    public List<Hackathon> getAllHackathons() {
        return hackathonService.listAll();
    }

    // GET /hackathons/{id} - Dettaglio hackathon
    @GET
    @Path("/{id}")
    public Response getHackathonById(@PathParam("id") Long id) {
        Hackathon hackathon = hackathonService.findById(id);
        if (hackathon != null) {
            return Response.ok(hackathon).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
