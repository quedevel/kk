package org.jarvis.kk.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jarvis.kk.dto.BaseTimeEntity;

import lombok.Getter;

/**
 * ExecuteHistory
 */
@Getter
@Entity
@Table(name = "tbl_ExecuteHistory")
public class ExecuteHistory extends BaseTimeEntity {

    @Id
    private Integer no;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}