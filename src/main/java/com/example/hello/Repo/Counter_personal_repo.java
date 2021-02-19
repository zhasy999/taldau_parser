package com.example.hello.Repo;

import com.example.hello.Entity.Counter_personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Counter_personal_repo extends JpaRepository<Counter_personal, Long> {}
