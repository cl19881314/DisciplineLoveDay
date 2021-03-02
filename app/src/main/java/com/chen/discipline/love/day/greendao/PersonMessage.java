package com.chen.discipline.love.day.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author Chenhong
 * @date 2020/3/12.
 * @des
 */
@Entity
public class PersonMessage {
    @Id(autoincrement = true)
    private Long id;
    private String name;

    public PersonMessage() {
    }

    @Generated(hash = 1264154352)
    public PersonMessage(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
