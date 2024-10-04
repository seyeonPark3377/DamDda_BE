package org.eightbit.damdda.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    // ModelMapper Bean 등록.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // Getter, Setter가 없는 field도 자체의 이름을 사용하여 mapping 시도.
                .setFieldMatchingEnabled(true)
                // Private 접근 제한자를 가진 field에도 접근 가능.
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
                // field 이름이 정확히 일치하지 않더라도 유사한 필드 매칭.
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}
