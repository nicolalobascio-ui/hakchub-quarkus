package com.hackhub.services;

import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import com.hackhub.entity.Hackathon;
import com.hackhub.repository.HackathonRepository;

@ApplicationScoped
@Transactional
public class HackathonService {

    private final HackathonRepository repository;

    public HackathonService(HackathonRepository repository) {
        this.repository = repository;
    }

    public List<Hackathon> listAll() {
        return repository.listAll();
    }

    public Hackathon findById(Long id) {
        return repository.findById(id);
    }

    public Hackathon save(Hackathon hackathon) {
         repository.persist(hackathon);
        return hackathon;
    }

}

