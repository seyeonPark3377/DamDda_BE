package org.eightbit.damdda.project.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime savedAt;
    private LocalDateTime senderDeletedAt;
    private LocalDateTime receiverDeletedAt;

    // 굳이 member와 연동할 필요가 있나?
    private Long userId;

    @OneToOne
    @JoinColumn(name="projectId")
    private Project project;

    private LocalDateTime approvalDate;
    private String approval;
    private String collaborationText;
    private String name;
    private String email;
    private String phoneNumber;

    public void addSenderDeletedAt() {
        this.senderDeletedAt = LocalDateTime.now();
    }
    public void addReceiverDeletedAt() {
        this.receiverDeletedAt = LocalDateTime.now();
    }
    public void change(Long user_id, Project project, String content, String name, String email, String phoneNumber){
        this.userId=user_id;
        this.project=project;
        this.collaborationText=content;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }

    //협업 제안 시 필요한 파일.
    @Column(columnDefinition = "json")
    private String collabDocList;

    
  public void setCollabDocList(List<String> collabs) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      this.collabDocList = objectMapper.writeValueAsString(collabs);
  }

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
