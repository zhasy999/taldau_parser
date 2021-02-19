package com.example.hello.Repo;

import com.example.hello.Entity.Counter_house;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Counter_house_repo extends JpaRepository<Counter_house, Long> {}
