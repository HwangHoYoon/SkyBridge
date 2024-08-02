package com.skybridge.university.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "university")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "eng_name", length = Integer.MAX_VALUE)
    private String engName;

    @Column(name = "main_branch_type", length = Integer.MAX_VALUE)
    private String mainBranchType;

    @Column(name = "univ_type", length = Integer.MAX_VALUE)
    private String univType;

    @Column(name = "school_type", length = Integer.MAX_VALUE)
    private String schoolType;

    @Column(name = "estab_type", length = Integer.MAX_VALUE)
    private String estabType;

    @Column(name = "cido_code", length = Integer.MAX_VALUE)
    private String cidoCode;

    @Column(name = "cido_name", length = Integer.MAX_VALUE)
    private String cidoName;

    @Column(name = "street_address", length = Integer.MAX_VALUE)
    private String streetAddress;

    @Column(name = "number_address", length = Integer.MAX_VALUE)
    private String numberAddress;

    @Column(name = "street_code", length = Integer.MAX_VALUE)
    private String streetCode;

    @Column(name = "number_code", length = Integer.MAX_VALUE)
    private String numberCode;

    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;

    @Column(name = "phone_number", length = Integer.MAX_VALUE)
    private String phoneNumber;

    @Column(name = "fax_number", length = Integer.MAX_VALUE)
    private String faxNumber;

    @Column(name = "estab_date", length = Integer.MAX_VALUE)
    private String estabDate;

    @Column(name = "ref_year", length = Integer.MAX_VALUE)
    private String refYear;

    @Column(name = "data_ref_date", length = Integer.MAX_VALUE)
    private String dataRefDate;

    @Column(name = "prov_code", length = Integer.MAX_VALUE)
    private String provCode;

    @Column(name = "prov_name", length = Integer.MAX_VALUE)
    private String provName;

}