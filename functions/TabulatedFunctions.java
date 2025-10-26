package functions;
import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() { //конструктор, чтобы нельзя было создать объект этого класса

    }
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы для табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        double[] values = new double[pointsCount]; //создали массив, который будет хранить значения табулированной функции
        double funVal; //переменная для хранения y в каждой точке
        double intervalLength = Math.abs(leftX - rightX) / (pointsCount - 1); //находим длину интервала между двумя точками
        for (int i=0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                funVal = function.getFunctionValue(leftX + i*intervalLength); //получаем значение функции в заданной точке, каждый раз перемещая x на длину интервала
            }
            else {
                funVal = function.getFunctionValue(rightX); //последней точке присваиваем значение у правой границы
            }
            values[i] = funVal; //заполняем массив значениями
        }
        TabulatedFunction tabFun = new ArrayTabulatedFunction(leftX, rightX, values); //создаем объект, который будет хранить табулированную функцию в виде массива, в конструктор передаем границы и значения функции в каждой точке
        return tabFun;
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException{
        try (DataOutputStream Out = new DataOutputStream(out)) {
            Out.writeInt(function.getPointsCount());
            for (int i=0; i < function.getPointsCount(); i++) {
                Out.writeDouble(function.getPointX(i));
                Out.writeDouble(function.getPointY(i));
            }
            Out.flush(); //сбрасываем буфер
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException, InappropriateFunctionPointException {
        try (DataInputStream In = new DataInputStream(in)) {
            int pointsCount = In.readInt();
            ArrayTabulatedFunction tabFun = new ArrayTabulatedFunction(0, pointsCount - 1, pointsCount); //создаем объект для хранения функции
            for (int i=0; i < pointsCount; i++) {
                tabFun.setPointX(i, In.readDouble()); //записывам значения x и y
                tabFun.setPointY(i, In.readDouble());
            }

            return tabFun;
        }
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        try (BufferedWriter Out = new BufferedWriter(out)) {
            Out.write(String.valueOf(function.getPointsCount()));
            Out.newLine();
            for (int i=0; i < function.getPointsCount(); i++) {
                Out.write(String.valueOf(function.getPointX(i)));
                Out.write(" ");
                Out.write(String.valueOf(function.getPointY(i)));
                Out.newLine();
            }
            Out.flush();
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException, InappropriateFunctionPointException {
        StreamTokenizer In = new StreamTokenizer(in);
        In.nextToken(); //переход к следующему токену
        int pointsCount = (int)In.nval; //числовое значение токена преобразуем в число типа int
        ArrayTabulatedFunction tabFun = new ArrayTabulatedFunction(0, pointsCount - 1, pointsCount);
        for (int i=0; i < pointsCount; i++) {
            In.nextToken();
            tabFun.setPointX(i, In.nval); //записывам значения x и y
            In.nextToken();
            tabFun.setPointY(i, In.nval); //In.nval - статическое плое типа double
        }

        return tabFun;
    }


}
