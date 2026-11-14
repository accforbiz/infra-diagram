package org.ruscayn.infradiagram.scene;

/**
 * 배치를 마친 그림을 실제 그림 크기에 맞춰 옮기는 변환이다.
 *
 * 먼저 모든 길이에 scale을 곱하고, 그다음 (dx, dy)만큼 옮긴다.
 * 예: scale이 40, dx가 100이면 가로 좌표 2.5는 2.5 × 40 + 100 = 200이 된다.
 */
public record Placement(double scale, double dx, double dy) {

    public static Placement shift(double dx, double dy) {
        return new Placement(1, dx, dy);
    }

    public ScreenPoint apply(ScreenPoint point) {
        return new ScreenPoint(point.x() * scale + dx, point.y() * scale + dy);
    }

    public Box apply(Box box) {
        return new Box(box.left() * scale + dx, box.top() * scale + dy, box.width() * scale, box.height() * scale);
    }

    public double apply(double length) {
        return length * scale;
    }
}
