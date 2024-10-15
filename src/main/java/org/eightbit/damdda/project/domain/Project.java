package org.eightbit.damdda.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"category", "tags"})
public class Project extends DateEntity {

    @ManyToOne
    private Member member;

    @ManyToOne
    private Category category;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    private List<Tag> tags;

    private String title;
    private String description;

//    @Column(length = 10000000)
    @Column(columnDefinition = "TEXT")
    private String descriptionDetail;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long targetFunding;
    @ColumnDefault("0")
    private Long fundsReceive;
    @ColumnDefault("0")
    private Long supporterCnt;
    @ColumnDefault("0")
    private Long viewCnt;
    @ColumnDefault("0")
    private Long likeCnt;
    private String thumbnailUrl;
    private Timestamp submitAt;

//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval=true)
//    private List<PackageRewards> packageRewards;

}
