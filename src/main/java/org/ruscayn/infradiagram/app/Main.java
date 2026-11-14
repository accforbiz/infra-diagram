package org.ruscayn.infradiagram.app;

public final class Main {

    private Main() {
    }

    public static void main(String[] arguments) {
        // 화면이 없는 환경에서도 그림을 그릴 수 있게 한다. 그리기 기능을 쓰기 전에 정해야 한다.
        System.setProperty("java.awt.headless", "true");
        System.exit(new DiagramCommand(System.out, System.err).run(arguments));
    }
}
