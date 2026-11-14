package org.ruscayn.infradiagram.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.ScreenPoint;
import org.ruscayn.infradiagram.settings.LineSettings;

/**
 * 글자 하나를 그린다. 화살표, 글자, 색 점의 순서로 그린다.
 */
final class LabelPainter {

    private static final double HEAD_LENGTH_PER_FONT_SIZE = 0.45;
    private static final double HEAD_SPREAD_RADIANS = Math.toRadians(28);
    private static final double BACKING_PADDING_PER_HEIGHT = 0.25;

    private final DiagramFont font;
    private final BasicStroke arrowStroke;

    LabelPainter(DiagramFont font, LineSettings lines) {
        this.font = font;
        this.arrowStroke = new BasicStroke((float) lines.arrowLine(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    void paint(Graphics2D graphics, LabelMark label, Color dotColor) {
        clearBehind(graphics, label.captionBox());
        paintArrow(graphics, label);
        paintText(graphics, label);
        graphics.setColor(dotColor);
        double radius = label.dotRadius();
        ScreenPoint dot = label.dot();
        graphics.fill(new Ellipse2D.Double(dot.x() - radius, dot.y() - radius, 2 * radius, 2 * radius));
    }

    /**
     * 글자가 놓일 자리를 바탕색으로 먼저 덮는다. 점선이 글자 위를 지나가면 읽기 어렵기 때문이다.
     * 글자는 기둥과 겹치지 않는 자리에 놓이므로 기둥을 덮지는 않는다.
     */
    private void clearBehind(Graphics2D graphics, Box bounds) {
        double padding = bounds.height() * BACKING_PADDING_PER_HEIGHT;
        graphics.setColor(Palette.BACKGROUND);
        graphics.fill(new Rectangle2D.Double(bounds.left() - padding, bounds.top() - padding,
                bounds.width() + 2 * padding, bounds.height() + 2 * padding));
    }

    private void paintText(Graphics2D graphics, LabelMark label) {
        Box box = label.textBox();
        double baseline = box.centerY() + font.baselineDrop(label.fontSize());
        graphics.setColor(Palette.TEXT);
        graphics.setFont(font.at(label.fontSize()));
        graphics.drawString(label.text(), (float) box.left(), (float) baseline);
    }

    /**
     * 화살표를 그린다. 몸통 선을 긋고, 머리 끝에서 뒤쪽으로 벌어진 짧은 선 두 개를 긋는다.
     * 머리의 크기는 화살표의 길이가 아니라 글자 크기에 맞춘다. 화살표가 길어져도 머리가 커지지 않게 하기 위해서다.
     * 예: 화살표가 왼쪽(180도)을 향하면 머리의 두 선은 머리 끝에서 28도와 -28도 방향으로 뻗는다.
     */
    private void paintArrow(Graphics2D graphics, LabelMark label) {
        ScreenPoint tail = label.arrowTail();
        ScreenPoint head = label.arrowHead();
        graphics.setColor(Palette.TEXT);
        graphics.setStroke(arrowStroke);
        graphics.draw(line(tail, head));
        double backward = Math.atan2(tail.y() - head.y(), tail.x() - head.x());
        double length = label.fontSize() * HEAD_LENGTH_PER_FONT_SIZE;
        graphics.draw(line(head, barb(head, backward + HEAD_SPREAD_RADIANS, length)));
        graphics.draw(line(head, barb(head, backward - HEAD_SPREAD_RADIANS, length)));
    }

    private ScreenPoint barb(ScreenPoint head, double direction, double length) {
        return head.moved(Math.cos(direction) * length, Math.sin(direction) * length);
    }

    private Line2D line(ScreenPoint from, ScreenPoint to) {
        return new Line2D.Double(from.x(), from.y(), to.x(), to.y());
    }
}
