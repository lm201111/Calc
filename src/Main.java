import java.util.*;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение:");
        String input = scanner.nextLine();

        try {
            String result = calc(input);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static String calc(String input) throws Exception {
        String[] elements = input.split("\\s");

        if (elements.length != 3) {
            throw new Exception("Некорректный формат математической операции");
        }

        String operand1 = elements[0];
        String operator = elements[1];
        String operand2 = elements[2];

        if (!(isNumber(operand1) && isNumber(operand2)) && !(isRoman(operand1) && isRoman(operand2))) {
            throw new Exception("Используются разные системы счисления");
        }

        int num1 = convertToNumber(operand1);
        int num2 = convertToNumber(operand2);

        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new Exception("Числа должны быть от 1 до 10 включительно");
        }

        int result;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    throw new Exception("Деление на ноль");
                }
                result = num1 / num2;
                break;
            default:
                throw new Exception("Некорректный оператор");
        }

        if (isRoman(operand1) && isRoman(operand2)) {
            if (result <= 0) {
                throw new Exception("Римские числа не могут быть отрицательными или нулем");
            }
            return convertToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static boolean isNumber(String str) {
        try {
            int num = Integer.parseInt(str);
            return num >= 1 && num <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isRoman(String str) {
        String romanPattern = "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
        return str.matches(romanPattern);
    }

    private static int convertToNumber(String str) {
        if (isNumber(str)) {
            return Integer.parseInt(str);
        } else {
            return convertRomanToNumber(str);
        }
    }

    private static int convertRomanToNumber(String roman) {
        Map<Character, Integer> romanNumerals = new HashMap<>();
        romanNumerals.put('I', 1);
        romanNumerals.put('V', 5);
        romanNumerals.put('X', 10);
        romanNumerals.put('L', 50);
        romanNumerals.put('C', 100);
        romanNumerals.put('D', 500);
        romanNumerals.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int value = romanNumerals.get(roman.charAt(i));
            if (value < prevValue) {
                result -= value;
            } else {
                result += value;
            }
            prevValue = value;
        }

        return result;
    }

    private static String convertToRoman(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Отрицательные или нулевые значения невозможны для римских чисел");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        StringBuilder sb = new StringBuilder();

        int i = 0;

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}

enum RomanNumeral {
    I(1),
    IV(4),
    V(5),
    IX(9),
    X(10),
    XL(40),
    L(50),
    XC(90),
    C(100),
    CD(400),
    D(500),
    CM(900),
    M(1000);

    private final int value;

    RomanNumeral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                .collect(Collectors.toList());
    }
}