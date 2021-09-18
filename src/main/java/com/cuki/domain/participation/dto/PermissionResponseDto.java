package com.cuki.domain.participation.dto;

import com.cuki.domain.participation.domain.PermissionResult;
import lombok.Getter;

@Getter
public class PermissionResponseDto {

    private Long participationId;

    private PermissionResult permissionResult;

    public PermissionResponseDto(Long participationId, PermissionResult permissionResult) {
        this.participationId = participationId;
        this.permissionResult = permissionResult;
    }
}
