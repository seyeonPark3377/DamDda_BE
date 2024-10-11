package org.eightbit.damdda.member.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

import org.eightbit.damdda.member.domain.AccountCredentials;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.domain.User;
import org.eightbit.damdda.member.dto.LoginDTO;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.dto.RegisterDTO;
import org.eightbit.damdda.member.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.NoSuchElementException;

import java.io.IOException;
import java.util.UUID;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member") // member로 변경하는게 적절
public class MemberController {

    private final RegisterService registerService;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal User user){
        Long memberId = user.getMemberId();
        Map userInfo = memberService.getUserInfo(memberId);
        return ResponseEntity.ok().body(userInfo);
    }

    @PostMapping("/profile")
    public String insertMember (@RequestBody RegisterDTO registerDTO){
        try {
            registerService.insertMember(registerDTO);
            return "success";
        } catch (IllegalArgumentException e){
            return "error";
        }
    }

    @GetMapping("/check/id")
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

    @GetMapping("/check/nickname")
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
    public ResponseEntity<?> login (@RequestBody AccountCredentials credentials){
        try {
            UsernamePasswordAuthenticationToken creds =         // 인증 아직 안됨
                    new UsernamePasswordAuthenticationToken(
                            credentials.getLoginId(),
                            credentials.getPassword()
                    );

            Authentication auth = authenticationManager.authenticate(creds);
            String currentUserNickname = ((User) auth.getPrincipal()).getNickname();
            String jwts = jwtService.getToken(((User) auth.getPrincipal()).getMember().getId(), auth.getName());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                    .body(Map.of("X-Nickname", currentUserNickname));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        return ResponseEntity.ok("logout");
    }


    @GetMapping("/profile")
    public ResponseEntity<MemberDTO> getProfile (@AuthenticationPrincipal User user){
        try {
            String loginId = user.getLoginId();
            return ResponseEntity.ok(memberService.getMember(loginId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/findid")
    public ResponseEntity<String> findId(String name, String email){
        try {
            return ResponseEntity.ok(loginService.findId(name, email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


//    @PutMapping("/profile")
//    public ResponseEntity<MemberDTO> updateProfile (@RequestBody MemberDTO memberDTO){
//        try {
//            return ResponseEntity.ok(memberService.updateMember(memberDTO));
//        } catch (IllegalArgumentException e) {
//            return null;
//        }
//    }

//    @PostMapping("/confirmpw")
//    public ResponseEntity<?> confirmPassword (@RequestBody String password){
//        try {
//            String loginId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

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
//            MemberDTO memberDTO = memberService.confirmPw(loginId, password);
//
//            if(memberDTO != null){
//                return ResponseEntity.ok(memberDTO);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
//        }
//    }
}

