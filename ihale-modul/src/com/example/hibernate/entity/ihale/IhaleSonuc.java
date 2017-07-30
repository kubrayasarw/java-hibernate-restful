package com.example.hibernate.entity.ihale;

import com.example.hibernate.entity.kurum.Kurum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by Kubra on 14.03.2017.
 */
@Entity
public class IhaleSonuc extends BaseEntity {
    @OneToOne
    @JoinColumn(name="kurum")
    private Kurum kurum;
    @Column
    private Double teklif;
    @Column
    private Double yaklasikMaliyet;

    @OneToOne
    @JoinColumn(name="ihale")
    private Ihale ihale;


    public Kurum getKurum() {
        return kurum;
    }

    public void setKurum(Kurum kurum) {
        this.kurum = kurum;
    }

    public Double getTeklif() {
        return teklif;
    }

    public void setTeklif(Double teklif) {
        this.teklif = teklif;
    }

    public Ihale getIhale() {
        return ihale;
    }

    public void setIhale(Ihale ihale) {
        this.ihale = ihale;
    }

    public Double getYaklasikMaliyet() {
        return yaklasikMaliyet;
    }

    public void setYaklasikMaliyet(Double yaklasikMaliyet) {
        this.yaklasikMaliyet = yaklasikMaliyet;
    }
}
