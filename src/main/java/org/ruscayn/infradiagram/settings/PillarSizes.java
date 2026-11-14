package org.ruscayn.infradiagram.settings;

import org.ruscayn.infradiagram.model.Scale;

public record PillarSizes(double smallRadius, double mediumRadius, double largeRadius, double heightPerRadius) {

    public double radius(Scale scale) {
        return switch (scale) {
            case SMALL -> smallRadius;
            case MEDIUM -> mediumRadius;
            case LARGE -> largeRadius;
        };
    }

    public double height(Scale scale) {
        return radius(scale) * heightPerRadius;
    }
}
