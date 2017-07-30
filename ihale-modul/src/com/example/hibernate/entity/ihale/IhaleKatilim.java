package com.example.hibernate.entity.ihale;

import javax.persistence.*;

/**
 * Created by Kubra on 7.03.2017.
 */
@Entity
public class IhaleKatilim extends BaseEntity {
    @OneToOne
    @JoinColumn (name="ihaleyeKatilacakPersonel")
    private Personel ihaleyeKatilacakPersonel;

    @OneToOne
    @JoinColumn(name = "ihale")
    private Ihale ihale;

    public Ihale getIhale() {
        return ihale;
    }

    public void setIhale(Ihale ihale) {
        this.ihale = ihale;
    }

    public Personel getIhaleyeKatilacakPersonel() {
        return ihaleyeKatilacakPersonel;
    }

    public void setIhaleyeKatilacakPersonel(Personel ihaleyeKatilacakPersonel) {
        this.ihaleyeKatilacakPersonel = ihaleyeKatilacakPersonel;
    }
}
