package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.Pillar;

enum LabelSide {
    LEFT(-1),
    RIGHT(1);

    private final int direction;

    LabelSide(int direction) {
        this.direction = direction;
    }

    static LabelSide nearestTo(Pillar pillar) {
        return pillar.base().x() < 0 ? LEFT : RIGHT;
    }

    int direction() {
        return direction;
    }

    double edgeOf(Box area) {
        return this == LEFT ? area.left() : area.right();
    }

    double outerEdgeOf(Box textBox) {
        return this == LEFT ? textBox.left() : textBox.right();
    }
}
