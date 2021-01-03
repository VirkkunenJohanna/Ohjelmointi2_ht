package mainonta;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Mainonta-luokka, joka huolehtii henkilöstöstä.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" henkilöstöön.
 *
 * @author Johanna Virkkunen
 * @version 1.0, 04.03.2019
 * @version 21.04.2019 vaihe 7
 * 
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import mainonta.SailoException;
 *  private Mainonta mainonta;
 *  private Henkilo armi1;
 *  private Henkilo armi2;
 *  private int hid1;
 *  private int hid2;
 *  private Mielenkiinto tekniikka21;
 *  private Mielenkiinto tekniikka11;
 *  private Mielenkiinto tekniikka22; 
 *  private Mielenkiinto tekniikka12; 
 *  private Mielenkiinto tekniikka23;
 *  
 *  @SuppressWarnings("javadoc")
 *  public void alustaMainonta() {
 *    mainonta = new Mainonta();
 *    armi1 = new Henkilo(); armi1.vastaaAnttilaArmi(); armi1.rekisteroi();
 *    armi2 = new Henkilo(); armi2.vastaaAnttilaArmi(); armi2.rekisteroi();
 *    hid1 = armi1.getTunnusNro();
 *    hid2 = armi2.getTunnusNro();
 *    tekniikka21 = new Mielenkiinto(hid2); tekniikka21.vastaaTekniikka(hid2);
 *    tekniikka11 = new Mielenkiinto(hid1); tekniikka11.vastaaTekniikka(hid1);
 *    tekniikka22 = new Mielenkiinto(hid2); tekniikka22.vastaaTekniikka(hid2); 
 *    tekniikka12 = new Mielenkiinto(hid1); tekniikka12.vastaaTekniikka(hid1); 
 *    tekniikka23 = new Mielenkiinto(hid2); tekniikka23.vastaaTekniikka(hid2);
 *    try {
 *    mainonta.lisaa(armi1);
 *    mainonta.lisaa(armi2);
 *    mainonta.lisaa(tekniikka21);
 *    mainonta.lisaa(tekniikka11);
 *    mainonta.lisaa(tekniikka22);
 *    mainonta.lisaa(tekniikka12);
 *    mainonta.lisaa(tekniikka23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
*/
public class Mainonta {
    private Henkilot henkilot = new Henkilot();
    private Mielenkiinnot mielenkiinnot = new Mielenkiinnot(); 


    /**
     * Poistaa henkilöstöstä ja mielenkiinnoista hlö:n tiedot 
     * @param henkilo henkilö joka poistetaan
     * @return montako henkilöä poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaMainonta();
     *   mainonta.etsi("*",0).size() === 2;
     *   mainonta.annaMielenkiinnot(armi1).size() === 2;
     *   mainonta.poista(armi1) === 1;
     *   mainonta.etsi("*",0).size() === 1;
     *   mainonta.annaMielenkiinnot(armi1).size() === 0;
     *   mainonta.annaMielenkiinnot(armi2).size() === 3;
     * </pre>
     */
    public int poista(Henkilo henkilo) {
        if ( henkilo == null ) return 0;
        int ret = henkilot.poista(henkilo.getTunnusNro()); 
        mielenkiinnot.poistaHenkilonMielenkiinnot(henkilo.getTunnusNro()); 
        return ret; 
    }
    
    
    /** 
     * Poistaa tämän mielenkiinnonkohteen 
     * @param mielenkiinto poistettava mielenkiinnonkohde 
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaMainonta();
     *   mainonta.annaMielenkiinnot(armi1).size() === 2;
     *   mainonta.poistaMielenkiinto(tekniikka11);
     *   mainonta.annaMielenkiinnot(armi1).size() === 1;
     */ 
    public void poistaMielenkiinto(Mielenkiinto mielenkiinto) { 
        mielenkiinnot.poista(mielenkiinto); 
    } 


    /**
     * Lisää yritykseen uuden henkilon
     * @param henkilo lisättävä henkilo
     * @throws SailoException jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaMainonta();
     *  mainonta.etsi("*",0).size() === 2;
     *  mainonta.lisaa(armi1);
     *  mainonta.etsi("*",0).size() === 3;
     */
    public void lisaa(Henkilo henkilo) throws SailoException {
        henkilot.lisaa(henkilo);
    }
    
    
    /** 
     * Korvaa henkilön tietorakenteessa.  Ottaa henkilön omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva henkilö.  Jos ei löydy, 
     * niin lisätään uutena henkilönä. 
     * @param henkilo lisättävän henkilön viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaMainonta();
     *  mainonta.etsi("*",0).size() === 2;
     *  mainonta.korvaaTaiLisaa(armi1);
     *  mainonta.etsi("*",0).size() === 2;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Henkilo henkilo) throws SailoException { 
        henkilot.korvaaTaiLisaa(henkilo); 
    } 
    
    
    /** 
     * Korvaa mielenkiinnon tietorakenteessa.  Ottaa mielenkiinnon omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva mielenkiinnonkohde.  Jos ei löydy, 
     * niin lisätään uutena mielenkiinnonkohteena. 
     * @param mielenkiinto lisättävän mielenkiinnon viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Mielenkiinto mielenkiinto) throws SailoException { 
        mielenkiinnot.korvaaTaiLisaa(mielenkiinto); 
    } 
    
    
    /**
     * Listään uusi mielenkiinto mainontaan
     * @param miel lisättävä mielenkiinto
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Mielenkiinto miel) throws SailoException {
        mielenkiinnot.lisaa(miel);
    }


    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien henkilöiden viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä henkilöistä 
     * @throws SailoException Jos jotakin menee väärin
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alustaMainonta();
     *   Henkilo henkilo3 = new Henkilo(); henkilo3.rekisteroi();
     *   henkilo3.aseta(1,"Anttila Assi");
     *   mainonta.lisaa(henkilo3);
     *   Collection<Henkilo> loytyneet = mainonta.etsi("*Assi*",1);
     *   loytyneet.size() === 1;
     *   Iterator<Henkilo> it = loytyneet.iterator();
     *   it.next() == henkilo3 === true; 
     * </pre>
     */ 
    public Collection<Henkilo> etsi(String hakuehto, int k) throws SailoException { 
        return henkilot.etsi(hakuehto, k); 
    } 
    
    
    /**
     * Haetaan kaikki henkilön mielenkiinnot
     * @param henkilo henkilö jolle mielenkiintoja haetaan
     * @return tietorakenne jossa viiteet löydetteyihin mielenkiintoihin
     * @throws SailoException jos tulee ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  alustaMainonta();
     *  Henkilo armi3 = new Henkilo();
     *  armi3.rekisteroi();
     *  mainonta.lisaa(armi3);
     *  
     *  List<Mielenkiinto> loytyneet;
     *  loytyneet = mainonta.annaMielenkiinnot(armi3);
     *  loytyneet.size() === 0; 
     *  loytyneet = mainonta.annaMielenkiinnot(armi1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == tekniikka11 === true;
     *  loytyneet.get(1) == tekniikka12 === true;
     *  loytyneet = mainonta.annaMielenkiinnot(armi2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == tekniikka21 === true;
     * </pre> 
     */
    public List<Mielenkiinto> annaMielenkiinnot(Henkilo henkilo) throws SailoException {
        return mielenkiinnot.annaMielenkiinnot(henkilo.getTunnusNro());
    }


    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        henkilot.setTiedostonPerusNimi(hakemistonNimi + "nimet");
        mielenkiinnot.setTiedostonPerusNimi(hakemistonNimi + "mielenkiinnot");
    }
    

    /**
     * Lukee yrityksen tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     *   
     *  String hakemisto = "testimainoskunkku";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/nimet.dat");
     *  File fhtied = new File(hakemisto+"/mielenkiinnot.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  mainonta = new Mainonta(); // tiedostoja ei ole, tulee poikkeus
     *  mainonta.lueTiedostosta(hakemisto); #THROWS SailoException
     *  alustaMainonta();
     *  mainonta.setTiedosto(hakemisto); // nimi annettava koska uusi poisti sen 
     *  mainonta.tallenna();
     *  mainonta = new Mainonta();
     *  mainonta.lueTiedostosta(hakemisto);
     *  Collection<Henkilo> kaikki = mainonta.etsi("",-1); 
     *  Iterator<Henkilo> it = kaikki.iterator();
     *  it.next() === armi1;
     *  it.next() === armi2;
     *  it.hasNext() === false;
     *  List<Mielenkiinto> loytyneet = mainonta.annaMielenkiinnot(armi1);
     *  Iterator<Mielenkiinto> ih = loytyneet.iterator();
     *  ih.next() === tekniikka11;
     *  ih.next() === tekniikka12;
     *  ih.hasNext() === false;
     *  loytyneet = mainonta.annaMielenkiinnot(armi2);
     *  ih = loytyneet.iterator();
     *  ih.next() === tekniikka21;
     *  ih.next() === tekniikka22;
     *  ih.next() === tekniikka23;
     *  ih.hasNext() === false;
     *  mainonta.lisaa(armi2);
     *  mainonta.lisaa(tekniikka23);
     *  mainonta.tallenna(); // tekee molemmista .bak
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     *  File fbak = new File(hakemisto+"/nimet.bak");
     *  File fhbak = new File(hakemisto+"/mielenkiinnot.bak");
     *  fbak.delete() === true;
     *  fhbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        henkilot = new Henkilot(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
        mielenkiinnot = new Mielenkiinnot();

        setTiedosto(nimi);
        henkilot.lueTiedostosta();
        mielenkiinnot.lueTiedostosta();
    }


    /**
     * Tallentaa yrityksen tiedot tiedostoon.  
     * Vaikka henkilöiden tallettamien epäonistuisi, niin yritetään silti tallettaa
     * mielenkiintoja ennen poikkeuksen heittämistä.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            henkilot.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            mielenkiinnot.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }


    /**
     * Testiohjelma mainontarekisteristä
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Mainonta mainonta = new Mainonta();

        try {
            // mainonta.lueTiedostosta("mainoskunkku");

            Henkilo armi1 = new Henkilo(), armi2 = new Henkilo();
            armi1.rekisteroi();
            armi1.vastaaAnttilaArmi();
            armi2.rekisteroi();
            armi2.vastaaAnttilaArmi();

            mainonta.lisaa(armi1);
            mainonta.lisaa(armi2);
            int id1 = armi1.getTunnusNro();
            int id2 = armi2.getTunnusNro();
            Mielenkiinto tekniikka11 = new Mielenkiinto(id1);
            tekniikka11.vastaaTekniikka(id1);
            mainonta.lisaa(tekniikka11);
            Mielenkiinto tekniikka12 = new Mielenkiinto(id1);
            tekniikka12.vastaaTekniikka(id1);
            mainonta.lisaa(tekniikka12);
            Mielenkiinto tekniikka21 = new Mielenkiinto(id2);
            tekniikka21.vastaaTekniikka(id2);
            mainonta.lisaa(tekniikka21);
            Mielenkiinto tekniikka22 = new Mielenkiinto(id2);
            tekniikka22.vastaaTekniikka(id2);
            mainonta.lisaa(tekniikka22);
            Mielenkiinto tekniikka23 = new Mielenkiinto(id2);
            tekniikka23.vastaaTekniikka(id2);
            mainonta.lisaa(tekniikka23);

            System.out.println("============= Mainontarekisterin testi =================");
            Collection<Henkilo> henkilot = mainonta.etsi("", -1);
            int i = 0;
            for (Henkilo henkilo: henkilot) {
                System.out.println("Henkilö paikassa: " + i);
                henkilo.tulosta(System.out);
                List<Mielenkiinto> loytyneet = mainonta.annaMielenkiinnot(henkilo);
                for (Mielenkiinto mielenkiinto : loytyneet)
                    mielenkiinto.tulosta(System.out);
                i++;
            }

        } catch ( SailoException ex ) {
            System.out.println(ex.getMessage());
        }
    }

}