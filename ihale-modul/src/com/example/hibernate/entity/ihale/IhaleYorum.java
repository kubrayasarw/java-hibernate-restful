package com.example.hibernate.entity.ihale;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Kubra on 29.03.2017.
 */
@Entity
public class IhaleYorum extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "ihale")
    private Ihale ihale;
    @OneToOne
    @JoinColumn(name="personel")
    private Personel personel;
    @Column
    private String yorum;
    @CreationTimestamp
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "EET")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    public Ihale getIhale() {
        return ihale;
    }

    public void setIhale(Ihale ihale) {
        this.ihale = ihale;
    }

    public Personel getPersonel() {
        return personel;
    }

    public void setPersonel(Personel personel) {
        this.personel = personel;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
