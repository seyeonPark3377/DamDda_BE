//package org.eightbit.damdda.project.domain;
//
//import lombok.*;
//import org.eightbit.damdda.common.domain.DateEntity;
//
//import javax.persistence.*;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Entity
//@Table(name = "projects")
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString(exclude = {"category", "tags", "projectImages"})
//public class ProjectBackup extends DateEntity {
//
////    @ManyToOne
////    private Member member;
//    @ManyToOne
//    @JoinColumn(name = "category", referencedColumnName = "name")
//    private Category category;
//
//    @OneToMany
//    private List<ProjectImage> projectImages;
//
//    @ManyToMany
//    @JoinTable(
//            name = "project_tag",
//            joinColumns = @JoinColumn(name = "project_id"),
//            inverseJoinColumns = @JoinColumn(name = "tags_id")
//    )
//    private List<Tag> tags;
//
//    private String title;
//    private String description;
//    private String descriptionDetail;
//    private Timestamp startDate;
//    private Timestamp endDate;
//    private Long targetFunding;
//    private Long fundsReceive;
//    private Long supporterCnt;
//    private Long viewCnt;
//    private Long likeCnt;
//    private String thumbnailUrl;
//    private Timestamp submitAt;
//
//}
