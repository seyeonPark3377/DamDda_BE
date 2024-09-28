package org.eightbit.damdda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationDTO {
    private Long id;//클라이언트로는 받지 않지만, 서버 -> 클라이언트때는 받음

    @NotBlank(message = "제목은 필수 값입니다.")
    private String title;
    @NotBlank(message="승인 상태는 필수 값입니다.")
    private String approval;
    @NotBlank(message="날짜는 필수 값입니다.")
    private LocalDateTime CollaborateDate;
    @NotBlank(message="이름은 필수 값입니다.")
    private String name;
}
