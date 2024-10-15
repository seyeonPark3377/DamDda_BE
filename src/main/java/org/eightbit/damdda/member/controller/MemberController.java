package org.eightbit.damdda.member.controller;

import lombok.RequiredArgsConstructor;

import org.eightbit.damdda.member.dto.MemberSearchDTO;
import org.eightbit.damdda.member.dto.PasswordDTO;
import org.eightbit.damdda.security.user.AccountCredentials;
import org.eightbit.damdda.security.jwt.JwtService;
import org.eightbit.damdda.security.user.User;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.dto.RegisterDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.service.LoginService;
import org.eightbit.damdda.member.service.MemberService;
import org.eightbit.damdda.member.service.RegisterService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.NoSuchElementException;

import java.io.IOException;


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

    @PostMapping
    public ResponseEntity<String> insertMember (@RequestBody RegisterDTO registerDTO){

        try {
            registerService.insertMember(registerDTO);
            return ResponseEntity.ok("success");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

    @GetMapping("/profile/{id}")
    public ResponseEntity<MemberDTO> getProfile (@RequestParam("loginId") String loginId){
        try {
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

    @GetMapping("/confirmpw")
    public ResponseEntity<?> confirmPassword (@RequestParam String password){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String loginId = user.getMember().getLoginId();
            System.out.println(loginId + " " + password);
            MemberDTO memberDTO = memberService.confirmPw(loginId, password);

            if(memberDTO != null){
                return ResponseEntity.ok(memberDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
        }
    }

    @PutMapping("/{id}/Photo")
    public ResponseEntity<String> updateProfilePhoto (@RequestBody MultipartFile imageUrl, HttpSession session) throws IOException {
        try {

            String fileName = memberService.uploadFile(imageUrl);
            return ResponseEntity.ok(fileName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateProfile (@RequestBody MemberDTO memberDTO){
        try {
            return ResponseEntity.ok(memberService.updateMember(memberDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Long>> checkMemberDetails(MemberSearchDTO memberSearchDTO){
        try {
            return ResponseEntity.ok(Map.of("id", loginService.checkMemberDetails(memberSearchDTO)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, Boolean>> modifyPassword(
            @PathVariable Long id,
            @RequestBody PasswordDTO passwordDTO
    ){
        try {
            boolean result = loginService.modifyPassword(id, passwordDTO.getPassword());
            return ResponseEntity.ok(Map.of("isSuccess", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}