package org.ruscayn.infradiagram.settings;

import org.ruscayn.infradiagram.input.InputLimits;

/**
 * 그림을 그리는 데 쓰는 값의 묶음이다. 기본값은 StandardSettings에 이름을 붙여 모아 두었다.
 */
public record DrawingSettings(
        double viewAngleDegrees,
        PillarSizes pillarSizes,
        Spacing spacing,
        LabelSettings labels,
        CanvasSettings canvas,
        LineSettings lines,
        QualityThresholds quality,
        InputLimits limits,
        int colorCount) {

    public static DrawingSettings standard() {
        return StandardSettings.create();
    }
}
