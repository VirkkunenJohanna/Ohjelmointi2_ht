package mainonta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Johanna Virkkunen
 * @version 25.3.2019 Vaiheen 5 korjausta
 *
 */
public class HenkiloidenMielenkiinnot implements Iterable<HenkilonMielenkiinto> {

    private String                      tiedostonNimi = "";

    /** Taulukko mielenkiinnoista */
    private final Collection<HenkilonMielenkiinto> alkiot        = new ArrayList<HenkilonMielenkiinto>();


    /**
     * Mielenkiintojen alustaminen
     */
    public HenkiloidenMielenkiinnot() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden mielenkiinnon tietorakenteeseen.  Ottaa mielenkiinnnon omistukseensa.
     * @param miel lisättävä mielenkiinto.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(HenkilonMielenkiinto miel) {
        alkiot.add(miel);
    }


    /**
     * Lukee henkilöstön tiedostosta.  
     * TODO Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + ".miel";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa henkilöstön tiedostoon.  
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa mainontarekisterin mielenkiintojen lukumäärän
     * @return mielenkiintojen lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien mielenkiintojen läpikäymiseen
     * @return mielenkiintoiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  HenkiloidenMielenkiinnot henkiloidenmielenkiinnot = new HenkiloidenMielenkiinnot();
     *  HenkilonMielenkiinto tekniikka21 = new HenkilonMielenkiinto(2); henkiloidenmielenkiinnot.lisaa(tekniikka21);
     *  HenkilonMielenkiinto tekniikka11 = new HenkilonMielenkiinto(1); henkiloidenmielenkiinnot.lisaa(tekniikka11);
     *  HenkilonMielenkiinto tekniikka22 = new HenkilonMielenkiinto(2); henkiloidenmielenkiinnot.lisaa(tekniikka22);
     *  HenkilonMielenkiinto tekniikka12 = new HenkilonMielenkiinto(1); henkiloidenmielenkiinnot.lisaa(tekniikka12);
     *  HenkilonMielenkiinto tekniikka23 = new HenkilonMielenkiinto(2); henkiloidenmielenkiinnot.lisaa(tekniikka23);
     * 
     *  Iterator<HenkilonMielenkiinto> i2=henkiloidenmielenkiinnot.iterator();
     *  i2.next() === tekniikka21;
     *  i2.next() === tekniikka11;
     *  i2.next() === tekniikka22;
     *  i2.next() === tekniikka12;
     *  i2.next() === tekniikka23;
     *  i2.next() === tekniikka12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int hnrot[] = {2,1,2,1,2};
     *  
     *  for ( HenkilonMielenkiinto miel:henkiloidenmielenkiinnot ) { 
     *    miel.getHenkiloNro() === hnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<HenkilonMielenkiinto> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki henkilön mielenkiinnot
     * @param tunnusnro henkilön tunnusnumero jolle mielenkiintoja haetaan
     * @return tietorakenne jossa viiteet löydetteyihin mielenkiintoihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  HenkiloidenMielenkiinnot henkiloidenmielenkiinnot = new HenkiloidenMielenkiinnot();
     *  HenkilonMielenkiinto tekniikka21 = new HenkilonMielenkiinto(2); henkiloidenmielenkiinnot.lisaa(tekniikka21);
     *  HenkilonMielenkiinto tekniikka11 = new HenkilonMielenkiinto(1); henkiloidenmielenkiinnot.lisaa(tekniikka11);
     *  HenkilonMielenkiinto tekniikka22 = new HenkilonMielenkiinto(2); henkiloidenmielenkiinnot.lisaa(tekniikka22);
     *  HenkilonMielenkiinto tekniikka12 = new HenkilonMielenkiinto(1); henkiloidenmielenkiinnot.lisaa(tekniikka12);
     *  HenkilonMielenkiinto tekniikka23 = new HenkilonMielenkiinto(2); henkiloidenmielenkiinnot.lisaa(tekniikka23);
     *  HenkilonMielenkiinto tekniikka51 = new HenkilonMielenkiinto(5); henkiloidenmielenkiinnot.lisaa(tekniikka51);
     *  
     *  List<HenkilonMielenkiinto> loytyneet;
     *  loytyneet = henkiloidenmielenkiinnot.annaHenkiloidenMielenkiinnot(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = henkiloidenmielenkiinnot.annaHenkiloidenMielenkiinnot(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == tekniikka11 === true;
     *  loytyneet.get(1) == tekniikka12 === true;
     *  loytyneet = henkiloidenmielenkiinnot.annaHenkiloidenMielenkiinnot(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == tekniikka51 === true;
     * </pre> 
     */
    public List<HenkilonMielenkiinto> annaHenkiloidenMielenkiinnot(int tunnusnro) {
        List<HenkilonMielenkiinto> loydetyt = new ArrayList<HenkilonMielenkiinto>();
        for (HenkilonMielenkiinto miel : alkiot)
            if (miel.getHenkiloNro() == tunnusnro) loydetyt.add(miel);
        return loydetyt;
    }


    /**
     * Testiohjelma mielenkiinnoille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        HenkiloidenMielenkiinnot henkiloidenmielenkiinnot = new HenkiloidenMielenkiinnot();
        HenkilonMielenkiinto tekniikka1 = new HenkilonMielenkiinto();
        tekniikka1.vastaaTekniikka(2);
        HenkilonMielenkiinto tekniikka2 = new HenkilonMielenkiinto();
        tekniikka2.vastaaTekniikka(1);
        HenkilonMielenkiinto tekniikka3 = new HenkilonMielenkiinto();
        tekniikka3.vastaaTekniikka(2);
        HenkilonMielenkiinto tekniikka4 = new HenkilonMielenkiinto();
        tekniikka4.vastaaTekniikka(2);

        henkiloidenmielenkiinnot.lisaa(tekniikka1);
        henkiloidenmielenkiinnot.lisaa(tekniikka2);
        henkiloidenmielenkiinnot.lisaa(tekniikka3);
        henkiloidenmielenkiinnot.lisaa(tekniikka2);
        henkiloidenmielenkiinnot.lisaa(tekniikka4);

        System.out.println("============= Mielenkiinnot testi =================");

        List<HenkilonMielenkiinto> henkiloidenmielenkiinnot2 = henkiloidenmielenkiinnot.annaHenkiloidenMielenkiinnot(2);

        for (HenkilonMielenkiinto miel : henkiloidenmielenkiinnot2) {
            System.out.print(miel.getHenkiloNro() + " ");
            miel.tulosta(System.out);
        }

    }

}