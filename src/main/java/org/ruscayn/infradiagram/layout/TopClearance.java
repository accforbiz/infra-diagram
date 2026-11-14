package org.ruscayn.infradiagram.layout;

/**
 * 앞 기둥이 뒤 기둥의 윗면을 가리지 않는지 판정한다. (품질 조건 2)
 *
 * 화면에서는 앞뒤 방향이 납작하게 눌려 있어 계산이 번거롭다. 그래서 눌린 비율만큼 되돌려서,
 * 바닥을 바로 위에서 내려다본 평면에서 생각한다. 이 평면에서는 다음과 같이 보인다.
 * - 기둥의 윗면은 바닥의 원을 뒤쪽으로 "높이 / 눌린 비율"만큼 옮긴 원이다.
 * - 기둥 전체의 그림은 바닥의 원에서 윗면의 원까지 원이 지나간 자리, 곧 알약 모양이다.
 *
 * 뒤 기둥의 윗면(원)이 앞 기둥의 그림(알약)과 겹치지 않으면 윗면이 다 보인다.
 * 원과 알약이 겹치지 않는다는 것은, 뒤 기둥 윗면의 가운데에서 앞 기둥의 가운데 선까지의
 * 거리가 두 반지름의 합 이상이라는 뜻이다.
 *
 * 그 거리는 뒤 기둥 윗면의 가운데가 어디에 있느냐에 따라 달라진다.
 * - 앞 기둥 윗면의 가운데보다 더 뒤에 있으면: 두 윗면의 가운데 사이의 거리다.
 * - 앞 기둥의 몸통과 나란한 깊이에 있으면: 가로로 떨어진 거리다.
 *
 * 예: 높이가 같은 두 기둥은 윗면이 바닥을 똑같이 옮긴 것이므로, 바닥이 겹치지 않으면 윗면도 겹치지 않는다.
 * 예: 눌린 비율이 0.5이고 앞 기둥이 화면에서 1만큼 더 높으면, 앞 기둥의 윗면은 평면에서 2만큼 더 뒤로 간다.
 * 뒤 기둥이 뒤로 3, 옆으로 1 떨어져 있다면 두 윗면의 가운데는 뒤로 3 - 2 = 1, 옆으로 1 떨어져
 * 거리가 약 1.41이다. 두 반지름의 합이 2이면 겹치므로 더 띄워야 한다.
 */
final class TopClearance {

    private final double depthScale;
    private final double gap;

    TopClearance(double depthScale, double gap) {
        this.depthScale = depthScale;
        this.gap = gap;
    }

    boolean leavesTopVisible(Standing front, Standing back) {
        double across = Math.abs(front.spot().x() - back.spot().x());
        double behind = front.spot().y() - back.spot().y();
        double frontTopFurtherBack = (front.height() - back.height()) / depthScale;
        double beyondFrontTop = behind - frontTopFurtherBack;
        double distance = beyondFrontTop >= 0 ? Math.hypot(across, beyondFrontTop) : across;
        return distance >= front.radius() + back.radius() + gap;
    }
}
