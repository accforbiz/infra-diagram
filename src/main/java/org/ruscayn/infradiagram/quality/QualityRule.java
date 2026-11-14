package org.ruscayn.infradiagram.quality;

import java.util.List;
import org.ruscayn.infradiagram.scene.Scene;

public interface QualityRule {

    List<String> violations(Scene scene);
}
