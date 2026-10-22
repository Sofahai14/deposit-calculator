package by.student.deposit;

import by.student.deposit.model.Deposit;
import by.student.deposit.service.DepositCalculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// static import: можно писать assertEquals(...) вместо Assertions.assertEquals(...)
import static org.junit.jupiter.api.Assertions.*;

/**
 * МОДУЛЬНЫЕ ТЕСТЫ (Листинг 4 методички + дополнительные тесты).
 *
 * Группы тестов:
 *  [Позитивный] - нормальные данные, проверяем правильность расчёта;
 *  [Граничный]  - крайние значения (ставка 0, срок 1 год, несколько ставок);
 *  [Ошибочный]  - неправильные данные, ожидаем IllegalArgumentException.
 *
 * Имя класса оканчивается на Test - по этому признаку тесты находят
 * Maven Surefire и наш build.xml (шаблон "*Test.class").
 */
@DisplayName("Тесты калькулятора вкладов")
class DepositCalculatorTest {

    /** Допустимая погрешность при сравнении дробных чисел. */
    private static final double DELTA = 0.001;

    private DepositCalculator calculator;   // тестируемый объект

    /** @BeforeEach: выполняется перед КАЖДЫМ тестом - каждый тест получает новый объект. */
    @BeforeEach
    void setUp() {
        calculator = new DepositCalculator();
    }

    // ======================== ПОЗИТИВНЫЕ ========================

    @Test
    @DisplayName("[Позитивный] Простые проценты: 1000 BYN, 10%, 3 года = 1300")
    void simpleInterest() {
        Deposit deposit = new Deposit(1000, 10, 3, false);
        // assertEquals(ожидаемое, фактическое, погрешность)
        assertEquals(1400.0, calculator.calculateSimple(deposit), DELTA);
    }

    @Test
    @DisplayName("[Позитивный] Сложные проценты: 1000 BYN, 10%, 3 года = 1331")
    void compoundInterest() {
        Deposit deposit = new Deposit(1000, 10, 3, true);
        assertEquals(1331.0, calculator.calculateCompound(deposit), DELTA);
    }

    @Test
    @DisplayName("[Позитивный] calculateTotal выбирает режим капитализации")
    void totalDependsOnCapitalization() {
        Deposit simple = new Deposit(1000, 10, 3, false);
        Deposit compound = new Deposit(1000, 10, 3, true);
        assertEquals(1300.0, calculator.calculateTotal(simple), DELTA);
        assertEquals(1331.0, calculator.calculateTotal(compound), DELTA);
    }

    @Test
    @DisplayName("[Позитивный] Начисленные проценты положительны")
    void interestIsPositive() {
        Deposit deposit = new Deposit(1000, 10, 3, true);
        assertTrue(calculator.calculateInterest(deposit) > 0);
    }

    @Test
    @DisplayName("[Позитивный] Начисленные простые проценты = 300 BYN")
    void simpleInterestAmount() {
        Deposit deposit = new Deposit(1000, 10, 3, false);
        assertEquals(300.0, calculator.calculateInterest(deposit), DELTA);
    }

    @Test
    @DisplayName("[Позитивный] Отчёт содержит ключевые поля")
    void reportContainsKeyFields() {
        String report = calculator.buildReport(new Deposit(1000, 10, 3, true));
        // assertAll выполняет все проверки и сообщает обо всех ошибках сразу
        assertAll(
                () -> assertTrue(report.contains("Итоговая сумма")),
                () -> assertTrue(report.contains("Начислено процентов")),
                () -> assertTrue(report.contains("Капитализация"))
        );
    }

    // ======================== ГРАНИЧНЫЕ ========================

    @Test
    @DisplayName("[Граничный] Нулевая ставка не изменяет сумму")
    void zeroRateDoesNotChangePrincipal() {
        Deposit deposit = new Deposit(1000, 0, 5, false);
        assertEquals(1000.0, calculator.calculateTotal(deposit), DELTA);
    }

    @Test
    @DisplayName("[Граничный] Срок 1 год: простые и сложные проценты совпадают (1100)")
    void oneYearSimpleEqualsCompound() {
        Deposit simple = new Deposit(1000, 10, 1, false);
        Deposit compound = new Deposit(1000, 10, 1, true);
        assertAll(
                () -> assertEquals(1100.0, calculator.calculateTotal(simple), DELTA),
                () -> assertEquals(1100.0, calculator.calculateTotal(compound), DELTA)
        );
    }

    /**
     * Параметризованный тест: ОДИН метод запускается 4 раза с разными
     * ставками из @CsvSource ("ставка, ожидаемый итог"): 1000 BYN, 2 года, капитализация.
     */
    @ParameterizedTest(name = "[Граничный] ставка {0}% -> {1} BYN")
    @CsvSource({
            "0,  1000.0",
            "5,  1102.5",
            "10, 1210.0",
            "15, 1322.5"
    })
    void compoundForSeveralRates(double rate, double expected) {
        Deposit deposit = new Deposit(1000, rate, 2, true);
        assertEquals(expected, calculator.calculateCompound(deposit), DELTA);
    }

    // ======================== ОШИБОЧНЫЕ ========================

    @Test
    @DisplayName("[Ошибочный] Отрицательная сумма вызывает исключение")
    void negativePrincipalIsRejected() {
        // assertThrows(тип исключения, код, который должен его выбросить)
        assertThrows(IllegalArgumentException.class, () -> new Deposit(-100, 10, 3, false));
    }

    @Test
    @DisplayName("[Ошибочный] Нулевая сумма вызывает исключение")
    void zeroPrincipalIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Deposit(0, 10, 3, false));
    }

    @Test
    @DisplayName("[Ошибочный] Нулевой срок вызывает исключение")
    void zeroYearsIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Deposit(1000, 10, 0, false));
    }

    @Test
    @DisplayName("[Ошибочный] Отрицательная ставка вызывает исключение с понятным сообщением")
    void negativeRateIsRejected() {
        // assertThrows возвращает пойманное исключение - проверяем и его текст
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Deposit(1000, -1, 3, false));
        assertEquals("Ставка не может быть отрицательной", ex.getMessage());
    }
}
