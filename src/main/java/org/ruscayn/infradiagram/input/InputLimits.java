package org.ruscayn.infradiagram.input;

/**
 * 받을 수 있는 입력의 크기다.
 *
 * maxMultiElementClusters는 요소가 둘 이상인 무리의 수의 상한이다. 그런 무리는 저마다 다른 색으로
 * 그리는데, 서로 구별되는 색의 수에 한계가 있어서 따로 상한을 둔다. 요소가 하나뿐인 무리는 세지 않는다.
 *
 * maxLabelsPerCluster는 한 무리에서 글자를 붙일 수 있는 요소의 수의 상한이다. 글자는 무리의 양옆에
 * 쌓여 무리의 폭을 넓히므로, 글자가 많으면 그림 전체가 줄어들어 글자를 읽을 수 없게 된다.
 */
public record InputLimits(
        int maxClusters,
        int maxMultiElementClusters,
        int maxElementsPerCluster,
        int maxLabelsPerCluster,
        int maxLabelLength) {
}
