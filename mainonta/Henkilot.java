package mainonta;

import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import fi.jyu.mit.ohj2.WildChars; 

/**
 * Mainontayrityksen henkilöstö joka osaa mm. lisätä uuden henkilön
 *
 * @author Johanna Virkkunen
 * @version 4.3.2019 vaihe 5.1
 * @version 30.3.2019 vaihe 6 aloitettu
 * @version 4.4.2019 vaihe 6 jatkettu 
 * @version 21.04.2019 vaihe 7
 */
public class Henkilot implements Iterable<Henkilo> {
    private static final int MAX_HENKILOITA = 15;
    private boolean muutettu = false;
    private int lkm = 0; 
    private String kokoNimi = ""; 
    private String tiedostonPerusNimi = "nimet"; 
    private Henkilo alkiot[] = new Henkilo[MAX_HENKILOITA];


    /**
     * Oletusmuodostaja
     */
    public Henkilot() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden henkilön tietorakenteeseen.  Ottaa henkilön omistukseensa.
     * @param henkilo lisättävän henkilön viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Henkilot henkilot = new Henkilot();
     * Henkilo armi1 = new Henkilo(), armi2 = new Henkilo();
     * henkilot.getLkm() === 0;
     * henkilot.lisaa(armi1); henkilot.getLkm() === 1;
     * henkilot.lisaa(armi2); henkilot.getLkm() === 2;
     * henkilot.lisaa(armi1); henkilot.getLkm() === 3;
     * Iterator<Henkilo> it = henkilot.iterator(); 
     * it.next() === armi1;
     * it.next() === armi2; 
     * it.next() === armi1;  
     * henkilot.lisaa(armi1); henkilot.getLkm() === 4;
     * henkilot.lisaa(armi1); henkilot.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Henkilo henkilo) throws SailoException {
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
        alkiot[lkm] = henkilo;
        lkm++;
        muutettu = true;
    }
    
    
    /**
     * Korvaa henkilön tietorakenteessa.  Ottaa henkilön omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva henkilö.  Jos ei löydy,
     * niin lisätään uutena henkilönä.
     * @param henkilo lisätäävän henkilön viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Henkilot henkilot = new Henkilot();
     * Henkilo armi1 = new Henkilo(), armi2 = new Henkilo();
     * armi1.rekisteroi(); armi2.rekisteroi();
     * henkilot.getLkm() === 0;
     * henkilot.korvaaTaiLisaa(armi1); henkilot.getLkm() === 1;
     * henkilot.korvaaTaiLisaa(armi2); henkilot.getLkm() === 2;
     * Henkilo armi3 = armi1.clone();
     * armi3.aseta(3,"0401234567");
     * Iterator<Henkilo> it = henkilot.iterator();
     * it.next() == armi1 === true;
     * henkilot.korvaaTaiLisaa(armi3); henkilot.getLkm() === 2;
     * it = henkilot.iterator();
     * Henkilo h0 = it.next();
     * h0 === armi3;
     * h0 == armi3 === true;
     * h0 == armi1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Henkilo henkilo) throws SailoException {
        int id = henkilo.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = henkilo;
                muutettu = true;
                return;
            }
        }
        lisaa(henkilo);
    }


    /**
     * Palauttaa viitteen i:teen henkilöön.
     * @param i monennenko henkilön viite halutaan
     * @return viite henkilöön, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    protected Henkilo anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /** 
     * Poistaa henkilön jolla on valittu tunnusnumero  
     * @param id poistettavan henkilön tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Henkilot henkilot = new Henkilot(); 
     * Henkilo armi1 = new Henkilo(), armi2 = new Henkilo(), armi3 = new Henkilo(); 
     * armi1.rekisteroi(); armi2.rekisteroi(); armi3.rekisteroi(); 
     * int id1 = armi1.getTunnusNro(); 
     * henkilot.lisaa(armi1); henkilot.lisaa(armi2); henkilot.lisaa(armi3); 
     * henkilot.poista(id1+1) === 1; 
     * henkilot.annaId(id1+1) === null; henkilot.getLkm() === 2; 
     * henkilot.poista(id1) === 1; henkilot.getLkm() === 1; 
     * henkilot.poista(id1+3) === 0; henkilot.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Lukee henkilöstön tiedostosta. 
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Henkilot henkilot = new Henkilot();
     *  Henkilo armi1 = new Henkilo(), armi2 = new Henkilo();
     *  armi1.vastaaAnttilaArmi();
     *  armi2.vastaaAnttilaArmi();
     *  String hakemisto = "testimainoskunkku";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  henkilot.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  henkilot.lisaa(armi1);
     *  henkilot.lisaa(armi2);
     *  henkilot.tallenna();
     *  henkilot = new Henkilot();            // Poistetaan vanhat luomalla uusi
     *  henkilot.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Henkilo> i = henkilot.iterator();
     *  i.next() === armi1;
     *  i.next() === armi2;
     *  i.hasNext() === false;
     *  henkilot.lisaa(armi2);
     *  henkilot.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if ( kokoNimi == null ) throw new SailoException("Yrityksen nimi puuttuu");
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Henkilo henkilo = new Henkilo();
                henkilo.parse(rivi); // voisi olla virhekäsittely
                lisaa(henkilo);
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
     * Tallentaa henkilöstön tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * Mainoskunkku
     * 7
     * ; kommenttirivi
     * 1|Anttila Armi|121103-706Y|opiskelija|0401234567|armianttila@student.jyu.fi
     * 2|Anttila Assi|121103-706Y|eläkeläinen|0501234567|anttilaassi@hotmail.com
     * </pre>
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Henkilo henkilo : this) {
                fo.println(henkilo.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
            //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
        
       /* if ( !muutettu ) return;
    
        File ftied = new File(getTiedostonNimi());
        if (!ftied.exists()) {
         try {
             ftied.createNewFile();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         }
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.length);
            for (Henkilo henkilo : this) {
                fo.println(henkilo.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
 
        muutettu = false;
     }*/
    
    
    /**
     * Palauttaa Yrityksen koko nimen
     * @return Yrityksen koko nimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    
    /**
     * Palauttaa yrityksen henkilöiden lukumäärän
     * @return henkilöiden lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }

    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    
    /**
     * Luokka henkilöiden iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Henkilot henkilot = new Henkilot();
     * Henkilo armi1 = new Henkilo(), armi2 = new Henkilo();
     * armi1.rekisteroi(); armi2.rekisteroi();
     *
     * henkilot.lisaa(armi1); 
     * henkilot.lisaa(armi2); 
     * henkilot.lisaa(armi1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Henkilo henkilo:henkilot)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+henkilo.getTunnusNro());           
     * 
     * String tulos = " " + armi1.getTunnusNro() + " " + armi2.getTunnusNro() + " " + armi1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Henkilo>  i=henkilot.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Henkilo henkilo = i.next();
     *   ids.append(" "+henkilo.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Henkilo>  i=henkilot.iterator();
     * i.next() == armi1  === true;
     * i.next() == armi2  === true;
     * i.next() == armi1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class HenkilotIterator implements Iterator<Henkilo> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa henkilöä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä henkilöitä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        
        /**
         * Annetaan seuraava henkilö
         * @return seuraava henkilö
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Henkilo next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    
    
    /**
     * Palautetaan iteraattori henkilöistään.
     * @return henkilö iteraattori
     */
    @Override
    public Iterator<Henkilo> iterator() {
        return new HenkilotIterator();
    }
    
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien henkilöiden viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä henkilöistä 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException    
     *   Henkilot henkilot = new Henkilot(); 
     *   Henkilo henkilo1 = new Henkilo(); henkilo1.parse("1|Anttila Armi|121103-706Y|opiskelija|0401234567|armianttila@student.jyu.fi"); 
     *   Henkilo henkilo2 = new Henkilo(); henkilo2.parse("2|Anttila Assi|121103-706Y|eläkeläinen|0501234567|anttilaassi@hotmail.com");
     *   henkilot.lisaa(henkilo1); henkilot.lisaa(henkilo2);
     *   List<Henkilo> loytyneet;  
     *   loytyneet = (List<Henkilo>)henkilot.etsi("*s*",1);  
     *   loytyneet.size() === 1;  
     *   loytyneet.get(0) == henkilo2 === true;  
     *     
     *   loytyneet = (List<Henkilo>)henkilot.etsi("*op*",3);  
     *   loytyneet.size() === 1;  
     *   loytyneet.get(0) == henkilo1 === true;   
     *     
     *   loytyneet = (List<Henkilo>)henkilot.etsi(null,-1);  
     *   loytyneet.size() === 2;  
     * </pre> 
     */ 
    public Collection<Henkilo> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 0; // jotta etsii id:n mukaan
        List<Henkilo> loytyneet = new ArrayList<Henkilo>(); 
        for (Henkilo henkilo : this) { 
            if (WildChars.onkoSamat(henkilo.anna(hk), ehto)) loytyneet.add(henkilo);   
        } 
        Collections.sort(loytyneet, new Henkilo.Vertailija(hk));  
        return loytyneet; 
    }
    
    
    /** 
     * Etsii henkilön id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return henkilö jolla etsittävä id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Henkilot henkilot = new Henkilot(); 
     * Henkilo armi1 = new Henkilo(), armi2 = new Henkilo(), armi3 = new Henkilo(); 
     * armi1.rekisteroi(); armi2.rekisteroi(); armi3.rekisteroi(); 
     * int id1 = armi1.getTunnusNro(); 
     * henkilot.lisaa(armi1); henkilot.lisaa(armi2); henkilot.lisaa(armi3); 
     * henkilot.annaId(id1  ) == armi1 === true; 
     * henkilot.annaId(id1+1) == armi2 === true; 
     * henkilot.annaId(id1+2) == armi3 === true; 
     * </pre> 
     */ 
    public Henkilo annaId(int id) { 
        for (Henkilo henkilo : this) { 
            if (id == henkilo.getTunnusNro()) return henkilo; 
        } 
        return null; 
    } 
    
    
    /** 
     * Etsii henkilön id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen henkilön indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Henkilot henkilot = new Henkilot(); 
     * Henkilo armi1 = new Henkilo(), armi2 = new Henkilo(), armi3 = new Henkilo(); 
     * armi1.rekisteroi(); armi2.rekisteroi(); armi3.rekisteroi(); 
     * int id1 = armi1.getTunnusNro(); 
     * henkilot.lisaa(armi1); henkilot.lisaa(armi2); henkilot.lisaa(armi3); 
     * henkilot.etsiId(id1+1) === 1; 
     * henkilot.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    }

    
    /**
     * Testiohjelma henkilöstölle
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Henkilot henkilot = new Henkilot();

        Henkilo armi = new Henkilo(), armi2 = new Henkilo();
        armi.rekisteroi();
        armi.vastaaAnttilaArmi();
        armi2.rekisteroi();
        armi2.vastaaAnttilaArmi();

        try {
            henkilot.lisaa(armi);
            henkilot.lisaa(armi2);

            System.out.println("============= Henkilöt testi =================");

            int i = 0;
            for (Henkilo henkilo: henkilot) { 
                System.out.println("Henkilö nro: " + i++);
                henkilo.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}