package com.example.hello.Entity;

import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name="sewerage_need_repair")
public class Sewerage_need_repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region")
    private String region;

    @Column(name = "year")
    private Long year;

    @Column(name = "sewerage")
    private Long sewerage;

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

    public Long getSewerage() {
        return sewerage;
    }

    public void setSewerage(Long sewerage) {
        this.sewerage = sewerage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


