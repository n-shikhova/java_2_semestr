package entity;

import javax.persistence.*;
import java.util.List;

@Entity  //обозначаем класс как сущность
public class District { //создаем класс(сущность) район

    @Id//почемаем, это это поле - идентификатор
    @GeneratedValue(strategy = GenerationType.IDENTITY) //стратегия присвоения id берется из БД
    private int id; //поле id
    private String district_name; //поле название района

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //обозначаем связть один ко многим т.е. в одном районе много адресов
    @JoinColumn(name = "district_id") //поле района в таблице адреса
    private List<Address> addresses; //поле с адресами района

    public District() {
    } //конструктор по умолчанию

    //геттеры и сеттеры:

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    //переопределенный метод вывода объекта на экран
    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", districtName='" + district_name + '\'' +
                '}';
    }
}
