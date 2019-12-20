package org.jarvis.kk.domain;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Token
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Token {

    private String token;
}