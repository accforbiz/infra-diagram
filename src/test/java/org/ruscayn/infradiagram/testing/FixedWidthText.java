package org.ruscayn.infradiagram.testing;

import org.ruscayn.infradiagram.layout.TextMeasurer;

/**
 * 글자 하나의 폭을 글자 크기의 절반으로 치는 가짜 폭 재기 도구다.
 * 글꼴과 운영체제에 따라 값이 달라지지 않게 해서, 자리 계산 테스트의 정답을 손으로 구할 수 있게 한다.
 */
public final class FixedWidthText implements TextMeasurer {

    @Override
    public double width(String text, double fontSize) {
        return text.length() * fontSize * 0.5;
    }
}
