package entity;

import javax.persistence.*;

@Entity//обозначаем класс как сущность
public class Children {//класс ребенок

    @Id//почемаем, это это поле - идентификатор
    @GeneratedValue(strategy = GenerationType.IDENTITY)//стратегия присвоения id берется из БД
    private int id;//поле id

    private String fio;//фио ребенка
    private int age;//возраст

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//многие к одному
    @JoinColumn(name = "parent_id")//колонка родителей в таблице ребенка
    private Parent parent;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//связь один к одному
    @JoinColumn(name = "school_id")//колонка школы в таблице ребенка
    private School school;

    public Children() {
    }// конструктор по умолчанию
    //геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
    //переопределенный метод вывода объекта на экран
    @Override
    public String toString() {
        return "Children{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", age=" + age +
                ", parent=" + parent +
                ", school=" + school +
                '}';
    }
}
