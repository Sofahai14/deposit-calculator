package by.student.deposit.service;

import by.student.deposit.model.Deposit;   // подключаем модель вклада из другого пакета

/**
 * СЕРВИС РАСЧЁТА ВКЛАДА (Листинг 2 методички) - бизнес-логика приложения.
 *
 * В классе нет кода Ant/Maven/Gradle, поэтому один и тот же класс
 * можно собирать разными build-системами.
 */
public class DepositCalculator {

    /**
     * Простые проценты (без капитализации):
     * итог = сумма * (1 + ставка * срок / 100).
     * Пример: 1000 * (1 + 10 * 3 / 100) = 1300.
     */
    public double calculateSimple(Deposit deposit) {
        return deposit.getPrincipal()
                * (1 + deposit.getAnnualRate() * deposit.getYears() / 100.0);
    }

    /**
     * Сложные проценты (с капитализацией - проценты каждый год прибавляются к сумме):
     * итог = сумма * (1 + ставка / 100) ^ срок.
     * Пример: 1000 * 1.1^3 = 1331.
     * Math.pow(a, b) - возведение a в степень b.
     */
    public double calculateCompound(Deposit deposit) {
        return deposit.getPrincipal()
                * Math.pow(1 + deposit.getAnnualRate() / 100.0, deposit.getYears());
    }

    /**
     * Выбор формулы: условие ? если_да : если_нет.
     * Есть капитализация - сложные проценты, нет - простые.
     */
    public double calculateTotal(Deposit deposit) {
        return deposit.isCapitalization()
                ? calculateCompound(deposit)
                : calculateSimple(deposit);
    }

    /**
     * Сумма начисленных процентов = итоговая сумма - первоначальная сумма.
     */
    public double calculateInterest(Deposit deposit) {
        return calculateTotal(deposit) - deposit.getPrincipal();
    }

    /**
     * Текстовый отчёт по вкладу.
     * StringBuilder собирает длинный текст по частям;
     * String.format подставляет значения в шаблон:
     * %.2f - число с 2 знаками после запятой, %d - целое, %s - строка,
     * %% - знак процента, %n - перевод строки.
     */
    public String buildReport(Deposit deposit) {
        StringBuilder sb = new StringBuilder();
        // В методичке строка "=== Отчёт по вкладу ===" разорвана переносом
        // и не компилируется; здесь перевод строки задан через %n.
        sb.append(String.format("=== Отчёт по вкладу ===%n"));
        sb.append(String.format("Начальная сумма: %.2f BYN%n", deposit.getPrincipal()));
        sb.append(String.format("Ставка: %.2f%%%n", deposit.getAnnualRate()));
        sb.append(String.format("Срок: %d лет%n", deposit.getYears()));
        sb.append(String.format("Капитализация: %s%n", deposit.isCapitalization() ? "да" : "нет"));
        sb.append(String.format("Итоговая сумма: %.2f BYN%n", calculateTotal(deposit)));
        sb.append(String.format("Начислено процентов: %.2f BYN%n", calculateInterest(deposit)));
        return sb.toString();
    }
}
