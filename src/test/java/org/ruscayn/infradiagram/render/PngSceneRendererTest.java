package org.ruscayn.infradiagram.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.ruscayn.infradiagram.testing.TestInputs.cluster;
import static org.ruscayn.infradiagram.testing.TestInputs.element;
import static org.ruscayn.infradiagram.testing.TestInputs.infrastructure;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ruscayn.infradiagram.layout.SceneLayout;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.DrawingSettings;
import org.ruscayn.infradiagram.testing.TestInputs;

class PngSceneRendererTest {

    private static final int WHITE = 0xFFFFFF;

    private final DrawingSettings settings = DrawingSettings.standard();
    private final DiagramFont font = new DiagramFont();

    @Test
    @DisplayName("그림 파일은 가로 1920, 세로 1080인 PNG이고 흰 바탕에 무언가 그려져 있다")
    void writesPngOfCanvasSize(@TempDir Path directory) throws IOException {
        Path output = directory.resolve("nested").resolve("picture.png");

        new PngSceneRenderer(font, settings.lines()).render(sceneWithKoreanLabel(), output);

        BufferedImage image = ImageIO.read(output.toFile());
        assertEquals(1920, image.getWidth());
        assertEquals(1080, image.getHeight());
        assertTrue(hasInk(image));
    }

    private Scene sceneWithKoreanLabel() {
        String json = infrastructure(cluster(element("a", "\"label\":\"데이터 레이크 ABC\""), element("b")));
        return new SceneLayout(settings, font).arrange(TestInputs.read(json));
    }

    private boolean hasInk(BufferedImage image) {
        return IntStream.range(0, image.getWidth())
                .anyMatch(x -> IntStream.range(0, image.getHeight()).anyMatch(y -> (image.getRGB(x, y) & WHITE) != WHITE));
    }
}
