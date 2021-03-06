package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.participation.entity.PermissionResult;
import lombok.Getter;

@Getter
public class PermissionResponseDto {

    private Long participationId;

    private PermissionResult permissionResult;

    public PermissionResponseDto(Long participationId, PermissionResult permissionResult) {
        this.participationId = participationId;
        this.permissionResult = permissionResult;
    }

    public static PermissionResponseDto of(Participation participation) {
        return new PermissionResponseDto(participation.getId(), participation.getResult());
    }
}
