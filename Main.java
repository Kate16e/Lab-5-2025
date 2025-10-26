import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {
    public static void eq(boolean mess) { //функция для вывода сообщения о том, равны ли функции
        if (mess) {
            System.out.println("Функции равны" + '\n');
        }
        else {
            System.out.println("Функции не равны" + '\n');
        }
    }
    public static void main(String[] s) throws InappropriateFunctionPointException {

        TabulatedFunction fun1 = new ArrayTabulatedFunction(1, 100, 10);
        for (int i=0; i < fun1.getPointsCount(); i++) {
            fun1.setPointY(i,3 * fun1.getPointX(i));
        }
        System.out.println("fun1: y = 3x");
        System.out.println(fun1.toString() + '\n');

        double[] val ={1, 2, 3, 5, 9, 6, 32};
        TabulatedFunction fun2 = new LinkedListTabulatedFunction(1, 7, val);
        System.out.println("fun2: Функция с значениями из массива");
        System.out.println(fun2.toString() + '\n');

        TabulatedFunction fun3 = TabulatedFunctions.tabulate(Functions.composition(new Log(Math.E), new Exp()), 1, 11, 11);
        System.out.println("fun3: log(exp)"); //композиция функций
        System.out.println(fun3.toString() + '\n');

        TabulatedFunction fun4 = TabulatedFunctions.tabulate(Functions.sum(fun1, fun3), 1, 11, 11);
        System.out.println("fun4: fun1 + fun3"); //сумма функций
        System.out.println(fun4.toString() + '\n');

        double[] val1 = {3, 36, 69, 102, 135, 168, 201, 234, 267, 300};
        TabulatedFunction fun5 = new LinkedListTabulatedFunction(1, 100, val1);
        System.out.println("fun5: Функция с значениями из массива");
        System.out.println(fun5.toString() + '\n');

        TabulatedFunction fun6 = (TabulatedFunction) fun4.clone(); //используем приведение типа, так как возвращает тип Object
        System.out.println("fun6: Копия fun4");
        System.out.println(fun6.toString() + '\n');

        System.out.println("Равенство функций fun1 и fun2 (объекты разных классов)");
        eq(fun1.equals(fun2));

        System.out.println("Равенство функций fun1 и fun3 (объекты одного класса)");
        eq(fun1.equals(fun3));

        System.out.println("Равенство функций fun1 и fun5 (объекты разных классов)");
        eq(fun1.equals(fun5));

        System.out.println("Равенство функций fun4 и fun6 (объекты одного класса)");
        eq(fun4.equals(fun6));

        System.out.println("Хэш-код для fun1");
        System.out.println(fun1.hashCode());
        System.out.println();

        System.out.println("Хэш-код для fun2");
        System.out.println(fun2.hashCode());
        System.out.println();

        System.out.println("Хэш-код для fun3");
        System.out.println(fun3.hashCode());
        System.out.println();

        System.out.println("Хэш-код для fun4");
        System.out.println(fun4.hashCode());
        System.out.println();

        System.out.println("Хэш-код для fun5");
        System.out.println(fun5.hashCode());
        System.out.println();

        System.out.println("Хэш-код для fun6");
        System.out.println(fun6.hashCode()); //для одинаковых функций хеш-код одинаковый
        System.out.println();

        System.out.println("Изменили точку с индексом 1 на 0.001");
        fun4.setPointY(1, fun4.getPointY(1) + 0.001);
        System.out.println(fun4.toString() + '\n');
        System.out.println("Измененный хэш-код для fun4");
        System.out.println(fun4.hashCode());
        System.out.println();

        System.out.println("Изменили исходную fun4, при этом копия fun6 не изменилась");
        System.out.println(fun6.toString() + '\n');

        TabulatedFunction fun7 = (TabulatedFunction) fun2.clone();
        System.out.println("fun7: Копия fun2");
        System.out.println(fun7.toString() + '\n');

        fun2.addPoint(new FunctionPoint(16, 12));
        System.out.println("Изменили исходную fun2");
        System.out.println(fun2.toString() + '\n');

        System.out.println("Копия fun7 не изменилась");
        System.out.println(fun7.toString() + '\n');

    }
}