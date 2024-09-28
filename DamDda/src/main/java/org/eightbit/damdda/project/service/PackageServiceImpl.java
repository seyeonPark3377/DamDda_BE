package org.eightbit.damdda.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.PackageRewards;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectPackage;
import org.eightbit.damdda.project.domain.ProjectRewards;
import org.eightbit.damdda.project.dto.PackageDTO;
import org.eightbit.damdda.project.dto.RewardDTO;
import org.eightbit.damdda.project.repository.PackageRepository;
import org.eightbit.damdda.project.repository.PackageRewardsRepository;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.eightbit.damdda.project.repository.RewardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {


    private final PackageRewardsRepository packageRewardsRepository;
    private final RewardRepository rewardRepository;
    private final PackageRepository packageRepository;
    private final ProjectRepository projectRepository;


    //선물 등록
    @Override
    @Transactional
    public void registerReward(RewardDTO rewardDTO, Long project_id) throws JsonProcessingException {
        Project project = projectRepository.findById(project_id).orElseThrow(() -> new EntityNotFoundException("project not found"));
        ProjectRewards projectRewards = rewardDtoToEntity(rewardDTO); //reward dto -> entity
        projectRewards.setOptionList(rewardDTO.getOptionList()); //optionDTO형태라서 -> projectOption 형태로 바꾸기 위해.
        projectRewards.setPackageReward(project);
        rewardRepository.save(projectRewards);


    }

    //선물 구성 등록
//    @Override
//    public void registerPackage(PackageDTO packageDTO, Long project_id) {
//
//        List<PackageRewards> packageRewardsList = new ArrayList<>();
//        /*!----------------------중요----------------------!*/
//        //프로젝트 등록하기를 누르자마자 db에 저장이 되어야 함.
//        Project project = projectRepository.findById(project_id).orElseThrow(()->new EntityNotFoundException("project not found"));
//
//        // projectPackage 객체 생성
//        ProjectPackage projectPackage = packageDTOToEntity(packageDTO);//PackageDTO -> ProjectPackage Entity
////        List<Long> rewardIds = packageDTO.getRewardList().stream().map(RewardDTO::getId)
////                .collect(Collectors.toList());
////        Map<Long, ProjectRewards> projectRewardsMap = rewardRepository.findAllById(rewardIds).stream().collect(Collectors.toMap(ProjectRewards::getId, reward -> reward));
//
//        //packageReward 생성 -> for rewardCount 설정
//
//        packageDTO.getRewardList().stream().forEach(reward -> {
//            List<PackageRewards> packageRewards = packageRewardsRepository.findPackageRewardByRewardId(reward.getId());
//            log.info(packageRewards);
//            for(PackageRewards packageReward:packageRewards) {
//                packageReward.changeRewardCount(reward.getCount());
//                packageReward.setProjectPackage(projectPackage);
//            }
//            packageRewardsList.addAll(packageRewards);
//            log.info("여기여기여기여기"+packageRewards);
//        });
////        projectPackage.addPackageReward(packageRewardsList);
//        // PackageRewards를 저장, cascade 설정에 의해 ProjectPackage도 저장
//        log.info("여기여기여기여기"+projectPackage);
//        packageRepository.save(projectPackage);
//    }
    @Override
    public void registerPackage(PackageDTO packageDTO, Long project_id) {
        List<PackageRewards> packageRewardsList = new ArrayList<>();
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new EntityNotFoundException("project not found"));

        log.info(packageDTO);
        ProjectPackage projectPackage = packageDTOToEntity(packageDTO);

        if (packageDTO.getRewardList() == null) {
            log.error("RewardList is null in packageDTO");
            throw new IllegalArgumentException("RewardList cannot be null");
        }

        packageDTO.getRewardList().forEach(reward -> {
            if (reward.getId() == null) {
                log.error("Reward ID is null for reward: {}", reward);
                return; // Skip this iteration
            }

            List<PackageRewards> packageRewards = packageRewardsRepository.findPackageRewardByRewardId(reward.getId());
            log.info("Found PackageRewards for reward ID {}: {}", reward.getId(), packageRewards);

            if (packageRewards == null || packageRewards.isEmpty()) {
                log.warn("No PackageRewards found for reward ID: {}", reward.getId());
                return; // Skip this iteration
            }

            for (PackageRewards packageReward : packageRewards) {
                if (packageReward == null) {
                    log.warn("Null PackageReward found in the list for reward ID: {}", reward.getId());
                    continue; // Skip this packageReward
                }
                packageReward.changeRewardCount(reward.getCount());
                packageReward.setProjectPackage(projectPackage);
            }

            packageRewardsList.addAll(packageRewards);
            log.info("Added PackageRewards for reward ID {}: {}", reward.getId(), packageRewards);
        });

        log.info("Final ProjectPackage: {}", projectPackage);
        packageRepository.save(projectPackage);
    }


    
    //패키지에 해당하는 선물 보여주는 기능
    @Override
    @Transactional(readOnly=true)
    public List<RewardDTO> viewRewardByPackage(Long package_id) {
        //패키지와 연관된 리워드 리스트를 보여줌.
        List<ProjectRewards> projectRewards = packageRewardsRepository.findRewardsByPackageId(package_id);
        List<RewardDTO> rewardDTOList = projectRewards.stream().map(reward -> {
            // packageRewardsRepository에서 reward_count를 들고옴
            int rewardCount = packageRewardsRepository.findRewardCountByRewardId(package_id, reward.getId());
            try {
                return new RewardDTO(reward.getId(), reward.getRewardName(), rewardCount, reward.getOptionList(), reward.getOptionType());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return rewardDTOList;
    }

    //프로젝트에 해당하는 리워드 보여주는 기능. 화면 설계서 17번, 19번
    @Override
    @Transactional(readOnly=true)
    public List<RewardDTO> viewRewardByProject(Long project_id) {
        //프로젝트와 연관된 리워드 리스트를 보여줌
        List<RewardDTO> rewardDTOList = packageRewardsRepository.findRewardsByProjectId(project_id).stream().map(reward -> {
            //대신 rewardCount는 설정 전이므로 입력x -> 임의의 rewardCount 초기값을 주는 형태 / 아니면 RewardDTO를 고치는 형태
            try {
                return new RewardDTO(reward.getId(), reward.getRewardName(), 0, reward.getOptionList(), reward.getOptionType()); //아직 reward가 저장되지 않았으므로 count는 0
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return rewardDTOList;
    }

    //프로젝트에 해당하는 패키지 조회
    @Override
    @Transactional(readOnly=true)
    public List<PackageDTO> viewPackage(Long project_id) {
        List<PackageDTO> packageDTOList = new ArrayList<>();
        //1) 프로젝트와 연관된 package 리스트를 보여줌
        List<PackageRewards> packageRewardList = packageRewardsRepository.findAllByProjectIdWithPackageAndRewards(project_id); //project에 연결된 packageReward가 여러 개이면 중복될 수 있음
        List<ProjectPackage> projectPackage = packageRewardList.stream().map(PackageRewards::getProjectPackage).distinct().collect(Collectors.toList());
        for(ProjectPackage pp : projectPackage){
            List<ProjectRewards> projectRewards = packageRewardList.stream().filter(pr->pr.getProjectPackage().getId().equals(pp.getId())).map(PackageRewards::getProjectReward).collect(Collectors.toList());
            List<RewardDTO> rewardDTOList = projectRewards.stream().map(pr ->{
                try {
                    return rewardEntityToDto(pr,packageRewardList);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            packageDTOList.add(packageEntityToDTO(pp,rewardDTOList));
    }
        return packageDTOList;
}


    //선물 구성 수정
    @Override
    @Transactional
    public void modifyPackage(PackageDTO packageDTO, Long package_id, Long project_id) {
        // 기존의 projectPackage 조회
        ProjectPackage projectPackage = packageRepository.findById(package_id).orElseThrow();

        // 기존의 packageRewards 클리어
        projectPackage.getPackageRewards().clear();

        List<ProjectRewards> projectRewards = packageRewardsRepository.findRewardsByPackageId(package_id);
        Map<Long, ProjectRewards> projectRewardsMap = projectRewards.stream().collect(Collectors.toMap(ProjectRewards::getId,reward->reward));

        List<PackageRewards> newPackageRewards = packageDTO.getRewardList().stream()
                .map(reward -> {
                    return PackageRewards.builder()
                            .projectReward(projectRewardsMap.get(reward.getId()))
                            .rewardCount(reward.getCount())
                            .projectPackage(projectPackage)
                            .build();
                })
                .collect(Collectors.toList());

        projectPackage.getPackageRewards().addAll(newPackageRewards); //기존의 객체를 초기화하고 새로운 객체를 한 번에 저장.

        // projectPackage 업데이트
        projectPackage.change(packageDTO.getName(), packageDTO.getPrice(), packageDTO.getQuantityLimited());

        // 변경사항 저장
        packageRepository.save(projectPackage);
    }


    //선물 삭제
    @Override
    public void deleteReward(Long reward_id) {
        rewardRepository.deleteById(reward_id);
    }

    //선물 구성 삭제
    @Override
    public void deletePackage(Long package_id) {
        packageRepository.deleteById(package_id);
    }


}
