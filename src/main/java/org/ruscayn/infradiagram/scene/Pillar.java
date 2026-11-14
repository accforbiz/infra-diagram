package org.ruscayn.infradiagram.scene;

/**
 * 화면에 놓인 기둥 하나다.
 *
 * base는 바닥 가운데의 화면 좌표, radius는 가로 반지름, height는 화면에서의 높이다.
 * 비스듬히 내려다보므로 바닥과 윗면의 원은 납작한 타원으로 보이고,
 * depthRadius가 그 타원의 세로 반지름이다. 각기둥은 이 타원 안에 꼭짓점을 둔다.
 */
public record Pillar(
        String elementId,
        int clusterIndex,
        ClusterStyle style,
        ScreenPoint base,
        double radius,
        double depthRadius,
        double height,
        boolean marked) {

    private static final double TOUCHING = 1e-9;

    public ScreenPoint topCenter() {
        return base.moved(0, -height);
    }

    public Box bounds() {
        return new Box(base.x() - radius, base.y() - height - depthRadius, 2 * radius, height + 2 * depthRadius);
    }

    public boolean inFrontOf(Pillar other) {
        return base.y() > other.base.y();
    }

    /**
     * 바닥 위에서 잰 두 기둥 가운데 사이의 거리를 돌려준다.
     *
     * 화면에서는 앞뒤 방향이 납작하게 눌려 있으므로, 세로 차이를 눌린 비율만큼 되돌려서 잰다.
     * 예: 눌린 비율이 0.5이고 화면에서 세로로 1 떨어져 있으면 바닥에서는 2 떨어져 있다.
     */
    public double groundDistanceTo(Pillar other) {
        double dx = base.x() - other.base.x();
        double dy = (base.y() - other.base.y()) * radius / depthRadius;
        return Math.hypot(dx, dy);
    }

    /**
     * 화면의 한 점이 이 기둥의 그림에 덮이는지 알려 준다.
     *
     * 기둥의 그림은 몸통 사각형, 바닥 타원, 윗면 타원을 합친 모양이다.
     * 경계에 딱 닿은 점은 덮인 것으로 보지 않는다.
     */
    public boolean covers(ScreenPoint point) {
        return inBody(point) || inEllipse(point, base) || inEllipse(point, topCenter());
    }

    public Pillar placed(Placement placement) {
        return new Pillar(elementId, clusterIndex, style, placement.apply(base),
                placement.apply(radius), placement.apply(depthRadius), placement.apply(height), marked);
    }

    private boolean inBody(ScreenPoint point) {
        boolean withinWidth = Math.abs(point.x() - base.x()) < radius - TOUCHING;
        boolean withinHeight = point.y() > base.y() - height + TOUCHING && point.y() < base.y() - TOUCHING;
        return withinWidth && withinHeight;
    }

    private boolean inEllipse(ScreenPoint point, ScreenPoint center) {
        double nx = (point.x() - center.x()) / radius;
        double ny = (point.y() - center.y()) / depthRadius;
        return nx * nx + ny * ny < 1 - TOUCHING;
    }
}
