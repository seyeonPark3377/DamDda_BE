package org.eightbit.damdda.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.dto.PackageDTO;
import org.eightbit.damdda.project.dto.RewardDTO;
import org.eightbit.damdda.project.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/packages")
@Log4j2
public class PackageController {

    @Autowired
    PackageService packageService;

    @GetMapping("/test")
    public ResponseEntity<?> getProject() {
        // 프로젝트 조회 로직
        return ResponseEntity.ok("Imok");
    }
    //package 등록
    @PostMapping("/register/{projectId}")
    public ResponseEntity<?> registerPackage(@Valid @RequestBody PackageDTO packageDTO, @PathVariable("projectId") Long projectId){
        packageService.registerPackage(packageDTO,projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //특정 프로젝트의 PACKAGE 조회
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> viewPackage(@PathVariable("projectId") Long projectId) throws JsonProcessingException{
        List<PackageDTO> packages = packageService.viewPackage(projectId);
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    //package 수정
    @PutMapping("/modify")
    public ResponseEntity<?> modifyPackage(@Valid @RequestBody PackageDTO packageDTO, @RequestParam("projectId") Long projectId){
        packageService.modifyPackage(packageDTO, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Package 삭제
    @DeleteMapping("/delete/{packageId}")
    public ResponseEntity<?> deletePackage(@PathVariable("packageId") Long packageId) {
        packageService.deletePackage(packageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Reward 등록
    @PostMapping("/rewards/register/{projectId}")
    public ResponseEntity<?> registerReward(@Valid @RequestBody RewardDTO rewardDTO,@PathVariable("projectId") Long projectId) throws JsonProcessingException {
        packageService.registerReward(rewardDTO, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Reward registered successfully");
    }
    @GetMapping("/rewards/package/{packageId}")
    public ResponseEntity<?> viewRewardByPackage(@PathVariable("packageId") Long packageId) {
        List<RewardDTO> rewards = packageService.viewRewardByPackage(packageId);
        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/rewards/project/{projectId}")
    public ResponseEntity<?> viewRewardByProject(@PathVariable("projectId") Long projectId) {
        List<RewardDTO> rewards = packageService.viewRewardByProject(projectId);
        return ResponseEntity.ok(rewards);
    }

    @DeleteMapping("/rewards/delete/{rewardId}")
    public ResponseEntity<?> deleteReward(@PathVariable("rewardId") Long rewardId) {
        packageService.deleteReward(rewardId);
        return ResponseEntity.noContent().build();
    }
}