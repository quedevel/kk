package org.jarvis.kk.service;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.jarvis.kk.domain.Member;
import org.jarvis.kk.dto.OAuth2Attributes;
import org.jarvis.kk.dto.SessionMember;
import org.jarvis.kk.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * OAuth2MemberService
 */
@RequiredArgsConstructor
@Service
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        httpSession.setAttribute("member", new SessionMember(member));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private Member saveOrUpdate(OAuth2Attributes attributes) {
        Member member = memberRepository.findById(attributes.getMid()).map(entity->entity.update(attributes.getSex(), attributes.getAgeGroup())).orElse(attributes.toEntity());
        return memberRepository.save(member);
    }
}