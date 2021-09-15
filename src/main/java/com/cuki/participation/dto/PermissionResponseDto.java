package com.cuki.participation.dto;

import com.cuki.participation.domain.PermissionResult;
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
