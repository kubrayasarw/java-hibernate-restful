package com.example.hibernate.entity.ihale;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Kubra on 23.02.2017.
 */
@Entity
public class IhaleDetay extends BaseEntity {

    @Column
    private String benzerIs;

    @Column
    private String tahminiYM;
    @Column
    private String sozlesmeSuresi;

    @Column
    private String isBaslangicTarihi;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column
    private Date sonSikayetTarihi;

    @Column
    private String odemeYeriSartlari;

    @ManyToOne
    @JoinColumn(name = "ihale")
    private Ihale ihale;


    public String getBenzerIs() {
        return benzerIs;
    }

    public void setBenzerIs(String benzerIs) {
        this.benzerIs = benzerIs;
    }



    public String getSozlesmeSuresi() {
        return sozlesmeSuresi;
    }

    public void setSozlesmeSuresi(String sozlesmeSuresi) {
        this.sozlesmeSuresi = sozlesmeSuresi;
    }

    public String getIsBaslangicTarihi() {
        return isBaslangicTarihi;
    }

    public void setIsBaslangicTarihi(String isBaslangicTarihi) {
        this.isBaslangicTarihi = isBaslangicTarihi;
    }

    public String getOdemeYeriSartlari() {
        return odemeYeriSartlari;
    }

    public void setOdemeYeriSartlari(String odemeYeriSartlari) {
        this.odemeYeriSartlari = odemeYeriSartlari;
    }

    public Ihale getIhale() {
        return ihale;
    }

    public void setIhale(Ihale ihale) {
        this.ihale = ihale;
    }

    public String getTahminiYM() {
        return tahminiYM;
    }

    public void setTahminiYM(String tahminiYM) {
        this.tahminiYM = tahminiYM;
    }

    public Date getSonSikayetTarihi() {
        return sonSikayetTarihi;
    }

    public void setSonSikayetTarihi(Date sonSikayetTarihi) {
        this.sonSikayetTarihi = sonSikayetTarihi;
    }
}
