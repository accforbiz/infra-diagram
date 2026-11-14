package org.ruscayn.infradiagram.scene;

public record ScreenPoint(double x, double y) {

    public ScreenPoint moved(double dx, double dy) {
        return new ScreenPoint(x + dx, y + dy);
    }

    public ScreenPoint toward(ScreenPoint target, double distance) {
        double length = Math.hypot(target.x - x, target.y - y);
        if (length == 0) {
            return this;
        }
        return moved((target.x - x) / length * distance, (target.y - y) / length * distance);
    }
}
