package mainonta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Mainontarekisterin mielenkiinnot, joka osaa mm. lisätä uuden mielenkiinnon
 *
 * @author Johanna Virkkunen
 * @version 5.3.2019 vaihe 5
 * @version 30.3.2019 vaihe 6 aloitettu
 * @version 4.4.2019 vaihe 6 jatkettu 
 * @version 21.04.2019 vaihe 7
 */
public class Mielenkiinnot implements Iterable<Mielenkiinto> {
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";

    /** Taulukko mielenkiinnoista */
    private final List<Mielenkiinto> alkiot        = new ArrayList<Mielenkiinto>();


    /**
     * Mielenkiintojen alustaminen
     */
    public Mielenkiinnot() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden mielenkiinnon tietorakenteeseen.  Ottaa mielenkiinnnon omistukseensa.
     * @param miel lisättävä mielenkiinto.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Mielenkiinto miel) {
        alkiot.add(miel);
        muutettu = true;
    }
    
    
    /**
     * Korvaa mielenkiinnon tietorakenteessa.  Ottaa mielenkiinnon omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva mielenkiinto.  Jos ei löydy,
     * niin lisätään uutena mielenkiintona.
     * @param mielenkiinto lisättävän mielenkiinnon viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
     * Mielenkiinto miel1 = new Mielenkiinto(), miel2 = new Mielenkiinto();
     * miel1.rekisteroi(); miel2.rekisteroi();
     * mielenkiinnot.getLkm() === 0;
     * mielenkiinnot.korvaaTaiLisaa(miel1); mielenkiinnot.getLkm() === 1;
     * mielenkiinnot.korvaaTaiLisaa(miel2); mielenkiinnot.getLkm() === 2;
     * Mielenkiinto miel3 = miel1.clone();
     * miel3.aseta(2,"kkk");
     * Iterator<Mielenkiinto> i2=mielenkiinnot.iterator();
     * i2.next() === miel1;
     * mielenkiinnot.korvaaTaiLisaa(miel3); mielenkiinnot.getLkm() === 2;
     * i2=mielenkiinnot.iterator();
     * Mielenkiinto m = i2.next();
     * m === miel3;
     * m == miel3 === true;
     * m == miel1 === false;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Mielenkiinto mielenkiinto) throws SailoException {
        int id = mielenkiinto.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, mielenkiinto);
                muutettu = true;
                return;
            }
        }
        lisaa(mielenkiinto);
    }
    
    
    /**
     * Poistaa valitun mielenkiinnon
     * @param mielenkiinto poistettava mielenkiinto
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
     *  Mielenkiinto tekniikka21 = new Mielenkiinto(); tekniikka21.vastaaTekniikka(2);
     *  Mielenkiinto tekniikka11 = new Mielenkiinto(); tekniikka11.vastaaTekniikka(1);
     *  Mielenkiinto tekniikka22 = new Mielenkiinto(); tekniikka22.vastaaTekniikka(2); 
     *  Mielenkiinto tekniikka12 = new Mielenkiinto(); tekniikka12.vastaaTekniikka(1); 
     *  Mielenkiinto tekniikka23 = new Mielenkiinto(); tekniikka23.vastaaTekniikka(2); 
     *  mielenkiinnot.lisaa(tekniikka21);
     *  mielenkiinnot.lisaa(tekniikka11);
     *  mielenkiinnot.lisaa(tekniikka22);
     *  mielenkiinnot.lisaa(tekniikka12);
     *  mielenkiinnot.poista(tekniikka23) === false; mielenkiinnot.getLkm() === 4;
     *  mielenkiinnot.poista(tekniikka11) === true;   mielenkiinnot.getLkm() === 3;
     *  List<Mielenkiinto> m = mielenkiinnot.annaMielenkiinnot(1);
     *  m.size() === 1; 
     *  m.get(0) === tekniikka12;
     * </pre>
     */
    public boolean poista(Mielenkiinto mielenkiinto) {
        boolean ret = alkiot.remove(mielenkiinto);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn henkilön mielenkiinnot
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
     *  Mielenkiinto tekniikka21 = new Mielenkiinto(); tekniikka21.vastaaTekniikka(2);
     *  Mielenkiinto tekniikka11 = new Mielenkiinto(); tekniikka11.vastaaTekniikka(1);
     *  Mielenkiinto tekniikka22 = new Mielenkiinto(); tekniikka22.vastaaTekniikka(2); 
     *  Mielenkiinto tekniikka12 = new Mielenkiinto(); tekniikka12.vastaaTekniikka(1); 
     *  Mielenkiinto tekniikka23 = new Mielenkiinto(); tekniikka23.vastaaTekniikka(2); 
     *  mielenkiinnot.lisaa(tekniikka21);
     *  mielenkiinnot.lisaa(tekniikka11);
     *  mielenkiinnot.lisaa(tekniikka22);
     *  mielenkiinnot.lisaa(tekniikka12);
     *  mielenkiinnot.lisaa(tekniikka23);
     *  mielenkiinnot.poistaHenkilonMielenkiinnot(2) === 3;  mielenkiinnot.getLkm() === 2;
     *  mielenkiinnot.poistaHenkilonMielenkiinnot(3) === 0;  mielenkiinnot.getLkm() === 2;
     *  List<Mielenkiinto> m = mielenkiinnot.annaMielenkiinnot(2);
     *  m.size() === 0; 
     *  m = mielenkiinnot.annaMielenkiinnot(1);
     *  m.get(0) === tekniikka11;
     *  m.get(1) === tekniikka12;
     * </pre>
     */
    public int poistaHenkilonMielenkiinnot(int tunnusNro) {
        int n = 0;
        for (Iterator<Mielenkiinto> it = alkiot.iterator(); it.hasNext();) {
            Mielenkiinto miel = it.next();
            if ( miel.getHenkiloNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }

    
    
    /**
     * Lukee mielenkiinnot tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
     *  Mielenkiinto tekniikka21 = new Mielenkiinto(); tekniikka21.vastaaTekniikka(2);
     *  Mielenkiinto tekniikka11 = new Mielenkiinto(); tekniikka11.vastaaTekniikka(1);
     *  Mielenkiinto tekniikka22 = new Mielenkiinto(); tekniikka22.vastaaTekniikka(2); 
     *  Mielenkiinto tekniikka12 = new Mielenkiinto(); tekniikka12.vastaaTekniikka(1); 
     *  Mielenkiinto tekniikka23 = new Mielenkiinto(); tekniikka23.vastaaTekniikka(2); 
     *  String tiedNimi = "testimainoskunkku";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  mielenkiinnot.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  mielenkiinnot.lisaa(tekniikka21);
     *  mielenkiinnot.lisaa(tekniikka11);
     *  mielenkiinnot.lisaa(tekniikka22);
     *  mielenkiinnot.lisaa(tekniikka12);
     *  mielenkiinnot.lisaa(tekniikka23);
     *  mielenkiinnot.tallenna();
     *  mielenkiinnot = new Mielenkiinnot();
     *  mielenkiinnot.lueTiedostosta(tiedNimi);
     *  Iterator<Mielenkiinto> i = mielenkiinnot.iterator();
     *  i.next().toString() === tekniikka21.toString();
     *  i.next().toString() === tekniikka11.toString();
     *  i.next().toString() === tekniikka22.toString();
     *  i.next().toString() === tekniikka12.toString();
     *  i.next().toString() === tekniikka23.toString();
     *  i.hasNext() === false;
     *  mielenkiinnot.lisaa(tekniikka23);
     *  mielenkiinnot.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Mielenkiinto miel = new Mielenkiinto();
                miel.parse(rivi); // voisi olla virhekäsittely
                lisaa(miel);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }

    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa mielenkiinnot tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Mielenkiinto miel : this) {
                fo.println(miel.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    
    /**
     * Palauttaa yrityksen henkilöiden mielenkiintojen lukumäärän
     * @return mielenkiintojen lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
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
     *  Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
     *  Mielenkiinto tekniikka21 = new Mielenkiinto(2); mielenkiinnot.lisaa(tekniikka21);
     *  Mielenkiinto tekniikka11 = new Mielenkiinto(1); mielenkiinnot.lisaa(tekniikka11);
     *  Mielenkiinto tekniikka22 = new Mielenkiinto(2); mielenkiinnot.lisaa(tekniikka22);
     *  Mielenkiinto tekniikka12 = new Mielenkiinto(1); mielenkiinnot.lisaa(tekniikka12);
     *  Mielenkiinto tekniikka23 = new Mielenkiinto(2); mielenkiinnot.lisaa(tekniikka23);
     * 
     *  Iterator<Mielenkiinto> i2=mielenkiinnot.iterator();
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
     *  for ( Mielenkiinto miel:mielenkiinnot ) { 
     *    miel.getHenkiloNro() === hnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Mielenkiinto> iterator() {
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
     *  Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
     *  Mielenkiinto tekniikka21 = new Mielenkiinto(2); mielenkiinnot.lisaa(tekniikka21);
     *  Mielenkiinto tekniikka11 = new Mielenkiinto(1); mielenkiinnot.lisaa(tekniikka11);
     *  Mielenkiinto tekniikka22 = new Mielenkiinto(2); mielenkiinnot.lisaa(tekniikka22);
     *  Mielenkiinto tekniikka12 = new Mielenkiinto(1); mielenkiinnot.lisaa(tekniikka12);
     *  Mielenkiinto tekniikka23 = new Mielenkiinto(2); mielenkiinnot.lisaa(tekniikka23);
     *  Mielenkiinto tekniikka51 = new Mielenkiinto(5); mielenkiinnot.lisaa(tekniikka51);
     *  
     *  List<Mielenkiinto> loytyneet;
     *  loytyneet = mielenkiinnot.annaMielenkiinnot(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = mielenkiinnot.annaMielenkiinnot(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == tekniikka11 === true;
     *  loytyneet.get(1) == tekniikka12 === true;
     *  loytyneet = mielenkiinnot.annaMielenkiinnot(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == tekniikka51 === true;
     * </pre> 
     */
    public List<Mielenkiinto> annaMielenkiinnot(int tunnusnro) {
        List<Mielenkiinto> loydetyt = new ArrayList<Mielenkiinto>();
        for (Mielenkiinto miel : alkiot)
            if ( miel.getHenkiloNro() == tunnusnro ) loydetyt.add(miel);
        return loydetyt;
    }
    

    /**
     * Testiohjelma mielenkiinnoille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Mielenkiinnot mielenkiinnot = new Mielenkiinnot();
        Mielenkiinto tekniikka1 = new Mielenkiinto();
        tekniikka1.vastaaTekniikka(2);
        Mielenkiinto tekniikka2 = new Mielenkiinto();
        tekniikka2.vastaaTekniikka(1);
        Mielenkiinto tekniikka3 = new Mielenkiinto();
        tekniikka3.vastaaTekniikka(2);
        Mielenkiinto tekniikka4 = new Mielenkiinto();
        tekniikka4.vastaaTekniikka(2);

        mielenkiinnot.lisaa(tekniikka1);
        mielenkiinnot.lisaa(tekniikka2);
        mielenkiinnot.lisaa(tekniikka3);
        mielenkiinnot.lisaa(tekniikka2);
        mielenkiinnot.lisaa(tekniikka4);

        System.out.println("============= Mielenkiinnot testi =================");

        List<Mielenkiinto> mielenkiinnot2 = mielenkiinnot.annaMielenkiinnot(2);

        for (Mielenkiinto miel : mielenkiinnot2) {
            System.out.print(miel.getHenkiloNro() + " ");
            miel.tulosta(System.out);
        }

    }

}