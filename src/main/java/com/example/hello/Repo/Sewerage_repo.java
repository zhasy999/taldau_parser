package com.example.hello.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Sewerage_repo extends JpaRepository<com.example.hello.Entity.Sewerage, Long> {}
