package mainonta;

import java.io.*;
import java.util.Arrays;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue; 

import static kanta.HetuTarkistus.rand;

/**
 * Mielenkiinto joka osaa mm. itse huolehtia tunnus_nro:staan.
 *
 * @author Johanna Virkkunen
 * @version 5.3.2019 vaihe 5 aloitus
 * @version 30.3.2019 vaihe 6 aloitettu
 * @version 4.4.2019 vaihe 6 jatkettu 
 * @version 21.04.2019 vaihe 7
 */
public class Mielenkiinto implements Cloneable, Tietue {
    private int tunnusNro;
    private int henkiloNro;
    private String asia = "";
    private int vahvuus;

    private static int seuraavaNro = 1;


    /**
     * Alustetaan mielenkiinto.  Toistaiseksi ei tarvitse tehdä mitään
     */
    public Mielenkiinto() {
        // Vielä ei tarvita mitään
    }


    /**
     * Alustetaan tietyn henkilön mielenkiinto.  
     * @param henkiloNro henkilön viitenumero 
     */
    public Mielenkiinto(int henkiloNro) {
        this.henkiloNro = henkiloNro;
    }
    
    
    /**
     * @return mielenkiintojen kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 4;
    }


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    
    
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "henkilöId";
            case 2:
                return "asia";
            case 3:
                return "vahvuus";
            default:
                return "???";
        }
    }
    
    
    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.parse("   1   |  1  |   Tekniikka  | 5 ");
     *   miel.anna(0) === "1";   
     *   miel.anna(1) === "1";   
     *   miel.anna(2) === "Tekniikka";   
     *   miel.anna(3) === "5";     
     *   
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + tunnusNro;
            case 1:
                return "" + henkiloNro;
            case 2:
                return asia;
            case 3:
                return "" + vahvuus;
            default:
                return "???";
        }
    }
    
    
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.aseta(3,"kissa") === "Vahvuuden täytyy olla välillä 1-5";
     *   miel.aseta(3,"3")  === null;
     *   
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
                henkiloNro = Mjonot.erota(sb, '$', henkiloNro);
                return null;
            case 2:
                asia = st;
                return null;
            case 3:
                String eiSallittu = "Vahvuuden täytyy olla välillä 1-5";
                String[] sallitut = {"1", "2", "3", "4","5"};
                if (!(Arrays.asList(sallitut).contains(st))) return eiSallittu;
                vahvuus = Mjonot.erotaEx(sb, '§', vahvuus);
                return null;
            /*case 3:
                try {
                    vahvuus = Mjonot.erotaEx(sb, '§', vahvuus);
                } catch (NumberFormatException ex) {
                    return "vahvuus: Ei kokonaisluku ("+st+")";
                }
                return null;*/

            default:
                return "Väärä kentän indeksi";
        }
    }
    
    
    /**
     * Tehdään identtinen klooni henkilöstä
     * @return Object kloonattu henkilö
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.parse("   1   |  1  |   Tekniikka  | 5 ");
     *   Mielenkiinto kopio = miel.clone();
     *   kopio.toString() === miel.toString();
     *   miel.parse("   1   |  1  |   Perhe  | 5 ");
     *   kopio.toString().equals(miel.toString()) === false;
     * </pre>
     */
    @Override
    public Mielenkiinto clone() throws CloneNotSupportedException { 
        return (Mielenkiinto)super.clone();
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Mielenkiinnolle.
     * Vahvuus arvotaan, jotta kahdella mielenkiinnolla ei olisi
     * samoja tietoja.
     * @param nro viite henkilöön, jonka mielenkiinnosta on kyse
     */
    public void vastaaTekniikka(int nro) {
        henkiloNro = nro;
        asia = "Tekniikka";
        vahvuus = rand(1, 5);
    }


    /**
     * Tulostetaan mielenkiinnon tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(asia + " " + vahvuus);
    }


    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa mielenkiinnolle seuraavan rekisterinumeron.
     * @return mielenkiinnon uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Mielenkiinto tekniikka1 = new Mielenkiinto();
     *   tekniikka1.getTunnusNro() === 0;
     *   tekniikka1.rekisteroi();
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palautetaan mielenkiinnon oma id
     * @return mielenkiinnon id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Palautetaan mille henkilölle mielenkiinto kuuluu
     * @return mielenkiinnon id
     */
    public int getHenkiloNro() {
        return henkiloNro;
    }

    
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    

    /**
     * Palauttaa mielenkiinnon tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return mielenkiinto tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Mielenkiinto mielenkiinto = new Mielenkiinto();
     *   mielenkiinto.parse("   1   |  1  |   Tekniikka  | 5 ");
     *   mielenkiinto.toString()    === "1|1|Tekniikka|5";
     * </pre>
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
     }
    
    
    /**
     * n+20+1
     * Selvitää mielenkiinnon tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta mielenkiinnon tiedot otetaan
     * @example
     * <pre name="test">
     *   Mielenkiinto mielenkiinto = new Mielenkiinto();
     *   mielenkiinto.parse("   2   |  1  |   Tekniikka  | 5 ");
     *   mielenkiinto.getHenkiloNro() === 1;
     *   mielenkiinto.toString()    === "2|1|Tekniikka|5";
     *   
     *   mielenkiinto.rekisteroi();
     *   int n = mielenkiinto.getTunnusNro();
     *   mielenkiinto.parse(""+(n+20));
     *   mielenkiinto.rekisteroi();
     *   mielenkiinto.getTunnusNro() === n+20+1;
     *   mielenkiinto.toString()     === "" + (n+20+1) + "|1||5";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    
    
    @Override
    public int hashCode() {
        return tunnusNro;
    }
    
    
    /**
     * Testiohjelma Mielenkiinnolle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Mielenkiinto miel = new Mielenkiinto();
        miel.vastaaTekniikka(2);
        miel.tulosta(System.out);
    }

}