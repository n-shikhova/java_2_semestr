import entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Work { //основнй класс задания

    private EntityManagerFactory entityManagerFactory; //интерфейс ORM для управления персистентными сущностями
    private EntityManager entityManager;// выполняет основные операции над сущностями
    private Scanner scanner;//для ввода команд с клавиатуры
    private String commandFromUser; //для сохранения команды пользователя

    public Work() {// конструктор
        entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit"); //подставляем файл, в котором описано соединение с БД
        entityManager = null;
        scanner = new Scanner(System.in); //создаем объект сканнера, для чтения клавиатуры
    }

    public void workThisDataBase(){ //метод работы с БД
        selectAction();//метод выбора операции
    }

    private void selectAction(){//метод выбора операции
        System.out.println("Вы начинаете работать с базой данных! Выберете необходимую задачу:");
        System.out.println("1. Добавить родителей в БД;");
        System.out.println("2. Добавить ребенка в БД;");
        System.out.println("3. Смена адреса проживания;");
        System.out.println("4. Смена учебного заведения;");
        System.out.println("5. Тестирование без JPA;");
        System.out.print("Ввести номер задачи: ");
        commandFromUser = scanner.nextLine();//ждем команды поьзователя
        System.out.println("Выбрана задача №: " + commandFromUser + ".");

        switch (commandFromUser) { //в зависимости от команды, выполняем действия
            case ("1"):{
                System.out.println("Добавление родителей в БД.");
                addParentInDataBase();
                break;
            }
            case ("2"):{
                System.out.println("Добавление ребенка в БД.");
                addChildrenInDataBase();
                break;
            }
            case ("3"):{
                System.out.println("Смена адреса проживания");
                updateAddress();
                break;
            }
            case ("4"):{
                System.out.println("Смена учебного заведения");
                updateSchool();
                break;
            }
            case ("5"):{
                WorkWithoutJpa workWithoutJpa = new WorkWithoutJpa();
                workWithoutJpa.ConnectionToDataBase();
                break;
            }
            default:{
                System.out.println("Команда не распознана. Завершение работы.");
                break;
            }
        }

    }

    private void addParentInDataBase(){ //метод добаления родителей в БД

        Parent parent = new Parent(); //создаем сущность родителя
        System.out.print("Введите ФИО отца:");
        parent.setFio_father(scanner.nextLine());//устанавливаем ФИО отца
        System.out.print("Введите ФИО матери:");
        parent.setFio_mother(scanner.nextLine());//устанавливаем ФИО матери
        System.out.println("Выбор адреса проживания:");

        createAndBeginEntityManager(); //вызываем метод для открытия менеджера сущностей и возможности выпонять запрос к БД
        List<Address> addresses = entityManager.createQuery("select address from Address address").getResultList();//выбираем все места жительства из БД
        for(Address address : addresses){
            System.out.println("id = " + address.getId() + ". " + address.getAddress()); //выводим в цикле
        }

        Address address = null;//подготовка к созданию сущности
        while (address == null){ //пока сущность не установлена
            System.out.print("Введите соответствующий id адреса проживания:");
            address = entityManager.find(Address.class, strInput()); //пользователь должен ввести корректный ID, после чего сущность будет установлена
        }

        parent.setAddress(address);//устанавливаем место жительства для родителей
        entityManager.persist(parent);//подготовка родителя к записи в БД - добавление в контекст
        closeAndCommitEntityManager(); //метод записыват в БД и закрывает менеджер сущностей
        System.out.print("Выполнено.");
    }

    private void  addChildrenInDataBase(){//метод добавления ребенка в БД

        Children children = new Children();//создаем сущность ребенка
        System.out.print("Введите ФИО ребенка: ");
        children.setFio(scanner.nextLine());//устанавливаем ФИО
        System.out.print("Введите возраст ребенка: ");
        children.setAge(strInput());//устанавливаем возраст
        System.out.println("Добавление родителей: ");

        createAndBeginEntityManager();//вызываем метод для открытия менеджера сущностей и возможности выпонять запрос к БД
        List<Parent> parents = entityManager.createQuery("select parent from Parent parent").getResultList(); //выбираем всех родителей из БД
        for(Parent parent : parents){
            System.out.println("id = " + parent.getId() + ". Отец: " + parent.getFio_father() + "; Мать: " + parent.getFio_mother()); //выводим в цикле
        }

        Parent parent = null;//подготовка к созданию сущности
        while (parent == null){//пока сущность не установлена
            System.out.print("Введите соответствующий id родителей:");
            parent = entityManager.find(Parent.class, strInput());//пользователь должен ввести корректный ID, после чего сущность будет установлена
        }
        children.setParent(parent);//устанавливаем родителей для ребенка

        System.out.println("Зачисление в школу. Ребенок может быть зачислен в следующие школы: ");
        District district = parent.getAddress().getDistrict();//получаем район, где живут родители
        for(Address address : district.getAddresses()){//проходимся по всем адресам в районе
            System.out.println("id = " + address.getSchool().getId() + ". Школа № " + address.getSchool().getNumber());//выводим доступные школы для поступления
        }

        School school = null;//подготовка к созданию сущности
        while (school == null){//пока сущность не установлена
            System.out.print("Введите соответствующий id школы: ");
            int schoolId = strInput();//пользователь вводит Id школы
            List<Address> addressesList = district.getAddresses().stream().
                                          filter(address -> address.getSchool().getId() == schoolId).
                                          collect(Collectors.toList());//ищем введенную пользователем школу и заносим в список
            if(addressesList.size() == 1){ //если найдена 1 школа
                school = addressesList.get(0).getSchool();//устанавливаем сущность школы
            }
        }

        children.setSchool(school);//устанавливаем школу для ребенка
        entityManager.persist(children);//подготовка ребенка к записи в БД - добавление в контекст
        closeAndCommitEntityManager();//метод записыват в БД и закрывает менеджер сущностей
        System.out.println("Выполнено.");
    }

    private void updateAddress(){//метод изменения адреса

        System.out.println("Введите соответствующий id родителей, у которых необходимо сменить адресс проживания: ");
        createAndBeginEntityManager();//вызываем метод для открытия менеджера сущностей и возможности выпонять запрос к БД
        List<Parent> parents = entityManager.createQuery("select parent from Parent parent").getResultList();//выбираем всех родителей из БД

        for(Parent parent : parents){//цикл по родителям
            System.out.println("id = " + parent.getId() + ". Отец: " + parent.getFio_father() + "; Мать: " + parent.getFio_mother());//выводим родителей
        }

        Parent parent = null;//подготовка к созданию сущности
        while (parent == null){//пока сущность не установлена
            System.out.print("Введите соответствующий id родителей:");
            parent = entityManager.find(Parent.class, strInput());//пользователь должен ввести корректный ID, после чего сущность будет установлена
        }
        System.out.println("Введите соответствующий id нового места жительства:");

        List<Address> addresses = entityManager.createQuery("select address from Address address").getResultList();//выбираем все адреса
        for(Address address : addresses){//цикл по адресам
            System.out.println("id = " + address.getId() + ". " + address.getAddress());//выводим адреса
        }

        Address address = null;//подготовка к созданию сущности
        while (address == null){//пока сущность не установлена
            System.out.print("Введите соответствующий id нового места жительства:");
            address = entityManager.find(Address.class, strInput());//пользователь должен ввести корректный ID, после чего сущность будет установлена
        }

        parent.setAddress(address);//устанавливаем адрес для родителей
        entityManager.persist(parent);//подготовка родителя к записи в БД - добавление в контекст
        closeAndCommitEntityManager();//метод записыват в БД и закрывает менеджер сущностей
        System.out.println("Выполнено.");
    }

    private void updateSchool(){//метод изменения школы

        System.out.println("Введите соответствующий id ребенка, учебное заведение которого необходимо изменить:");

        createAndBeginEntityManager();//вызываем метод для открытия менеджера сущностей и возможности выпонять запрос к БД

        List<Children> childrenList = entityManager.createQuery("select children from Children children").getResultList();//выбираем детей из БД
        for(Children children : childrenList){
            System.out.println("id = " + children.getId() + ". ФИО = " + children.getFio() + ", текущая школа = Школа №" + children.getSchool().getNumber());//выводим детей
        }

        Children children = null;//подготовка к созданию сущности

        while (children == null){//пока сущность не установлена
            System.out.print("Введите соответствующий id ребенка:");
            children = entityManager.find(Children.class, strInput());//пользователь должен ввести корректный ID, после чего сущность будет установлена
        }

        System.out.println("Введите соответствующий id школы, в которую необходимо зачислить ребенка:");

        List<School> schoolList = entityManager.createQuery("select school from School school").getResultList();//выбираем все школы из БД
        for(School school : schoolList){
            System.out.println("id = " + school.getId() + ". Школа №" + school.getNumber());//выводим школы
        }

        School school = null;//подготовка к созданию сущности
        while (school == null){//пока сущность не установлена
            System.out.print("Введите соответствующий id школы:");
            school = entityManager.find(School.class, strInput());//пользователь должен ввести корректный ID, после чего сущность будет установлена
        }

        children.setSchool(school);//добавляем школу для ребенка
        entityManager.persist(children);//подготовка ребенка к записи в БД - добавление в контекст
        closeAndCommitEntityManager();//метод записыват в БД и закрывает менеджер сущностей
        System.out.println("Выполнено.");

    }

    private void createAndBeginEntityManager(){//закрытиые и начало работы мнеджера сущностей
        entityManager = entityManagerFactory.createEntityManager();//получаем менеджер сущностей
        entityManager.getTransaction().begin();//открываем "сессию" с базой данных
    }

    private void closeAndCommitEntityManager(){//сохранение изменений в БД и закрытие мнеджера сущностей
        entityManager.getTransaction().commit();//запись в БД
        entityManager.close();//зыкрываем менеджер сущностей
    }

    private int strInput(){//метод проверки пользовательского ввода
        int id;

        while(true){
            try{
                id = Integer.parseInt(scanner.nextLine());
                break;
            }catch (NumberFormatException e){
                System.out.print("Ошибка ввода, повторите попытку:");
            }
        }

        return id;
    }








}
