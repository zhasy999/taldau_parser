package com.example.hello.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Water_pipes_need_repair_repo extends JpaRepository<com.example.hello.Entity.Water_pipes_need_repair, Long> {}
