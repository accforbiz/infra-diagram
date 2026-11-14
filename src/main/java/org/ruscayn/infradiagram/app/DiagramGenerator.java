package org.ruscayn.infradiagram.app;

import java.nio.file.Path;
import java.util.List;
import org.ruscayn.infradiagram.input.InfrastructureReader;
import org.ruscayn.infradiagram.input.InputRules;
import org.ruscayn.infradiagram.input.JsonInfrastructureReader;
import org.ruscayn.infradiagram.layout.SceneLayout;
import org.ruscayn.infradiagram.quality.QualityInspector;
import org.ruscayn.infradiagram.render.DiagramFont;
import org.ruscayn.infradiagram.render.PngSceneRenderer;
import org.ruscayn.infradiagram.render.SceneRenderer;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.DrawingSettings;

/**
 * 입력 파일 하나를 읽어 그림 파일 하나를 만든다. 읽기, 자리 계산, 품질 검사, 그리기를 차례로 한다.
 *
 * 품질 조건을 어긴 그림은 저장하지 않는다. 상한 안의 입력이라도 조합에 따라
 * 조건을 어길 수 있으므로, 저장하기 전에 한 번 더 검사해서 어떤 출력도 조건을 지키게 한다.
 */
public final class DiagramGenerator {

    private final InfrastructureReader reader;
    private final SceneLayout layout;
    private final QualityInspector inspector;
    private final SceneRenderer renderer;

    public DiagramGenerator(DrawingSettings settings) {
        DiagramFont font = new DiagramFont();
        InputRules rules = InputRules.standard(settings.limits()).and(new DisplayableLabelRule(font::canDisplay));
        this.reader = new JsonInfrastructureReader(rules);
        this.layout = new SceneLayout(settings, font);
        this.inspector = QualityInspector.standard(settings.canvas(), settings.quality());
        this.renderer = new PngSceneRenderer(font, settings.lines());
    }

    public void generate(Path input, Path output) {
        Scene scene = layout.arrange(reader.read(input));
        List<String> violations = inspector.inspect(scene);
        if (!violations.isEmpty()) {
            throw new PoorQualityException(violations);
        }
        renderer.render(scene, output);
    }
}
