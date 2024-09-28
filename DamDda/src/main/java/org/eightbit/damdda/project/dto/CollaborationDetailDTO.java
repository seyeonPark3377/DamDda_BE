package org.eightbit.damdda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationDetailDTO {
    /*제목이 프로젝트의 제목이라면 백쪽에서 처리를 해주는 것도 좋을 듯-> */
    @NotBlank(message="제목은 필수 값입니다.")
    private String title;

    @NotBlank(message="전화번호는 필수 값입니다.")
    private String phoneNumber;

    @Pattern(regexp="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",message="이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @NotBlank(message="세부 내용을 입력해주세요.")
    private String content;

    //NotBlank -> 라고 하면 어떻게 되는 지 알지 못해서 안넣음.
    private CollaborationDTO collaborationDTO;

    private Long user_id;

    // 파일 목록 -> 필수가 아닐 수도 있겠다는 의견.
    private List<Object> collabDocList;


}
