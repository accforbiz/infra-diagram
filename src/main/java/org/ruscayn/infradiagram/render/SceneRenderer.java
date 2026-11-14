package org.ruscayn.infradiagram.render;

import java.nio.file.Path;
import org.ruscayn.infradiagram.scene.Scene;

public interface SceneRenderer {

    void render(Scene scene, Path output);
}
