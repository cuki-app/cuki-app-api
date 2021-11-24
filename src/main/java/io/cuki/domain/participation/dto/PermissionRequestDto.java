package io.cuki.domain.participation.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PermissionRequestDto {

    private Long participationId;

    private Boolean accept;
}
