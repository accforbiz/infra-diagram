package org.ruscayn.infradiagram.input;

/**
 * 받을 수 있는 입력의 크기다.
 *
 * maxMultiElementClusters는 요소가 둘 이상인 무리의 수의 상한이다. 그런 무리는 저마다 다른 색으로
 * 그리는데, 서로 구별되는 색의 수에 한계가 있어서 따로 상한을 둔다. 요소가 하나뿐인 무리는 세지 않는다.
 */
public record InputLimits(int maxClusters, int maxMultiElementClusters, int maxElementsPerCluster, int maxLabelLength) {
}
