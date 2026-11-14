package org.ruscayn.infradiagram.render;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.ScreenPoint;

/**
 * 원기둥을 그린다. 몸통을 먼저 그리고 윗면의 타원을 그 위에 덧그린다.
 *
 * 몸통의 둘레는 왼쪽 세로선, 바닥 타원의 앞쪽 절반, 오른쪽 세로선이다.
 * 바닥 타원의 뒤쪽 절반은 몸통에 가려 보이지 않으므로 그리지 않는다.
 * 몸통의 위쪽 가장자리는 윗면의 타원에 덮이므로 선을 긋지 않는다.
 */
final class CylinderPainter implements PillarPainter {

    private static final int WEST = 180;
    private static final int HALF_TURN = 180;

    @Override
    public void paint(Brush brush, Pillar pillar, Color topColor) {
        brush.fillAndOutline(body(pillar), Palette.SURFACE);
        brush.fillAndOutline(ellipse(pillar, pillar.topCenter()), topColor);
    }

    private Shape body(Pillar pillar) {
        double left = pillar.base().x() - pillar.radius();
        double top = pillar.topCenter().y();
        Path2D path = new Path2D.Double();
        path.moveTo(left, top);
        path.lineTo(left, pillar.base().y());
        path.append(frontHalfOfBase(pillar), true);
        path.lineTo(left + 2 * pillar.radius(), top);
        return path;
    }

    /**
     * 바닥 타원의 앞쪽 절반이다. 왼쪽 끝에서 출발해 아래쪽을 지나 오른쪽 끝으로 간다.
     * 자바의 호는 시계 반대 방향으로 그리며, 왼쪽 끝(180도)에서 반 바퀴를 돌면 화면의 아래쪽을 지난다.
     */
    private Shape frontHalfOfBase(Pillar pillar) {
        Ellipse2D base = ellipse(pillar, pillar.base());
        return new Arc2D.Double(base.getBounds2D(), WEST, HALF_TURN, Arc2D.OPEN);
    }

    private Ellipse2D ellipse(Pillar pillar, ScreenPoint center) {
        double left = center.x() - pillar.radius();
        double top = center.y() - pillar.depthRadius();
        return new Ellipse2D.Double(left, top, 2 * pillar.radius(), 2 * pillar.depthRadius());
    }
}
