package org.ruscayn.infradiagram.render;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import javax.imageio.ImageIO;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.PillarShape;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.LineSettings;

/**
 * 그림을 PNG 파일로 그린다. 점선, 뒤쪽 기둥부터 앞쪽 기둥, 글자의 순서로 그린다.
 *
 * 점선을 먼저 그리므로 점선은 기둥 뒤로 지나가고, 글자를 마지막에 그리므로 글자는 가려지지 않는다.
 */
public final class PngSceneRenderer implements SceneRenderer {

    private static final double TRIANGLE_FIRST_CORNER = 70;
    private static final double HEXAGON_FIRST_CORNER = 10;

    private final Palette palette = new Palette();
    private final Map<PillarShape, PillarPainter> pillarPainters = Map.of(
            PillarShape.TRIANGULAR_PRISM, new PrismPainter(3, TRIANGLE_FIRST_CORNER),
            PillarShape.HEXAGONAL_PRISM, new PrismPainter(6, HEXAGON_FIRST_CORNER),
            PillarShape.CYLINDER, new CylinderPainter());
    private final LinkPainter linkPainter;
    private final LabelPainter labelPainter;
    private final double pillarLine;

    public PngSceneRenderer(DiagramFont font, LineSettings lines) {
        this.linkPainter = new LinkPainter(lines);
        this.labelPainter = new LabelPainter(font, lines);
        this.pillarLine = lines.pillarLine();
    }

    @Override
    public void render(Scene scene, Path output) {
        int width = (int) Math.round(scene.width());
        int height = (int) Math.round(scene.height());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        paint(scene, graphics);
        graphics.dispose();
        write(image, output);
    }

    private void paint(Scene scene, Graphics2D graphics) {
        smoothEdges(graphics);
        graphics.setColor(Palette.BACKGROUND);
        graphics.fillRect(0, 0, (int) Math.round(scene.width()), (int) Math.round(scene.height()));
        scene.links().forEach(link -> linkPainter.paint(graphics, link));
        Brush brush = new Brush(graphics, pillarLine);
        scene.backToFront().forEach(pillar -> paintPillar(brush, pillar));
        scene.labels().forEach(label -> labelPainter.paint(graphics, label, palette.of(label.color())));
    }

    /**
     * 선과 글자의 가장자리를 부드럽게 그리도록 한다. 이 설정이 없으면 비스듬한 선이 계단처럼 보인다.
     */
    private void smoothEdges(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private void paintPillar(Brush brush, Pillar pillar) {
        ClusterStyle style = pillar.style();
        pillarPainters.get(style.shape()).paint(brush, pillar, palette.of(style.color()));
    }

    private void write(BufferedImage image, Path output) {
        try {
            Path folder = output.toAbsolutePath().getParent();
            Files.createDirectories(folder);
            ImageIO.write(image, "png", output.toFile());
        } catch (IOException error) {
            throw new UncheckedIOException("Cannot write the picture to '%s'.".formatted(output), error);
        }
    }
}
