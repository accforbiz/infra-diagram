package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.settings.PillarSizes;

final class PillarFactory {

    private final PillarSizes sizes;
    private final Projection projection;

    PillarFactory(PillarSizes sizes, Projection projection) {
        this.sizes = sizes;
        this.projection = projection;
    }

    Standing standing(Element element, GroundPoint spot) {
        double height = projection.screenHeight(sizes.height(element.scale()));
        return new Standing(spot, sizes.radius(element.scale()), height);
    }

    Pillar create(Element element, Standing standing, int clusterIndex, ClusterStyle style) {
        double radius = standing.radius();
        return new Pillar(element.id(), clusterIndex, style, projection.onGround(standing.spot()),
                radius, projection.depthRadius(radius), standing.height(), element.hasLabel());
    }
}
