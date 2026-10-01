package com.hackhub.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;
import io.quarkus.elytron.security.common.BcryptUtil;

import com.hackhub.security.JwtUtils;
import com.hackhub.services.UserService;
import com.hackhub.dto.LoginRequest;
import com.hackhub.entity.User;


import java.util.HashMap;
import java.util.Map;


@Path("/api")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PermitAll 
    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest loginRequest) {

        User user = userService.findIdByUsername(loginRequest.getUsername());

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Utente non trovato").build();
        }

        if (!BcryptUtil.matches(loginRequest.getPassword(), user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Password errata").build();
        }

        // Genera il token
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());

        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", user.getUsername());
        body.put("role", user.getRole() == null ? "" : user.getRole());

        return Response.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .entity(body)
                .build();
    }
}
