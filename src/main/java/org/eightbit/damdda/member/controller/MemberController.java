package org.eightbit.damdda.member.controller;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.member.domain.AccountCredentials;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.domain.User;
import org.eightbit.damdda.member.dto.LoginDTO;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.dto.PasswordModifyDTO;
import org.eightbit.damdda.member.dto.RegisterDTO;
import org.eightbit.damdda.member.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member") // member로 변경하는게 적절
public class MemberController {

    private final RegisterService registerService;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;

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


    @GetMapping("/logout")
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

    @PostMapping("/findpw")
    public ResponseEntity<String> findPassword (@RequestBody PasswordModifyDTO passwordModifyDTO){
        try {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @PutMapping("/findpw")
    public ResponseEntity<String> modifyPassword (@RequestBody PasswordModifyDTO passwordModifyDTO){
        try {
            System.out.println(passwordModifyDTO);
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

