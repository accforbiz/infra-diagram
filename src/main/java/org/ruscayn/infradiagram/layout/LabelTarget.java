package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.scene.Pillar;

record LabelTarget(Pillar pillar, String text) {

    double topHeight() {
        return pillar.topCenter().y();
    }

    LabelSide side() {
        return LabelSide.nearestTo(pillar);
    }
}
