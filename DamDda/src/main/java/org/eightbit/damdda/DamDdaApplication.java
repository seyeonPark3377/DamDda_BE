package org.eightbit.damdda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = {"org.eightbit.damdda.project.domain", "org.eightbit.damdda.member.domain", "org.eightbit.damdda.order.domain", "org.eightbit.damdda.admin.domain"})
public class DamDdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DamDdaApplication.class, args);
    }


}
