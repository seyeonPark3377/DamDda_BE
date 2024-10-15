package org.eightbit.damdda.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.eightbit.damdda.member.domain.Member;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"category", "tags"})
@EntityListeners(value={AuditingEntityListener.class})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @CreatedDate
    @Column(updatable = false)
    private Timestamp createdAt;

    private Timestamp deletedAt;


//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval=true)
//    private List<PackageRewards> packageRewards;

}
