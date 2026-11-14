package org.ruscayn.infradiagram.settings;

public record LabelSettings(double fontSize, double lineSpacing, double arrowLength, double dotRadius) {

    public double lineHeight() {
        return fontSize * lineSpacing;
    }
}
