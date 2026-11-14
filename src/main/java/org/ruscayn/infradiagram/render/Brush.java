package org.ruscayn.infradiagram.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * 면을 칠하고 그 둘레에 선을 긋는 붓이다. 기둥의 모든 면을 같은 선으로 그리게 한다.
 */
final class Brush {

    private final Graphics2D graphics;
    private final BasicStroke line;

    Brush(Graphics2D graphics, double lineWidth) {
        this.graphics = graphics;
        this.line = new BasicStroke((float) lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    void fillAndOutline(Shape shape, Color fill) {
        graphics.setColor(fill);
        graphics.fill(shape);
        graphics.setColor(Palette.LINE);
        graphics.setStroke(line);
        graphics.draw(shape);
    }
}
