package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.settings.DrawingSettings;
import org.ruscayn.infradiagram.settings.LabelSettings;
import org.ruscayn.infradiagram.settings.Spacing;

/**
 * 설정값으로 무리 하나를 놓는 부품을 조립한다.
 */
final class ClusterLayoutFactory {

    private final DrawingSettings settings;
    private final TextMeasurer measurer;

    ClusterLayoutFactory(DrawingSettings settings, TextMeasurer measurer) {
        this.settings = settings;
        this.measurer = measurer;
    }

    ClusterLayout create() {
        Projection projection = Projection.lookingDownAt(settings.viewAngleDegrees());
        PillarFactory pillarFactory = new PillarFactory(settings.pillarSizes(), projection);
        return new ClusterLayout(pitch(projection), pillarFactory, labelPlacer());
    }

    private ClusterPitch pitch(Projection projection) {
        double pillarGap = settings.spacing().pillarGap();
        return new ClusterPitch(new TopClearance(projection.depthScale(), pillarGap), pillarGap);
    }

    private LabelPlacer labelPlacer() {
        LabelSettings labels = settings.labels();
        Spacing spacing = settings.spacing();
        return new LabelPlacer(new LabelBuilder(labels, measurer, spacing.labelGap()), labels.lineHeight());
    }
}
