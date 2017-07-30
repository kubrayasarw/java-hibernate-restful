package com.example.hibernate.entity.kurum;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Kubra on 2.03.2017.
 */
@Entity
public class Kurum extends BaseEntity {
    @Column
    private String kurumAdi;
    @Column
    private String kurumTel;


    public String getKurumAdi() {
        return kurumAdi;
    }

    public void setKurumAdi(String kurumAdi) {
        this.kurumAdi = kurumAdi;
    }

    public String getKurumTel() {
        return kurumTel;
    }

    public void setKurumTel(String kurumTel) {
        this.kurumTel = kurumTel;
    }
}
