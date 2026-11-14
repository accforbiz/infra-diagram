package org.ruscayn.infradiagram.render;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import org.ruscayn.infradiagram.scene.LinkLine;
import org.ruscayn.infradiagram.settings.LineSettings;

final class LinkPainter {

    private final BasicStroke dashed;

    LinkPainter(LineSettings lines) {
        float[] pattern = {(float) lines.linkDash(), (float) lines.linkDash()};
        this.dashed = new BasicStroke((float) lines.linkLine(),
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f, pattern, 0f);
    }

    void paint(Graphics2D graphics, LinkLine link) {
        graphics.setColor(Palette.LINK);
        graphics.setStroke(dashed);
        graphics.draw(new Line2D.Double(link.from().x(), link.from().y(), link.to().x(), link.to().y()));
    }
}
