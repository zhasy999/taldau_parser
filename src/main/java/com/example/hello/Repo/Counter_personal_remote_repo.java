package com.example.hello.Repo;


import com.example.hello.Entity.Counter_personal_remote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Counter_personal_remote_repo extends JpaRepository<Counter_personal_remote, Long> {}
