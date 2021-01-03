package mainonta;

import java.io.*;

import static kanta.HetuTarkistus.*;

/**
 * @author Johanna Virkkunen
 * @version 25.3.2019 Vaiheen 5 korjausta
 *
 */
public class HenkilonMielenkiinto {
    private int tunnusNro;
    private int henkiloNro;
    private int vahvuus;

    private static int seuraavaNro = 1;


    /**
     * Alustetaan mielenkiinto.  Toistaiseksi ei tarvitse tehdä mitään
     */
    public HenkilonMielenkiinto() {
        // Vielä ei tarvita mitään
    }


    /**
     * Alustetaan tietyn henkilön mielenkiinto.  
     * @param henkiloNro henkilön viitenumero 
     */
    public HenkilonMielenkiinto(int henkiloNro) {
        this.henkiloNro = henkiloNro;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Mielenkiinnolle.
     * Vahvuus arvotaan, jotta kahdella mielenkiinnolla ei olisi
     * samoja tietoja.
     * @param nro viite henkilöön, jonka mielenkiinnosta on kyse
     */
    public void vastaaTekniikka(int nro) {
        henkiloNro = nro;
        vahvuus = rand(1, 5);
    }


    /**
     * Tulostetaan mielenkiinnon tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Vahvuus on :" + vahvuus + " " + "\nHenkilon numero on:" + henkiloNro + " " + "\nMielenkiinnon numero on:" + tunnusNro + "\n-----------------------------------");
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
        henkiloNro = seuraavaNro;
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
     * @return henkilön id
     */
    public int getHenkiloNro() {
        return henkiloNro;
    }


    /**
     * Testiohjelma Mielenkiinnolle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        HenkilonMielenkiinto miel = new HenkilonMielenkiinto();
        miel.vastaaTekniikka(2);
        miel.tulosta(System.out);
        
        HenkilonMielenkiinto miel2 = new HenkilonMielenkiinto();
        miel2.vastaaTekniikka(3);
        miel2.tulosta(System.out);
    }

}