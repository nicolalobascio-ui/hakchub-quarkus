package com.hackhub.repository;

import com.hackhub.entity.Hackathon;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class HackathonRepository implements PanacheRepository<Hackathon> {
    
}
