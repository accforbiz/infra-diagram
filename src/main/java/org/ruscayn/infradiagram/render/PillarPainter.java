package org.ruscayn.infradiagram.render;

import java.awt.Color;
import org.ruscayn.infradiagram.scene.Pillar;

interface PillarPainter {

    void paint(Brush brush, Pillar pillar, Color topColor);
}
