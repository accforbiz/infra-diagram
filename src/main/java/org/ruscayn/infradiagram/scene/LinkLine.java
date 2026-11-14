package org.ruscayn.infradiagram.scene;

public record LinkLine(ScreenPoint from, ScreenPoint to) {

    public LinkLine placed(Placement placement) {
        return new LinkLine(placement.apply(from), placement.apply(to));
    }
}
