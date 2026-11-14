package org.ruscayn.infradiagram.layout;

public record GroundPoint(double x, double y) {

    public GroundPoint scaled(double factor) {
        return new GroundPoint(x * factor, y * factor);
    }
}
