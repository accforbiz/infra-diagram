package org.ruscayn.infradiagram.settings;

import org.ruscayn.infradiagram.input.InputLimits;

/**
 * 그림을 보고 정한 값을 한 곳에 모은다. 값을 바꾸려면 이 파일만 고치면 된다.
 *
 * 길이의 단위는 두 가지다.
 * - "칸": 중간 규모 기둥의 반지름을 1로 본 길이. 자리 계산에 쓴다.
 *   그림 크기에 맞춰 넣을 때 배율을 곱해 픽셀로 바뀌므로, 요소가 많으면 한 칸이 작게 그려진다.
 * - 픽셀: 완성된 그림에서의 길이. 그림의 크기, 선의 굵기, 최소 크기에 쓴다.
 */
final class StandardSettings {

    /** 내려다보는 각도(도). 클수록 바로 위에서 보는 모습에 가까워진다. */
    private static final double VIEW_ANGLE_DEGREES = 30;

    /** 규모가 소, 중, 대인 기둥의 반지름(칸)과, 반지름 1에 대한 높이. */
    private static final PillarSizes PILLAR_SIZES = new PillarSizes(0.8, 1.0, 1.25, 3.0);

    /** 기둥 사이의 틈(칸). 바닥끼리, 그리고 앞 기둥과 뒤 기둥의 윗면 사이에 이만큼 띄운다. */
    private static final double PILLAR_GAP = 0.3;
    /** 무리 사이의 틈의 최솟값(칸). */
    private static final double MIN_CLUSTER_GAP = 2.0;
    /** 무리 사이의 틈을 무리 안의 가장 넓은 틈의 몇 배로 할지. 1보다 커야 한다. */
    private static final double CLUSTER_GAP_PER_NEIGHBOUR_GAP = 2.0;
    /** 무리의 가장자리와 글자의 화살표 사이의 틈(칸). */
    private static final double LABEL_GAP = 0.6;
    /** 짝수 번째 줄을 오른쪽으로 미는 거리(칸). 줄마다 엇갈리게 한다. */
    private static final double ROW_STAGGER = 1.5;

    /** 글자의 크기(칸), 줄 간격(글자 크기의 배수), 화살표가 시작되기 전에 비워 두는 길이(칸), 색 점의 반지름(칸). */
    private static final LabelSettings LABELS = new LabelSettings(0.8, 1.5, 1.0, 0.22);

    /** 그림의 가로와 세로(픽셀, 16대 9), 가장자리 여백(픽셀), 한 칸의 최대 크기(픽셀). */
    private static final CanvasSettings CANVAS = new CanvasSettings(1920, 1080, 60, 60);

    /** 기둥의 선, 점선, 점선 한 토막의 길이, 화살표의 선(모두 픽셀). */
    private static final LineSettings LINES = new LineSettings(2.5, 2.0, 9, 2.0);

    /** 기둥 반지름과 글자 크기의 최솟값(픽셀). 이보다 작게 그려야 하면 그리지 않는다. */
    private static final QualityThresholds QUALITY = new QualityThresholds(12, 16);

    /** 무리의 수, 한 무리의 요소 수, 글자 길이의 상한. */
    private static final InputLimits LIMITS = new InputLimits(12, 19, 30);

    /** 무리를 구분하는 색의 수. 모양의 수 3과 공약수가 없어야 모양과 색의 짝이 겹치지 않는다. */
    private static final int COLOR_COUNT = 4;

    private StandardSettings() {
    }

    static DrawingSettings create() {
        Spacing spacing = new Spacing(
                PILLAR_GAP, MIN_CLUSTER_GAP, CLUSTER_GAP_PER_NEIGHBOUR_GAP, LABEL_GAP, ROW_STAGGER);
        return new DrawingSettings(VIEW_ANGLE_DEGREES, PILLAR_SIZES, spacing, LABELS,
                CANVAS, LINES, QUALITY, LIMITS, COLOR_COUNT);
    }
}
