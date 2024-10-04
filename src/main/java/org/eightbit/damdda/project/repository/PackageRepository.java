package org.eightbit.damdda.project.repository;


import org.eightbit.damdda.project.domain.ProjectPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<ProjectPackage,Long> {

}
