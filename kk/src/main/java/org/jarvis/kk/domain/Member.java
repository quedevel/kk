package org.jarvis.kk.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


import org.jarvis.kk.dto.BaseTimeEntity;
import org.jarvis.kk.dto.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Member
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_member")
public class Member extends BaseTimeEntity {

    @Id
    private String mid;

    private String sex;

    private String ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_Token", joinColumns = @JoinColumn(name="mid"))
    private List<Token> tokens;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_Interest", joinColumns = @JoinColumn(name="mid"))
    private List<Interest> interests;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_Wanted", joinColumns = @JoinColumn(name="mid"))
    private List<Wanted> wanteds;

    public Member update(String sex, String ageGroup){
        this.sex = sex;
        this.ageGroup = ageGroup;

        return this;
    }
}