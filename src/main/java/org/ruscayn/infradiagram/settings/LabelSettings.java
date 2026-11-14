package org.ruscayn.infradiagram.settings;

public record LabelSettings(double fontSize, double lineSpacing, double arrowRoom, double dotRadius) {

    public double lineHeight() {
        return fontSize * lineSpacing;
    }
}
