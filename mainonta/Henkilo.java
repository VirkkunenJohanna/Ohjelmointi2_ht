package mainonta;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator; 
import fi.jyu.mit.ohj2.Mjonot;

import kanta.HetuTarkistus;
import kanta.Tietue;

import static kanta.HetuTarkistus.*;

/**
 * Mainontarekisterin henkilö joka osaa mm. itse huolehtia tunnusNro:staan.
 *
 * @author Johanna Virkkunen
 * @version 4.3.2019 vaihe 5.1
 * @version 30.3.2019 vaihe 6 aloitettu
 * @version 4.4.2019 vaihe 6 jatkettu 
 * @version 21.04.2019 vaihe 7
 */
public class Henkilo implements Cloneable, Tietue {
    private int        tunnusNro;
    private String     nimi           = "";
    private String     hetu           = "";
    //private int        ika            = 0;
    private String     status         = "";
    private String     puhelinnumero  = "";
    private String     sposti         = "";
    //private String     kaupunki         = "";

    private static int seuraavaNro    = 1;
    
    
    /** 
     * Henkilöiden vertailija 
     */ 
    public static class Vertailija implements Comparator<Henkilo> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Henkilo henkilo1, Henkilo henkilo2) { 
            return henkilo1.getAvain(k).compareToIgnoreCase(henkilo2.getAvain(k)); 
        } 
    } 
     
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + tunnusNro; 
        case 1: return "" + nimi.toUpperCase(); 
        case 2: return "" + hetu; // vaihda vuosi ja pvm keskenään 
        case 3: return "" + status; 
        case 4: return "" + puhelinnumero; 
        case 5: return "" + sposti; 
        default: return "Höh"; 
        } 
    } 
    
    
    /**
     * Palauttaa henkilön kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 6;
    }
    
    
    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return ekan kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Alustetaan henkilön merkkijono-attribuuti tyhjiksi jonoiksi
     * ja tunnusnro = 0.
     */
    public Henkilo() {
        // Toistaiseksi ei tarvita mitään
    }


    /**
     * @return henkilön nimi
     * @example
     * <pre name="test">
     *   Henkilo armi = new Henkilo();
     *   armi.vastaaAnttilaArmi();
     *   armi.getNimi() =R= "Anttila Armi .*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + hetu;
        case 3: return "" + status;
        case 4: return "" + puhelinnumero;
        case 5: return "" + sposti;
        default: return "Höh";
        }
    }
    
    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Henkilo henkilo = new Henkilo();
     *   henkilo.aseta(1,"Anttila Armi") === null;
     *   henkilo.aseta(2,"kissa") =R= "Hetu liian lyhyt"
     *   henkilo.aseta(2,"030201-1111") === "Tarkistusmerkin kuuluisi olla C"; 
     *   henkilo.aseta(2,"030201-111C") === null; 
     *   henkilo.aseta(3,"haaveilija") === "Status on virheellinen";
     *   henkilo.aseta(5,"kissa.koira@miau.fi") === null;
     *   henkilo.aseta(5,"kissa.koira@miau") === "Sähköpostiosoite on puutteellinen";
     *   henkilo.aseta(4,"") === "Liian vähän numeroita";
     * </pre>
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            String kelvotonNimi = "Nimi on tyhjä tai sisältää numeroita";
            if (tjono.length() <= 0) return kelvotonNimi;
            nimi = tjono;
            return null;
        case 2:
            HetuTarkistus hetut = new HetuTarkistus();
            String virhe = hetut.tarkista(tjono);
            if ( virhe != null ) return virhe;
            hetu = tjono;
            return null;
        case 3:
            String kelvotonStatus = "Status on virheellinen";
            String[] statukset = {"opiskelija", "eläkeläinen", "töissä", "työtön", "muu"};
            if (!(Arrays.asList(statukset).contains(tjono))) return kelvotonStatus;
            status = tjono;
            return null;
        case 4:
            String vajaa = "Liian vähän numeroita";
            if ( tjono.length() < 6 ) return vajaa;
            puhelinnumero = tjono;
            return null;
        case 5:
            String vaaraMuoto = "Sähköpostiosoite on puutteellinen";
            if ( !(tjono.matches("(.*)@(.*)\\.(.*)"))) return vaaraMuoto;
            sposti = tjono;
            return null;
        default:
            return "Höh";
        }
    }
    
    
    /**
     * Palauttaa k:tta henkilön kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnusnro";
        case 1: return "nimi";
        case 2: return "hetu";
        case 3: return "status";
        case 4: return "puhelinnumero";
        case 5: return "sposti";
        default: return "Höh";
        }
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot henkilölle.
     * @param apuhetu hetu joka annetaan henkilölle 
     */
    public void vastaaAnttilaArmi(String apuhetu) {
        
        nimi           = "Anttila Armi" + " " + rand(1000,9999);
        hetu           = apuhetu;
        //ika            = 20; 
        status         = "opiskelija";
        puhelinnumero  = "0401234567";
        sposti         = "armianttila@student.jyu.fi";
        //kaupunki         = "Porvoo";
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot henkilölle.
     * Henkilötunnus arvotaan, jotta kahdella henkilöllä ei olisi
     * samoja tietoja.
     */
    public void vastaaAnttilaArmi() {
        String apuhetu = arvoHetu();
        vastaaAnttilaArmi(apuhetu);
    }


    /**
     * Tulostetaan henkilön tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro, 3) + "  " + nimi + "  "
                + hetu);
        //out.println("Ikä: " + ika);
        out.println("Status: " + status);
        out.println("Puhelin: " + puhelinnumero);
        out.println("Sähköposti: " + sposti);
        //out.println("Kaupunki: " + kaupunki);
    }


    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa henkilölle seuraavan rekisterinumeron.
     * @return henkilön uusi tunnusNro
     * @example
     * <pre name="test">
     *   Henkilo armi1 = new Henkilo();
     *   armi1.getTunnusNro() === 0;
     *   armi1.rekisteroi();
     *   Henkilo armi2 = new Henkilo();
     *   armi2.rekisteroi();
     *   int n1 = armi1.getTunnusNro();
     *   int n2 = armi2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palauttaa henkilön tunnusnumeron.
     * @return henkilön tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }

    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    
    /**
     * Palauttaa henkilön tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return henkilö tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Henkilo henkilo = new Henkilo();
     *   henkilo.parse("   1  |  Anttila Armi   | 030201-111C");
     *   henkilo.toString().startsWith("1|Anttila Armi|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
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
     * Selvitää henkilön tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta henkilön tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Henkilo henkilo = new Henkilo();
     *   henkilo.parse("   1  |  Anttila Armi   | 030201-111C");
     *   henkilo.getTunnusNro() === 1;
     *   henkilo.toString().startsWith("1|Anttila Armi|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   henkilo.rekisteroi();
     *   int n = henkilo.getTunnusNro();
     *   henkilo.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   henkilo.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   henkilo.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    
    
    /**
     * Tehdään identtinen klooni henkilöstä
     * @return Object kloonattu henkilö
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Henkilo henkilo = new Henkilo();
     *   henkilo.parse("   3  |  Anttila Armi   | 123");
     *   Henkilo kopio = henkilo.clone();
     *   kopio.toString() === henkilo.toString();
     *   henkilo.parse("   4  |  Anttila Assi   | 123");
     *   kopio.toString().equals(henkilo.toString()) === false;
     * </pre>
     */
    @Override
    public Henkilo clone() throws CloneNotSupportedException {
        Henkilo uusi;
        uusi = (Henkilo) super.clone();
        return uusi;
    }
    
    
    /**
     * Tutkii onko henkilön tiedot samat kuin parametrina tuodun henkilön tiedot
     * @param henkilo henkilö johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Henkilo henkilo1 = new Henkilo();
     *   henkilo1.parse("   3  |  Anttila Armi   | 030201-111C");
     *   Henkilo henkilo2 = new Henkilo();
     *   henkilo2.parse("   3  |  Anttila Armi   | 030201-111C");
     *   Henkilo henkilo3 = new Henkilo();
     *   henkilo3.parse("   3  |  Anttila Armi   | 030201-115H");
     *   
     *   henkilo1.equals(henkilo2) === true;
     *   henkilo2.equals(henkilo1) === true;
     *   henkilo1.equals(henkilo3) === false;
     *   henkilo3.equals(henkilo2) === false;
     * </pre>
     */
    public boolean equals(Henkilo henkilo) {
        if ( henkilo == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(henkilo.anna(k)) ) return false;
        return true;
    }
    
    
    @Override
    public boolean equals(Object henkilo) {
        if ( henkilo instanceof Henkilo ) return equals((Henkilo)henkilo);
        return false;
    }


    @Override
    public int hashCode() {
        return tunnusNro;
    }
    

    /**
     * Testiohjelma henkilölle.
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Henkilo armi = new Henkilo(), armi2 = new Henkilo();
        armi.rekisteroi();
        armi2.rekisteroi();
        armi.tulosta(System.out);
        armi.vastaaAnttilaArmi();
        armi.tulosta(System.out);

        armi.vastaaAnttilaArmi();
        armi2.tulosta(System.out);

        armi2.vastaaAnttilaArmi();
        armi2.tulosta(System.out);
    }

}