package entity;

import javax.persistence.*;

@Entity//обозначаем класс как сущность
public class School {//класс школа

    @Id//почемаем, это это поле - идентификатор
    @GeneratedValue(strategy = GenerationType.IDENTITY)//стратегия присвоения id берется из БД
    private int id;//поле id
    private int number;//поле номера школы

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER) //связь один к одному
    @JoinColumn(name = "address_id")//колонка адреса в таблице школы
    private Address address;


    public School() {// конструктор по умолчанию
    }
    //геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
        return "School{" +
                "id=" + id +
                ", number=" + number +
                ", address=" + address +
                '}';
    }
}
