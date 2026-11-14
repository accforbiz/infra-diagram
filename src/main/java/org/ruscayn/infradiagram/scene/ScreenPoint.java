package org.ruscayn.infradiagram.scene;

public record ScreenPoint(double x, double y) {

    public ScreenPoint moved(double dx, double dy) {
        return new ScreenPoint(x + dx, y + dy);
    }
}
