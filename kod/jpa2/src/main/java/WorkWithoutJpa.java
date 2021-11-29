import java.sql.*;

public class WorkWithoutJpa {

    private String url = "jdbc:mysql://localhost:3306/jpa?serverTimezone=UTC"; //адрес БД
    private String userName = "root"; //логин к БД
    private String password = "root";//пароль к БД
    private Connection connection;
    private Statement statement;

    public void ConnectionToDataBase() {

        try {
            connection = DriverManager.getConnection(url, userName, password); //создаем новое соединение
            statement = connection.createStatement(); //создаем обект, для выполнния запросов sql к БД
            System.out.println("БД успешно подключена.");
            test();//вызываем метод, в котором описан алгоритм тестирования

        } catch (SQLException throwables) {//если произошли ошибки
            System.out.println("Ошибка подключения к БД");
            throwables.printStackTrace();
        }
    }


    private void test() throws SQLException {//метод тестирования

        System.out.println("Тестирование чтения из БД родителей.");
        System.out.println("Актуальная информация из БД:");
        String query;//строка, в которую будет писать запросы

        printTest();//метод, который выводит на экран информацию из базы данных

        System.out.println("Добавление в БД родителей тестовых записей для проверки...");

        query = "INSERT INTO jpa.parent" +
                        "(fio_father, fio_mother, address_id) " +
                        "VALUES " +
                        "('test_father', 'test_mother1', '1'), " +
                        "('test_father', 'test_mother2', '2'), " +
                        "('test_father', 'test_mother3', '3'), " +
                        "('test_father', 'test_mother4', '4'), " +
                        "('test_father', 'test_mother5', '5')";//добавляем тестовые записи в таблицк родителей

        statement.execute(query);//выполняем запрос
        System.out.println("Печать результата после добавления в БД родителей тестовых записей:");
        printTest();//выводим на экран информацию из базы данных


        System.out.println("Удаление тестовых записей из БД родителей...");
        query = "DELETE FROM jpa.parent WHERE (fio_father = 'test_father')";//удаляем добавленные записи из таблицы родителей
        statement.execute(query);//выполняем запрос
        query = "ALTER TABLE jpa.parent AUTO_INCREMENT = 1";//сбрасываем счетчик автоинкремента
        statement.execute(query);//выполняем запрос
        System.out.println("Результат удаления тестовых записей из БД родителей...");
        printTest();//выводим на экран информацию из базы данных


    }

    private void printTest() throws SQLException {

        String query = "SELECT * FROM jpa.parent"; //формируем запрос
        ResultSet resultSet = statement.executeQuery(query); //отправляем запрос и получаем ответ

        int column = resultSet.getMetaData().getColumnCount();//получение кол-ва столбцов

        //печать на экран:
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s| ", resultSet.getMetaData().getColumnName(1));
        System.out.printf("%-35s| ", resultSet.getMetaData().getColumnName(2));
        System.out.printf("%-35s| ", resultSet.getMetaData().getColumnName(3));
        System.out.printf("%-35s| ", resultSet.getMetaData().getColumnName(4));
        System.out.println("\n-------------------------------------------------------------------------------------------------------------------");

        while (resultSet.next()){//пока есть записи в БД
            for(int i =1; i <= column; i++){//цикл по кол-ву столбцов
                if(i == 1){
                    System.out.printf("%-3s| ", resultSet.getString(i));//если первый столбцец, то это ID, оставляем 3 символа под них
                }else {
                    System.out.printf("%-35s| ", resultSet.getString(i));//в других информация, оставляем 20 символов
                }

            }
            System.out.println();

        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
    }





}
