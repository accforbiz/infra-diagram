#!/usr/bin/env bash
#
# 인프라 구성 그림을 그린다. 실행할 때마다 바뀐 코드가 있으면 먼저 컴파일한다.
#
#   ./run.sh                            examples 폴더의 입력을 모두 그려, 같은 이름의 PNG로 저장한다.
#   ./run.sh <input.json> <output.png>  입력 하나만 그린다. 상대 경로는 이 명령을 실행한 자리를 기준으로 한다.
#
# 끝난 상태: 0은 성공, 1은 입력이 잘못되었거나 그릴 수 없음, 2는 인자를 잘못 줌.
# 테스트는 여기서 실행하지 않는다. ./gradlew test 로 따로 실행한다.
set -euo pipefail

project_dir="$(cd "$(dirname "$0")" && pwd)"

if [ $# -ne 0 ] && [ $# -ne 2 ]; then
  echo "Usage: ./run.sh [<input.json> <output.png>]" >&2
  echo "Without arguments, draws every input in the examples folder." >&2
  exit 2
fi

# 컴파일한 뒤 실행에 필요한 파일을 build/install 아래에 모으고, Gradle이 고른 JDK 25의 위치를 파일에 적게 한다.
# JDK 25가 PC에 없으면 Gradle이 이때 내려받는다.
"$project_dir/gradlew" --project-dir "$project_dir" --quiet --console=plain installDist writeJavaHome
java_home="$(cat "$project_dir/build/java-home.txt")"

# PC에 깔린 자바가 25보다 낮아도 실행되도록, 위에서 알아낸 JDK 25로 프로그램을 직접 띄운다.
draw() {
  "$java_home/bin/java" -cp "$project_dir/build/install/infra-diagram/lib/*" \
    org.ruscayn.infradiagram.app.Main "$1" "$2"
}

if [ $# -eq 2 ]; then
  draw "$1" "$2"
else
  for input in "$project_dir"/examples/*.json; do
    draw "$input" "${input%.json}.png"
  done
fi
