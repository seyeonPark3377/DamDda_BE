package org.eightbit.damdda.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eightbit.damdda.project.dto.PackageDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportingPackageDTO {

    private PackageDTO packageDTO;

    private Integer packageCount;
}