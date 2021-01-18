package com.asset.management.entity;

import lombok.Data;

import javax.persistence.Column;

@Data
public class Asset2 {
    @Column(name = "Jobnumber")
    private int jobNumber;

    @Column(name = "Assetnumber")
    private String assetNumber;
}
