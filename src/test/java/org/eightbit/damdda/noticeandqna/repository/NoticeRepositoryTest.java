//package org.eightbit.damdda.noticeandqna.repository;
//
//import lombok.extern.log4j.Log4j2;
//import org.eightbit.damdda.noticeandqna.domain.Notice;
//import org.eightbit.damdda.noticeandqna.repository.NoticeRepository;
//import org.eightbit.damdda.project.domain.Project;
//import org.eightbit.damdda.project.repository.ProjectRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Log4j2
//@SpringBootTest
//@Transactional
//public class NoticeRepositoryTest {
//
//    @Autowired
//    private NoticeRepository noticeRepository;
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    /**
//     * 공지사항 저장 테스트
//     * 프로젝트와 연관된 공지사항을 저장한 후,
//     * 저장이 성공적으로 되었는지 검증.
//     */
//    @Test
//    public void testSaveNotice() {
//        try {
//            // 프로젝트 생성 및 저장
//            Project project = Project.builder()
//                    .title("Test Project")
//                    .build();
//            Project savedProject = projectRepository.save(project);
//
//            // 공지사항 생성 및 저장
//            Notice notice = Notice.builder()
//                    .title("New Notice")
//                    .content("This is a new notice.")
//                    .project(savedProject)
//                    .build();
//            Notice savedNotice = noticeRepository.save(notice);
//
//            // 공지사항 저장 검증
//            assertNotNull(savedNotice.getId(), "Notice ID should not be null");
//            assertEquals("New Notice", savedNotice.getTitle(), "Title should be 'New Notice'");
//            assertEquals("This is a new notice.", savedNotice.getContent(), "Content should match");
//        } catch (Exception e) {
//            log.error("Exception during testSaveNotice: {}", e.getMessage(), e);
//            fail("Exception occurred in testSaveNotice");
//        }
//    }
//
//    /**
//     * 프로젝트 ID 1에 20개의 공지사항 생성 테스트
//     * 프로젝트 ID 1에 20개의 공지사항을 생성한 후,
//     * 각 공지사항이 정상적으로 저장되었는지 검증.
//     */
//    @Test
//    public void testCreate20NoticesForProject() {
//        try {
//            // 프로젝트 ID 1 조회.
//            Project project = projectRepository.findById(1L).orElseThrow(() ->
//                    new IllegalArgumentException("Project with ID 1 not found")
//            );
//
//            // 20개의 공지사항 생성 및 저장.
//            for (int i = 1; i <= 20; i++) {
//                Notice notice = Notice.builder()
//                        .title("Notice Title " + i)
//                        .content("This is the content for Notice " + i)
//                        .project(project)
//                        .build();
//
//                // 공지사항 저장 및 검증.
//                Notice savedNotice = noticeRepository.save(notice);
//                assertNotNull(savedNotice.getId(), "Notice ID should not be null for Notice " + i);
//
//                log.info("Successfully created Notice {} with ID {}", i, savedNotice.getId());
//            }
//
//            log.info("Successfully created 20 notices for project ID 1.");
//        } catch (Exception e) {
//            log.error("Exception during testCreate20NoticesForProject: {}", e.getMessage(), e);
//            fail("Exception occurred in testCreate20NoticesForProject");
//        }
//    }
//
//    /**
//     * 삭제되지 않은 공지사항 조회 테스트
//     * 삭제되지 않은 상태의 공지사항을 조회하여
//     * 모든 공지사항의 deletedAt 필드가 null인지 검증.
//     */
//    @Test
//    public void testFindAllByDeletedAtIsNullAndProjectId() {
//        try {
//            log.info("Starting test for findAllByDeletedAtIsNullAndProjectId...");
//
//            // 프로젝트 ID 1에 해당하는 삭제되지 않은 공지사항 조회.
//            long projectId = 1L;
//            List<Notice> notices = noticeRepository.findAllByDeletedAtIsNullAndProjectId(projectId);
//
//            // 조회된 공지사항의 deletedAt 필드가 모두 null인지 확인.
//            assertNotNull(notices);
//            assertTrue(notices.stream().allMatch(notice -> notice.getDeletedAt() == null));
//            log.info("Number of notices found: {}", notices.size());
//        } catch (Exception e) {
//            log.error("Exception during testFindAllByDeletedAtIsNullAndProjectId: {}", e.getMessage(), e);
//            fail("Exception occurred in testFindAllByDeletedAtIsNullAndProjectId");
//        }
//    }
//
//    /**
//     * 공지사항 소프트 삭제 테스트
//     * 공지사항을 소프트 삭제한 후,
//     * 해당 공지사항의 deletedAt 필드가 정상적으로 설정되었는지 검증.
//     */
//    @Test
//    public void testSoftDeleteNotice() {
//        try {
//            log.info("Starting test for softDeleteNotice...");
//
//            // 프로젝트 ID 1 조회.
//            Project project = projectRepository.findById(1L).orElseThrow(() ->
//                    new IllegalArgumentException("Project with ID 1 not found")
//            );
//
//            // 공지사항 생성 및 저장.
//            Notice notice = Notice.builder()
//                    .title("Notice to delete")
//                    .content("This notice will be soft deleted.")
//                    .project(project)
//                    .build();
//            Notice savedNotice = noticeRepository.save(notice);
//            Long noticeId = savedNotice.getId();
//
//            // 공지사항 소프트 삭제.
//            int result = noticeRepository.softDeleteNotice(noticeId);
//
//            // 소프트 삭제 성공 여부 확인.
//            assertEquals(1, result);
//            log.info("Soft delete successful for notice with ID: {}", noticeId);
//
//            // 공지사항의 deletedAt 필드가 null이 아닌지 확인.
//            Notice deletedNotice = noticeRepository.findById(noticeId).orElseThrow();
//            assertNotNull(deletedNotice.getDeletedAt());
//            log.info("Notice soft deleted, deletedAt: {}", deletedNotice.getDeletedAt());
//        } catch (Exception e) {
//            log.error("Exception during testSoftDeleteNotice: {}", e.getMessage(), e);
//            fail("Exception occurred in testSoftDeleteNotice");
//        }
//    }
//}
