package org.eightbit.damdda.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.repository.MemberRepository;
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

    @Value("${cloud.aws.credentials.bucket}")
    private String bucketName;
    @Autowired
    private AmazonS3 amazonS3;

    @Override
    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = "profile/"+ UUID.randomUUID()+"_"+file.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,fileName,file.getInputStream(),null);
        amazonS3.putObject(putObjectRequest);
        return fileName;

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
//    public MemberDTO updateMember(String loginId, MemberDTO updateMemberDTO) {
//        Member member = memberRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalArgumentException("not Found"));
//        member.updateInfo(updateMemberDTO.toEntity());
//        return null;
//    }


}
