package org.eightbit.damdda.member.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.dto.LoginDTO;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.dto.RegisterDTO;
import org.eightbit.damdda.member.service.LoginService;
import org.eightbit.damdda.member.service.MemberService;
import org.eightbit.damdda.member.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final RegisterService registerService;
    private final LoginService loginService;
    private final MemberService memberService;



    @PostMapping("/profile")
    public String insertMember (@RequestBody RegisterDTO registerDTO){

        try {
            registerService.insertMember(registerDTO);
            return "success";
        } catch (IllegalArgumentException e){
            return "error";
        }
    }

    @GetMapping("/profile/idcheck")
    public ResponseEntity<String> checkLoginId(@RequestParam("loginId") String loginId){
        try {
            if (registerService.checkLoginId(loginId)){
                return new ResponseEntity<>("unavailable", HttpStatus.OK);
            }
            return new ResponseEntity<>("available", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/profile/nickname")
    public ResponseEntity<String> checkNickname(@RequestParam("nickname") String nickname){
        try {
            if (registerService.checkNickname(nickname)){
                return new ResponseEntity<>("unavailable", HttpStatus.OK);
            }
            return new ResponseEntity<>("available", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginDTO loginDTO, HttpSession session){
        try {
            MemberDTO memberDTO = loginService.login(loginDTO, session);
            return ResponseEntity.ok(memberDTO.getNickname() +" "+ memberDTO.getLoginId()+" "+memberDTO.getId());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        loginService.logout(session);
        return ResponseEntity.ok("logout");
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberDTO> getProfile (@RequestParam("loginId") String loginId){
        try {
            MemberDTO memberDTO = memberService.getMember(loginId);
            return ResponseEntity.ok(memberDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/profile/Photo")
    public ResponseEntity<String> updateProfilePhoto (@RequestBody MultipartFile imageUrl, HttpSession session) throws IOException {
        try {

            String fileName = memberService.uploadFile(imageUrl);
            return ResponseEntity.ok(fileName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
//
//    @Transactional
//    @PutMapping("/profile")
//    public ResponseEntity<String> updateProfile (@RequestBody MemberDTO memberDTO, HttpSession session){
//
//    }
}

