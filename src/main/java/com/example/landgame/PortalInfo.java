package com.example.landgame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PortalInfo {
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
}
