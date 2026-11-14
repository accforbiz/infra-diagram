package org.ruscayn.infradiagram.render;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.io.IOException;
import java.io.InputStream;
import org.ruscayn.infradiagram.layout.TextMeasurer;

/**
 * 프로그램과 함께 둔 글꼴이다. 어느 PC에서나 같은 글꼴로 글자의 폭을 재고 글자를 그린다.
 *
 * 폭은 큰 기준 크기에서 잰 뒤 원하는 크기로 비례해 줄인다.
 * 자리 계산에서는 글자 크기가 0.8처럼 아주 작은 수여서, 그 크기로 직접 재면 오차가 커지기 때문이다.
 * 예: 기준 크기 100에서 폭이 420인 글자는 크기 0.8에서 420 × 0.8 / 100 = 3.36이다.
 */
public final class DiagramFont implements TextMeasurer {

    private static final String RESOURCE = "/fonts/Pretendard-Medium.otf";
    private static final float REFERENCE_SIZE = 100f;
    private static final FontRenderContext CONTEXT = new FontRenderContext(null, true, true);

    private final Font base;

    public DiagramFont() {
        this.base = load();
    }

    @Override
    public double width(String text, double fontSize) {
        double referenceWidth = base.deriveFont(REFERENCE_SIZE).getStringBounds(text, CONTEXT).getWidth();
        return referenceWidth * fontSize / REFERENCE_SIZE;
    }

    public boolean canDisplay(String text) {
        return base.canDisplayUpTo(text) == -1;
    }

    Font at(double fontSize) {
        return base.deriveFont((float) fontSize);
    }

    /**
     * 글자를 줄의 세로 가운데에 놓으려면 기준선을 가운데에서 얼마나 내려야 하는지 돌려준다.
     * 글자는 기준선 위로 ascent만큼 솟고 아래로 descent만큼 내려가므로, 그 가운데는
     * 기준선에서 (ascent - descent) / 2만큼 위에 있다.
     */
    double baselineDrop(double fontSize) {
        LineMetrics metrics = at(fontSize).getLineMetrics("A", CONTEXT);
        return (metrics.getAscent() - metrics.getDescent()) / 2;
    }

    private static Font load() {
        try (InputStream stream = DiagramFont.class.getResourceAsStream(RESOURCE)) {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (IOException | FontFormatException | NullPointerException error) {
            throw new IllegalStateException("Cannot load the bundled font " + RESOURCE, error);
        }
    }
}
