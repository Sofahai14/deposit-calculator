# Калькулятор банковских вкладов (deposit-calculator)

Лабораторная работа № 2 «Инструменты и средства разработки сценариев в системах непрерывной интеграции».

Приложение рассчитывает итоговую сумму банковского вклада по простым и сложным процентам
(с капитализацией). Один и тот же Java-код собирается Apache Ant, Apache Maven и Gradle,
а GitHub Actions запускает сборку Maven автоматически.

## Структура

```
deposit-calculator/
├── src/main/java/by/student/deposit/
│   ├── model/Deposit.java                 - модель вклада
│   ├── service/DepositCalculator.java     - бизнес-логика расчёта
│   └── Main.java                          - точка входа
├── src/test/java/by/student/deposit/
│   └── DepositCalculatorTest.java         - 16 запусков тестов JUnit 5
├── lib/                                   - JAR JUnit 5 для Ant (ant libs)
├── build.xml                              - сценарий Ant
├── pom.xml                                - модель проекта Maven
├── checkstyle.xml                         - правила статического анализа
├── build.gradle, settings.gradle          - сценарий Gradle
├── gradlew, gradlew.bat, gradle/          - Gradle Wrapper (Gradle 9.8.2)
└── .github/workflows/maven.yml            - CI GitHub Actions
```

## Команды

| Инструмент | Команда | Результат |
|---|---|---|
| Ant | `ant -p` | список target'ов |
| Ant | `ant clean all` | тесты, HTML-отчёт `build/reports/html/index.html`, `build/jar/DepositCalculator.jar` |
| Ant | `ant run` | запуск приложения |
| Maven | `mvn clean verify` | тесты, `target/deposit-calculator-1.0.0-SNAPSHOT.jar`, JaCoCo, Checkstyle |
| Maven | `mvn surefire-report:report` | HTML-отчёт `target/reports/surefire.html` |
| Gradle | `.\gradlew.bat build` | сборка, отчёт `build/reports/tests/test/index.html` |
| Gradle | `.\gradlew.bat run` | запуск приложения |
