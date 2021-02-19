package com.example.hello.Repo;


import com.example.hello.Entity.Water_pipes_net;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Water_pipes_net_repo extends JpaRepository<Water_pipes_net, Long> {}
