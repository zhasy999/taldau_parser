package com.example.hello.Entity;

import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name="water_pipes_need_repair")
public class Water_pipes_need_repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region")
    private String region;

    @Column(name = "year")
    private Long year;

    @Column(name = "water_pipes")
    private Long water_pipes;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getWater_pipes() {
        return water_pipes;
    }

    public void setWater_pipes(Long water_pipes) {
        this.water_pipes = water_pipes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


