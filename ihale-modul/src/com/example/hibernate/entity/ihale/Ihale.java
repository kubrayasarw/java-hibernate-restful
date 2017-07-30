package com.example.hibernate.entity.ihale;

import com.example.hibernate.entity.kurum.Kurum;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Kubra on 21.02.2017.
 */

@Entity
public class Ihale extends BaseEntity {


    @Column
    private String ikn;

    @Column
    private String isinAdi;

    @OneToOne
    @JoinColumn(name="kurumAdi")
    private Kurum kurumAdi;

    @Column
    private String isinKapsamı;

    @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "EET")
    @Column(nullable = false)
    private Date ihaleTarihiSaati;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column
    private Date ilanTarihi;

    @OneToOne
    @JoinColumn(name="ihaleTur")
    private ReferansTur ihaleTur;

    @OneToOne
    @JoinColumn(name="ihaleUsul")
    private ReferansTur ihaleUsul;
    @Column
    private String ihaleIptalNedeni;

    @Column
    private Boolean ihaleIptalDurum;

    @Column
    private Boolean ihaleAktiflikDurum;

    @OneToOne
    @JoinColumn(name="ihaleDosyasiniHazirlayacakPersonel")
    private Personel ihaleDosyasiniHazirlayacakPersonel;

    public String getIkn() {
        return ikn;
    }

    public void setIkn(String ikn) {
        this.ikn = ikn;
    }

    public Date getIhaleTarihiSaati() {
        return ihaleTarihiSaati;
    }

    public void setIhaleTarihiSaati(Date ihaleTarihiSaati) {
        this.ihaleTarihiSaati = ihaleTarihiSaati;
    }

    public String getIsinAdi() {
        return isinAdi;
    }

    public void setIsinAdi(String isinAdi) {
        this.isinAdi = isinAdi;
    }

    public Kurum getKurumAdi() {
        return kurumAdi;
    }

    public void setKurumAdi(Kurum kurumAdi) {
        this.kurumAdi = kurumAdi;
    }

    public String getIsinKapsamı() {
        return isinKapsamı;
    }

    public void setIsinKapsamı(String isinKapsamı) {
        this.isinKapsamı = isinKapsamı;
    }

    public Date getIlanTarihi() {
        return ilanTarihi;
    }

    public void setIlanTarihi(Date ilanTarihi) {
        this.ilanTarihi = ilanTarihi;
    }

    public ReferansTur getIhaleTur() {
        return ihaleTur;
    }

    public void setIhaleTur(ReferansTur ihaleTur) {
        this.ihaleTur = ihaleTur;
    }

    public ReferansTur getIhaleUsul() {
        return ihaleUsul;
    }

    public void setIhaleUsul(ReferansTur ihaleUsul) {
        this.ihaleUsul = ihaleUsul;
    }

    public String getIhaleIptalNedeni() {
        return ihaleIptalNedeni;
    }

    public void setIhaleIptalNedeni(String ihaleIptalNedeni) {
        this.ihaleIptalNedeni = ihaleIptalNedeni;
    }

    public Boolean getIhaleIptalDurum() {
        return ihaleIptalDurum;
    }

    public void setIhaleIptalDurum(Boolean ihaleIptalDurum) {
        this.ihaleIptalDurum = ihaleIptalDurum;
    }

    public Boolean getIhaleAktiflikDurum() {
        return ihaleAktiflikDurum;
    }

    public void setIhaleAktiflikDurum(Boolean ihaleAktiflikDurum) {
        this.ihaleAktiflikDurum = ihaleAktiflikDurum;
    }

    public Personel getIhaleDosyasiniHazirlayacakPersonel() {
        return ihaleDosyasiniHazirlayacakPersonel;
    }

    public void setIhaleDosyasiniHazirlayacakPersonel(Personel ihaleDosyasiniHazirlayacakPersonel) {
        this.ihaleDosyasiniHazirlayacakPersonel = ihaleDosyasiniHazirlayacakPersonel;
    }

}
