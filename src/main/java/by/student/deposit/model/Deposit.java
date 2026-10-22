// package - "папка-пакет", в которой лежит класс:
// by.student.deposit.model = src/main/java/by/student/deposit/model
package by.student.deposit.model;

/**
 * МОДЕЛЬ БАНКОВСКОГО ВКЛАДА (Листинг 1 методички).
 *
 * Модель только ХРАНИТ данные одного вклада и проверяет их правильность.
 * Класс не зависит от Ant, Maven или Gradle - поэтому любой инструмент
 * сборки работает с одним и тем же исходным кодом.
 */
public class Deposit {

    // Поля (private - доступны только внутри класса, final - задаются один раз).
    private final double principal;       // Первоначальная сумма, BYN
    private final double annualRate;      // Годовая ставка, %
    private final int years;              // Срок вклада, лет
    private final boolean capitalization; // Есть ли капитализация процентов

    /**
     * Конструктор вызывается командой new Deposit(...).
     * Если данные неправильные - выбрасывается исключение
     * IllegalArgumentException и объект НЕ создаётся.
     */
    public Deposit(double principal, double annualRate, int years, boolean capitalization) {
        if (principal <= 0) {                       // сумма должна быть положительной
            throw new IllegalArgumentException("Сумма вклада должна быть > 0");
        }
        if (annualRate < 0) {                       // ставка 0 допустима, отрицательная - нет
            throw new IllegalArgumentException("Ставка не может быть отрицательной");
        }
        if (years <= 0) {                           // срок минимум 1 год
            throw new IllegalArgumentException("Срок должен быть > 0");
        }
        // Проверки пройдены - сохраняем значения в поля объекта.
        this.principal = principal;
        this.annualRate = annualRate;
        this.years = years;
        this.capitalization = capitalization;
    }

    // Геттеры - методы для чтения значений полей из других классов.

    public double getPrincipal() {
        return principal;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public int getYears() {
        return years;
    }

    public boolean isCapitalization() {
        return capitalization;
    }
}
