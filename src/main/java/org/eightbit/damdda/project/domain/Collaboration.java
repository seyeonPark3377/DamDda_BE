package org.eightbit.damdda.project.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jshell.Snippet;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter //for testcode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collaboration {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate savedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate senderDeletedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiverDeletedAt;

    // 굳이 member와 연동할 필요가 있나?
    private String userId;

    @OneToOne
    @JoinColumn(name="projectId")
    private Project project;

    private Date approvalDate;

    @Builder.Default
    private String approval="대기";

    private String collaborationText;
    private String name; //프로젝트 제목. -> 나중에 바꾸기.
    private String email;
    private String phoneNumber;

    //협업 제안 시 필요한 파일.
    @Column(columnDefinition = "json")
    private String collabDocList;


    /* setter 함수 */
    public void addSenderDeletedAt() {
        this.senderDeletedAt = LocalDate.now();
    }
    public void addReceiverDeletedAt() {
        this.receiverDeletedAt = LocalDate.now();
    }

    public void setCollabDocList(List<String> collabs) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.collabDocList = objectMapper.writeValueAsString(collabs);
    }
    //json 역직렬화
    public List<String> getCollabDocList() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.collabDocList,new TypeReference<List<String>>(){});
    }

    public void removeCollabDocList(){
        this.collabDocList = null;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return "Collaboration{" +
                    "id=" + id +
                    ", savedAt=" + savedAt +
                    ", senderDeletedAt=" + senderDeletedAt +
                    ", receiverDeletedAt=" + receiverDeletedAt +
                    ", userId=" + userId +
                    ", project=" + project +
                    ", approvalDate=" + approvalDate +
                    ", approval='" + approval + '\'' +
                    ", collaborationText='" + collaborationText + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", collabDocList=" + objectMapper.writeValueAsString(getCollabDocList()) +
                    '}';
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error processing JSON";
        }
    }


}
