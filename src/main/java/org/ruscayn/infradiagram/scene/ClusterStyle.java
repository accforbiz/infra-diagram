package org.ruscayn.infradiagram.scene;

public record ClusterStyle(PillarShape shape, int colorIndex) {

    /**
     * 무리의 순서에 따라 모양과 색을 각각 돌려 쓴다.
     *
     * 모양은 3가지이므로 3개마다, 색은 colorCount개마다 처음으로 돌아온다.
     * colorCount가 3과 공약수가 없으면(예: 4) 두 주기가 어긋나서,
     * 무리가 3 × colorCount개(예: 12개)가 될 때까지 모양과 색의 짝이 겹치지 않는다.
     * 예: colorCount가 4일 때 0번 무리는 (삼각기둥, 0번 색), 3번 무리는 (삼각기둥, 3번 색),
     * 4번 무리는 (육각기둥, 0번 색)이다. 모양이나 색 하나는 같아도 짝은 다르다.
     */
    public static ClusterStyle forCluster(int clusterIndex, int colorCount) {
        PillarShape[] shapes = PillarShape.values();
        return new ClusterStyle(shapes[clusterIndex % shapes.length], clusterIndex % colorCount);
    }
}
