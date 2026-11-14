package org.ruscayn.infradiagram.render;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.ScreenPoint;

/**
 * 각기둥(삼각기둥, 육각기둥)을 그린다. 보는 사람 쪽을 향한 옆면을 그리고 윗면을 덧그린다.
 *
 * 꼭짓점은 바닥의 원 위에 같은 간격으로 놓는다. 각도는 오른쪽이 0도이고,
 * 90도가 보는 사람 쪽(화면의 아래쪽)이다. 바닥의 원은 화면에서 납작한 타원으로 보이므로
 * 꼭짓점의 세로 좌표에는 세로 반지름을 쓴다.
 * 예: 변이 3개이고 첫 꼭짓점이 70도이면 꼭짓점은 70도, 190도, 310도에 놓인다.
 *
 * 옆면이 보는 사람 쪽을 향하는지는 그 면의 가운데 방향으로 판단한다.
 * 가운데 방향이 0도와 180도 사이이면(sin 값이 양수이면) 앞을 향한 면이다.
 * 예: 위의 삼각기둥에서 70도와 190도 사이의 면은 가운데가 130도이므로 보이고,
 * 190도와 310도 사이의 면은 가운데가 250도이므로 뒤를 향해 보이지 않는다.
 */
final class PrismPainter implements PillarPainter {

    private static final double EDGE_ON = 1e-9;

    private final int sides;
    private final double firstCornerDegrees;

    PrismPainter(int sides, double firstCornerDegrees) {
        this.sides = sides;
        this.firstCornerDegrees = firstCornerDegrees;
    }

    @Override
    public void paint(Brush brush, Pillar pillar, Color topColor) {
        IntStream.range(0, sides)
                .filter(this::facesViewer)
                .forEach(side -> brush.fillAndOutline(sideFace(pillar, side), Palette.SURFACE));
        brush.fillAndOutline(topFace(pillar), topColor);
    }

    private boolean facesViewer(int side) {
        return Math.sin(angle(side + 0.5)) > EDGE_ON;
    }

    private double angle(double corner) {
        return Math.toRadians(firstCornerDegrees + corner * 360.0 / sides);
    }

    private ScreenPoint corner(Pillar pillar, int index, double lift) {
        double dx = pillar.radius() * Math.cos(angle(index));
        double dy = pillar.depthRadius() * Math.sin(angle(index));
        return pillar.base().moved(dx, dy - lift);
    }

    private Shape sideFace(Pillar pillar, int side) {
        double lift = pillar.height();
        return polygon(List.of(corner(pillar, side, 0), corner(pillar, side + 1, 0),
                corner(pillar, side + 1, lift), corner(pillar, side, lift)));
    }

    private Shape topFace(Pillar pillar) {
        return polygon(IntStream.range(0, sides).mapToObj(index -> corner(pillar, index, pillar.height())).toList());
    }

    private Shape polygon(List<ScreenPoint> corners) {
        Path2D path = new Path2D.Double();
        path.moveTo(corners.getFirst().x(), corners.getFirst().y());
        corners.stream().skip(1).forEach(corner -> path.lineTo(corner.x(), corner.y()));
        path.closePath();
        return path;
    }
}
