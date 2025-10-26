package functions;
import java.io.*;
public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable{
    private FunctionPoint[] arrayPoints; //массив, хранящий точки функции
    private int arrayLen; //длина массива


    public String toString() {
        String s = "";
        for (int i = 0; i < arrayLen - 1; i++) {
            s += arrayPoints[i].toString() + ", ";
        }
        s += arrayPoints[arrayLen - 1].toString(); //дбавили последнюю точку
        return "{" + s + '}';
    }

    public Object clone() {
        FunctionPoint[] values = new FunctionPoint[arrayLen]; //создали массив для храненеия копий точек
        for (int i = 0; i < arrayLen; i++) { //заполняем массив копиями точек
            values[i] = (FunctionPoint) arrayPoints[i].clone(); //использовали приведение типа, так как возвращает тип Object
        }
        return new ArrayTabulatedFunction(values); //возвращаем новый объект копию старого, в качестве параметра конструктора передаём массив точек
    }

    public int hashCode() {
        int h = arrayLen; //учитываем длину, чтобы хэш коды различались для функций отличающихся наличием нулевой точки
        for (int i = 0; i < arrayLen; i++) {
            h ^= arrayPoints[i].hashCode(); //проводим операцию XOR c каждой точкой
        }
        return h;
    }
    public boolean equals(Object o){
        //прямое обращение к точке, так как объект ArrayTabulatedFunction
        if (o instanceof ArrayTabulatedFunction && (arrayLen == (((ArrayTabulatedFunction) o).arrayLen))) {
            for (int i = 0; i < arrayLen; i++) {
                if (!(arrayPoints[i].equals(((ArrayTabulatedFunction) o).arrayPoints[i]))) { //если точки не равны возвращаем false
                    return false;
                }
            }
            return true;
        } //если объект TabulatedFunction
        else if (o instanceof TabulatedFunction && (arrayLen == (((TabulatedFunction) o).getPointsCount()))) {
            for (int i = 0; i < arrayLen; i++) {
                if (!(arrayPoints[i].equals(((TabulatedFunction) o).getPoint(i)))) { //если точки не равны возвращаем false
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public ArrayTabulatedFunction() {} //конструктор без параметров

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(arrayLen);
        for (int i = 0; i < arrayLen; i++) {
            out.writeDouble(arrayPoints[i].getx());
            out.writeDouble(arrayPoints[i].gety());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        arrayLen = in.readInt();
        arrayPoints = new FunctionPoint[arrayLen];
        for (int i = 0; i < arrayLen; i++) {
            arrayPoints[i] = new FunctionPoint(in.readDouble(), in.readDouble());
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] values) {
        arrayLen = values.length;
        if (arrayLen < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        double leftX = values[0].getx(); //получаем первое значение x
        arrayPoints = new FunctionPoint[arrayLen];
        arrayPoints[0] = new FunctionPoint(values[0]); //добавляем в массив
        double rightX;
        for (int i = 1; i < arrayLen; i++) { //с помощью цикла заполняем массив, каждый раз проверяя не больше ли левая граница правой(то есть не нарушен ли порядок)
            rightX = values[i].getx(); //присваиваем значение правой границе
            if (leftX >= rightX) {
                throw new IllegalArgumentException("Левая граница больше правой");
            }
            leftX = rightX;
            arrayPoints[i] =  new FunctionPoint(values[i]); //добавляем копию
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        double intervalLength = 0;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        intervalLength = Math.abs(rightX - leftX) / (pointsCount - 1); //находим длину интервала между двумя точками
        arrayLen = pointsCount;
        arrayPoints = new FunctionPoint[arrayLen];
        for (int i = 0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                arrayPoints[i] = new FunctionPoint(leftX, 0);
                leftX += intervalLength; //смещаем левую границу на длину интервала
            } else {
                arrayPoints[i] = new FunctionPoint(rightX, 0); //последней точке присваиваем значение правой границы
            }
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        int pointsCount = values.length;
        arrayLen = pointsCount;
        arrayPoints = new FunctionPoint[arrayLen];
        double intervalLength = 0;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        intervalLength = Math.abs(leftX - rightX) / (pointsCount - 1); //находим длину интервала между двумя точками

        for (int i = 0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                arrayPoints[i] = new FunctionPoint(leftX, values[i]); //присваиваем значение из массива значений функции
                leftX += intervalLength; //смещаем левую границу на длину интервала
            } else {
                arrayPoints[i] = new FunctionPoint(rightX, values[i]); //последней точке присваиваем значение правой границы
            }
        }
    }

    public double getLeftDomainBorder() { //левая граница
        return arrayPoints[0].getx();
    }

    public double getRightDomainBorder() { //правая граница
        return arrayPoints[arrayLen - 1].getx();
    }

    public double getFunctionValue(double x) {
        if (x > this.getRightDomainBorder() || x < this.getLeftDomainBorder()) {//если точка вне интервала
            return Double.NaN;
        }
        if (x == this.getLeftDomainBorder()) {
            return arrayPoints[0].gety();
        }
        if (x == this.getRightDomainBorder()) {
            return arrayPoints[arrayLen - 1].gety();
        }
        int ind = 1;
        for (int i = 1; i < (arrayLen - 1); i++)
        {
            if (x >= arrayPoints[i - 1].getx() && x <= arrayPoints[i + 1].getx()) {
                ind = i; //находим индекс
            }
        }
        double X1 = getPointX(ind - 1);
        double Y1 = arrayPoints[ind - 1].gety();
        double X2 = getPointX(ind);
        double Y2 = arrayPoints[ind].gety();
        return ((Y2 - Y1) * (x - X1) / (X2 - X1)) + Y1; // ищем значение функции через уравнение прямой, проходящей через 2 точки
    }

    public int getPointsCount() { //количество точек
        return arrayLen;
    }

    public FunctionPoint getPoint(int index) { //возвращение копии точки
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return new FunctionPoint(arrayPoints[index].getx(), arrayPoints[index].gety());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException { //замена точки
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        double leftPoint;
        double rightPoint;
        if (index > 0) { //находим значение x слева
            leftPoint = arrayPoints[index - 1].getx();
        } else {
            leftPoint = point.getx(); //если индекс первой точки, то можем изменить значение левого x на значение новой точки
        }
        if (index < (arrayLen - 1)) { //находим значение x справа
            rightPoint = arrayPoints[index + 1].getx();
        } else {
            rightPoint = point.getx(); //если точка последняя
        }
        if (leftPoint > point.getx() || rightPoint < point.getx()) {
            throw new InappropriateFunctionPointException("Точка вне интервала");
        }
        arrayPoints[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) { //Значение x с указанным индексом
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return arrayPoints[index].getx();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException{  //изменение x у точки с заданным индексом
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        setPoint(index, new FunctionPoint(x, arrayPoints[index].gety())); //вызываем метод, который заменяет точку на новую с измененным x
    }

    public double getPointY(int index) { //Значение y с указанным индексом
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return arrayPoints[index].gety();
    }

    public void setPointY(int index, double y) throws InappropriateFunctionPointException{
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        setPoint(index, new FunctionPoint(arrayPoints[index].getx(), y));//вызываем метод, который заменяет точку на новую с измененным y
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        if (arrayLen < 3) {
            throw new IllegalStateException("Точек меньше трех");
        }
        for (int i = index; i < (arrayLen - 1); i++) {
            arrayPoints[i] = arrayPoints[i + 1]; //сдвигаем все элементы влево из-за удаленного
        }
        arrayLen -= 1; //длина уменьшилась на 1 элемент
        arrayPoints[arrayLen] = null; //последний элемент теперь ни на что не ссылается
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        double X = point.getx();
        int ind = 0;
        for (int i = 1; i < (arrayLen - 1); i++) {
            if (X == arrayPoints[i - 1].getx() || X == arrayPoints[i + 1].getx()) {
                throw new InappropriateFunctionPointException("Совпадение абсцисс");
            }
            if (X > arrayPoints[i - 1].getx() && X < arrayPoints[i + 1].getx()) {
                ind = i; //находим индекс для новой точки
            }
        }
        if (X > arrayPoints[arrayLen - 1].getx()) { //если x больше последнего x, добавляем в конец
            ind = arrayLen;
        }
        FunctionPoint[] arrayPoints1 = new FunctionPoint[arrayLen + 1];
        System.arraycopy(arrayPoints, 0, arrayPoints1, 0, ind); //скопировали все элементы, которые будут стоять до нового
        arrayPoints1[ind] = point; //добавили новую точку
        System.arraycopy(arrayPoints, ind, arrayPoints1, ind + 1, arrayLen - ind); //скопировали все элементы, которые будут стоять после нового
        arrayLen++;
        arrayPoints = arrayPoints1;
    }
}