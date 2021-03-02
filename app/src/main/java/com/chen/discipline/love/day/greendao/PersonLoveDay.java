package com.chen.discipline.love.day.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Chenhong
 * @date 2020/3/12.
 * @des
 */
@Entity
public class PersonLoveDay {
    @Id(autoincrement = true)
    private Long id;
    private String dayName;
    //0 农历  1公历
    private String dayType;
    private String day;
    private long personId;

    public PersonLoveDay() {
    }

    @Generated(hash = 873589135)
    public PersonLoveDay(Long id, String dayName, String dayType, String day,
            long personId) {
        this.id = id;
        this.dayName = dayName;
        this.dayType = dayType;
        this.day = day;
        this.personId = personId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayName() {
        return this.dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayType() {
        return this.dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getPersonId() {
        return this.personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
