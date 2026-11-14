package org.ruscayn.infradiagram.layout;

import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.ClusterSpacing;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.CanvasSettings;
import org.ruscayn.infradiagram.settings.DrawingSettings;
import org.ruscayn.infradiagram.settings.Spacing;

/**
 * 인프라 구성을 그림으로 옮긴다. 무리마다 덩어리를 만들고, 덩어리를 줄에 놓고, 그림 크기에 맞춰 넣는다.
 */
public final class SceneLayout {

    private final ClusterLayout clusterLayout;
    private final Spacing spacing;
    private final CanvasSettings canvas;
    private final int colorCount;
    private final double depthScale;

    public SceneLayout(DrawingSettings settings, TextMeasurer measurer) {
        this.depthScale = Projection.lookingDownAt(settings.viewAngleDegrees()).depthScale();
        this.clusterLayout = new ClusterLayoutFactory(settings, measurer).create();
        this.spacing = settings.spacing();
        this.canvas = settings.canvas();
        this.colorCount = settings.colorCount();
    }

    public Scene arrange(Infrastructure infrastructure) {
        List<ClusterBlock> blocks = placed(blocks(infrastructure.clusters()));
        List<Pillar> pillars = pillarsOf(blocks);
        List<LabelMark> labels = blocks.stream().flatMap(block -> block.labels().stream()).toList();
        Scene loose = new Scene(0, 0, pillars, new LinkLines(pillars).between(infrastructure.links()), labels);
        return loose.placed(new CanvasFit(canvas).fit(loose.contentBounds()), canvas.width(), canvas.height());
    }

    private List<ClusterBlock> blocks(List<Cluster> clusters) {
        return IntStream.range(0, clusters.size())
                .mapToObj(index -> clusterLayout.arrange(
                        clusters.get(index), index, ClusterStyle.forCluster(index, colorCount)))
                .toList();
    }

    private List<ClusterBlock> placed(List<ClusterBlock> blocks) {
        List<Box> bounds = blocks.stream().map(ClusterBlock::bounds).toList();
        double gap = gapBetween(blocks);
        List<Box> spots = new RowPacker(gap, gap * depthScale, spacing.rowStagger(), canvas).pack(bounds);
        return IntStream.range(0, blocks.size()).mapToObj(index -> blocks.get(index).movedTo(spots.get(index))).toList();
    }

    /**
     * 무리 사이의 틈을 정한다. 무리 안에서 가장 넓은 틈에 배수를 곱하되, 정해 둔 최솟값보다 좁히지 않는다.
     *
     * 배수가 1보다 크므로 서로 다른 무리는 무리 안의 기둥 사이보다 항상 넓게 떨어진다. (품질 조건 3)
     * 예: 무리 안의 틈이 가장 넓은 곳이 0.8이고 배수가 2이면 무리 사이의 틈은 1.6이다.
     * 최솟값이 2.0이면 2.0을 쓴다.
     *
     * 줄 사이의 틈은 화면에서 이 값에 앞뒤로 눌린 비율을 곱해 쓴다. 화면의 세로는 앞뒤 방향이
     * 눌려 보이는 것이어서, 그렇게 해야 바닥에서 잰 틈이 가로 틈과 같아진다.
     */
    private double gapBetween(List<ClusterBlock> blocks) {
        double widestInside = ClusterSpacing.of(pillarsOf(blocks)).within();
        return Math.max(spacing.minClusterGap(), spacing.clusterGapPerNeighbourGap() * widestInside);
    }

    private List<Pillar> pillarsOf(List<ClusterBlock> blocks) {
        return blocks.stream().flatMap(block -> block.pillars().stream()).toList();
    }
}
