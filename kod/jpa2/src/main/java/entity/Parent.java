package entity;
import javax.persistence.*;
import java.util.List;

@Entity//обозначаем класс как сущность
public class Parent {//класс родители

    @Id//почемаем, это это поле - идентификатор
    @GeneratedValue(strategy = GenerationType.IDENTITY)//стратегия присвоения id берется из БД
    private int id;//поле id
    private String fio_mother;//фио мамы
    private String fio_father;//фио папы

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//один ко многим
    @JoinColumn(name = "parent_id")//колонка родителей в таблице ребенка
    private List<Children> children;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//один к одному
    @JoinColumn(name = "address_id")//колонка адреса в таблице родителей
    private Address address;

    public Parent() {
    }// конструктор по умолчанию
    //геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio_mother() {
        return fio_mother;
    }

    public void setFio_mother(String fio_mather) {
        this.fio_mother = fio_mather;
    }

    public String getFio_father() {
        return fio_father;
    }

    public void setFio_father(String fio_father) {
        this.fio_father = fio_father;
    }

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    //переопределенный метод вывода объекта на экран
    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", fio_mather='" + fio_mother + '\'' +
                ", fio_father='" + fio_father + '\'' +
                ", children=" + children +
                ", address=" + address +
                '}';
    }
}
