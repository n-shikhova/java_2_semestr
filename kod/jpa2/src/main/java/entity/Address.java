package entity;

import javax.persistence.*;

@Entity//обозначаем класс как сущность
public class Address {//класс адрес

    @Id//почемаем, это это поле - идентификатор
    @GeneratedValue(strategy = GenerationType.IDENTITY)//стратегия присвоения id берется из БД
    private int id;//поле id

    private String address;//адрес проживания

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //обозначение связи многие к одному т.е. в одном районе много адресов
    @JoinColumn(name = "district_id") //указываем на колонку с районом в таблице адресов
    private District district;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//связь один к одному
    @JoinColumn(name = "school_id")//колонка школы в таблице адресов
    private School school;

    public Address() {
    }// конструктор по умолчанию
    //геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
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
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", district=" + district +
                ", school=" + school +
                '}';
    }
}
