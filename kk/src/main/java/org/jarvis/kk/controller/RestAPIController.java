package org.jarvis.kk.controller;

import java.util.List;

import org.jarvis.kk.domain.CommunityCrawling;
import org.jarvis.kk.repositories.CommunityCrawlingRepository;
import org.jarvis.kk.util.FirebaseUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController
 */
@CrossOrigin
@RestController
@RequestMapping("/kk")
@AllArgsConstructor
@Slf4j
public class RestAPIController {

    private CommunityCrawlingRepository communityCrawlingRepository;


    @PostMapping("/token")
    public ResponseEntity<String> registerToken(@RequestBody String token) {
        //id와 token 등록 // 예외 이용할것!
        log.info(token);
        FirebaseUtil.addAllTopics("dNqYksXvvBUjGmaNbO237yv4Q47g1OyrTuvXXioqOSEldgzIBHcnQNdLYWCgEi");
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommunityCrawling>> getList() {
        return new ResponseEntity<>(communityCrawlingRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "no")).toList(),HttpStatus.OK);
    }

}