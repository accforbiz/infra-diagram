package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.scene.ScreenPoint;

/**
 * 입체를 평평한 그림으로 옮기는 평행 투영이다.
 *
 * 평행 투영은 멀리 있는 것도 같은 크기로 그린다. 기둥의 크기가 요소의 규모를 뜻하므로,
 * 멀다고 작게 그리면 규모를 잘못 읽게 되어 이 방식을 쓴다.
 *
 * 바닥의 점 (x, y)에서 y는 보는 사람 쪽으로 갈수록 커진다.
 * 비스듬히 내려다보면 앞뒤 방향(y)은 납작하게 눌려 보이고, 높이도 조금 줄어 보인다.
 * 내려다보는 각도가 30도이면 앞뒤 방향은 sin 30도 = 0.5배, 높이는 cos 30도 = 약 0.87배가 된다.
 * 예: 바닥의 점 (3, 4)는 화면의 (3, 2)가 되고, 높이 2인 기둥은 화면에서 약 1.73만큼 위로 솟는다.
 */
public record Projection(double depthScale, double heightScale) {

    public static Projection lookingDownAt(double degrees) {
        double radians = Math.toRadians(degrees);
        return new Projection(Math.sin(radians), Math.cos(radians));
    }

    public ScreenPoint onGround(GroundPoint point) {
        return new ScreenPoint(point.x(), point.y() * depthScale);
    }

    public double depthRadius(double radius) {
        return radius * depthScale;
    }

    public double screenHeight(double height) {
        return height * heightScale;
    }
}
