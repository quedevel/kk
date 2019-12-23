package org.jarvis.kk.domain;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Token
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Token {

    private String token;
}