package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.Placement;
import org.ruscayn.infradiagram.settings.CanvasSettings;

/**
 * 배치한 내용을 그림 크기에 맞춰 넣는 변환을 구한다.
 *
 * 가로와 세로에 같은 배율을 써서 모양이 찌그러지지 않게 하고, 여백 안에 들어가는 가장 큰 배율을 쓴다.
 * 다만 요소가 적을 때 기둥이 지나치게 커지지 않도록 배율에 상한을 둔다. 그다음 가운데로 옮긴다.
 *
 * 예: 그림이 1920 × 1080, 여백이 60, 배율 상한이 60이고 내용이 100 × 20이면
 * 가로 배율은 1800 / 100 = 18, 세로 배율은 960 / 20 = 48이므로 배율은 18이다.
 * 내용은 1800 × 360이 되고, 왼쪽에 60, 위쪽에 (1080 - 360) / 2 = 360을 띄운다.
 */
final class CanvasFit {

    private final CanvasSettings canvas;

    CanvasFit(CanvasSettings canvas) {
        this.canvas = canvas;
    }

    Placement fit(Box content) {
        double fitting = Math.min(canvas.innerWidth() / content.width(), canvas.innerHeight() / content.height());
        double scale = Math.min(canvas.maxScale(), fitting);
        double dx = (canvas.width() - content.width() * scale) / 2 - content.left() * scale;
        double dy = (canvas.height() - content.height() * scale) / 2 - content.top() * scale;
        return new Placement(scale, dx, dy);
    }
}
