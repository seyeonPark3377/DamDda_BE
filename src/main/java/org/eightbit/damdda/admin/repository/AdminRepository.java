package org.eightbit.damdda.admin.repository;


import org.eightbit.damdda.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
