package org.eightbit.damdda.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.repository.MemberRepository;

import org.eightbit.damdda.member.repository.RegisterRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RegisterRepository registerRepository;
    private PasswordEncoder passwordEncoder;

    @Value("${cloud.aws.credentials.bucket}")
    private String bucketName;
    @Autowired
    private AmazonS3 amazonS3;

    @Override
    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {

        String fileName = "profile/"+file.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,fileName,file.getInputStream(),null).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);
        return amazonS3.getUrl(bucketName,fileName).toString();

    }

    @Override
    @Transactional
    public void deleteFIle(String fileName){
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }
    @Override
    public Optional<Member> findById(Long memberId){
        return memberRepository.findById(memberId);
    }

    @Override
    public Member getById(Long memberId){
        return memberRepository.getById(memberId);
    }

    @Override
    public MemberDTO getMember(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            return MemberDTO.of(member.get());
        }else {
            return null;
        }
    }

//    @Override
//    public MemberDTO confirmPw(String loginId, String password) {
//        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
//        System.out.println("박세연" + optionalMember);
//        System.out.println(2 + optionalMember.get().getPassword());
//        System.out.println(3 + passwordEncoder.encode(password));
//        if(optionalMember.isPresent()) {
//            Member member = optionalMember.get();
//            if(passwordEncoder.matches(password, member.getPassword())) {
//                return MemberDTO.of(member);
//            }
//        }
//        return null;
//    }

//    @Override
//    public MemberDTO updateMember(MemberDTO memberDTO) {
//        String userId = SecurityContextHolder.getPrincipal().getLoginId();
//
//        Optional<Member> member = memberRepository.findByLoginId(loginId);
//
//
//        return null;
//    }


}
