package org.ruscayn.infradiagram.settings;

public record CanvasSettings(int width, int height, double margin, double maxScale) {

    public double innerWidth() {
        return width - 2 * margin;
    }

    public double innerHeight() {
        return height - 2 * margin;
    }
}
