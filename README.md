# SpringTest

Java と Spring Boot を学ぶための最小構成プロジェクトです。

## Stack

- Java 17
- Spring Boot 3.5.x
- Gradle Wrapper

## Run

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

Git Bash:

```bash
./gradlew bootRun
```

起動後の確認先:

- `http://localhost:8080/api/hello`

## Test

```bash
./gradlew test
```

## Build

```bash
./gradlew build
```

## GitHub に載せる流れ

1. ローカルで Git を初期化する
2. 空の GitHub リポジトリを作る
3. remote を追加して push する

例:

```bash
git init
git add .
git commit -m "Initial Spring Boot project"
git branch -M main
git remote add origin <your-repository-url>
git push -u origin main
```

## Next.js を後から追加する場合

今はバックエンド単体です。将来的にフロントを分けるなら、同じルート配下に `frontend/` を追加して Next.js を置く構成が扱いやすいです。