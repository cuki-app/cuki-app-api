package com.cuki.controller.dto;

import com.cuki.controller.PermissionResult;
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
