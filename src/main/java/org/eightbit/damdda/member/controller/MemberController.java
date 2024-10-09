package org.eightbit.damdda.member.controller;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.member.domain.AccountCredentials;
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
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<MemberDTO> getProfile (@RequestParam("loginId") String loginId){
        try {
            MemberDTO memberDTO = memberService.getMember(loginId);
            return ResponseEntity.ok(memberDTO);
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

//    @PutMapping("/profile/Photo")
//    public ResponseEntity<String> updateProfilePhoto (@RequestBody MemberDTO memberDTO, HttpSession session){
//
//    }
//
//    @Transactional
//    @PutMapping("/profile")
//    public ResponseEntity<String> updateProfile (@RequestBody MemberDTO memberDTO, HttpSession session){
//
//    }
}

