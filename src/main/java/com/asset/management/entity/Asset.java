package com.asset.management.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "AIM_assetsrelatedstatement")
public class Asset {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(name = "Datetime")
//    private Date dateTime;

    @Column(name = "Jobnumber")
    private int jobNumber;

    @Column(name = "Devicename")
    private String deviceName;

    @Column(name = "Assetnumber")
    private String assetNumber;

    @Column(name = "status")
    private String status;
}
