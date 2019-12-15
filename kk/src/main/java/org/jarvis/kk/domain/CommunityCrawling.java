package org.jarvis.kk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * CommunityCrawling
 */
@Data
@Entity
@Table(name = "tbl_CommunityCrawling")
public class CommunityCrawling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    private Integer price;
    private String category, title;
    
    @Column(length = 1000)
    private String image, link;

    private boolean lastCrawling;
}