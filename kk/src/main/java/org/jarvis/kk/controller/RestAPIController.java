package org.jarvis.kk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.jarvis.kk.domain.ClickHistory;
import org.jarvis.kk.domain.CommunityCrawling;
import org.jarvis.kk.domain.Member;
import org.jarvis.kk.dto.SessionMember;
import org.jarvis.kk.repositories.CategoryDTORepository;
import org.jarvis.kk.repositories.ClickHistoryRepository;
import org.jarvis.kk.repositories.CommunityCrawlingRepository;
import org.jarvis.kk.repositories.MemberRepository;
import org.jarvis.kk.service.FCMService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController
 */
@CrossOrigin
@RestController
@RequestMapping("/kk")
@RequiredArgsConstructor
@Slf4j
public class RestAPIController {

    private final CommunityCrawlingRepository communityCrawlingRepository;

    private final ClickHistoryRepository clickHistoryRepository;

    private final MemberRepository memberRepository;

    private final CategoryDTORepository categoryDTORepository;
    
    private final FCMService fcmService;

    private final HttpSession session;

    @Transactional
    @PutMapping("/updateInterest")
    public Integer updateInterest(@RequestBody String[] interests) {
        SessionMember member = (SessionMember) session.getAttribute("member");
        Member realMember = memberRepository.getOne(member.getMid());
        memberRepository.save(realMember.setInterestList(interests));
        return HttpStatus.OK.value();
    }

    @GetMapping("/interestChecking")
    public Integer interestChecking() {
        SessionMember member = (SessionMember) session.getAttribute("member");
        return member.getInterests().size() == 0 ? HttpStatus.MOVED_PERMANENTLY.value() : HttpStatus.OK.value();
    }

    @PostMapping("/token")
    public ResponseEntity<String> registerToken(@RequestBody String token) {
        SessionMember member = (SessionMember) session.getAttribute("member");
        fcmService.addAllTopics(token);
        //delete 안하고 insert만 해야됨
        memberRepository.save(memberRepository.getOne(member.getMid()).setToeknList(token));
        //서버 로딩시 토큰 테이블 읽어와서 전부다 토픽 추가해줘야댐 서버 내려가는 순간 토픽 증발
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommunityCrawling>> getList() {
        SessionMember member = (SessionMember) session.getAttribute("member");
        Member realMember = memberRepository.findById(member.getMid()).get();
        List<String> totalInterest = new ArrayList<>();
        List<String> analysisData = clickHistoryRepository.groupByCategoryCount(realMember);
        realMember.getInterests().forEach(interest->{
            String category = interest.getKeyword();
            totalInterest.add(category);
            analysisData.remove(category);
        });

        totalInterest.addAll(analysisData);

        //나중에 빈처리
        Map<String, List<CommunityCrawling>> codeToData = new HashMap<>();
        categoryDTORepository.findAll().forEach(category->codeToData.put(category.getCode(), new ArrayList<>()));
        Set<String> totalCode = codeToData.keySet();


        //로직
        communityCrawlingRepository.findAll(PageRequest.of(0, 50, Sort.Direction.DESC, "no")).toList().forEach(data->codeToData.get(data.getProduct().getCategory()).add(data));
        List<CommunityCrawling> result = new ArrayList<>();
        totalInterest.forEach(code->{
            result.addAll(codeToData.get(code));
            totalCode.remove(code);
        });
        totalCode.forEach(code->result.addAll(codeToData.get(code)));

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/click")
    public void pileUpClickHistory(@RequestParam("no") Integer communityCrawlingNo){
        SessionMember member = (SessionMember) session.getAttribute("member");

        clickHistoryRepository.save(ClickHistory.builder()
        .member(memberRepository.getOne(member.getMid()))
        .communityCrawling(communityCrawlingRepository.getOne(communityCrawlingNo)).build());
    }

    @GetMapping("/msg")
    public void pushAllFcm() {
        fcmService.pushAllFcm();
    }
}