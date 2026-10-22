package by.student.deposit;

import by.student.deposit.model.Deposit;
import by.student.deposit.service.DepositCalculator;

/**
 * ТОЧКА ВХОДА (Листинг 3 методички).
 * Java начинает выполнение программы с метода main.
 * Этот класс указан как Main-Class в JAR, поэтому работает "java -jar".
 */
public class Main {

    public static void main(String[] args) {
        // Демонстрационный вклад: 1000 BYN, 10% годовых, 3 года, с капитализацией.
        Deposit deposit = new Deposit(1000, 10, 3, true);
        // Создаём сервис расчёта и печатаем отчёт в консоль.
        DepositCalculator calculator = new DepositCalculator();
        System.out.println(calculator.buildReport(deposit));
    }
}
