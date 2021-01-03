package kanta;

/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa 
 * "attribuutit".
 * @author vesal
 * @autho Johanna Virkkunen
 * @version Mar 23, 2012
 * @version 23.04.2019 Vaihe 7.4
 * @example
 */
public interface Tietue {

    
    /**
     * @return tietueen kenttien lukumäärä
     * @example
     * <pre name="test">
     *   #import mainonta.Mielenkiinto;
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.getKenttia() === 4;
     * </pre>
     */
    public abstract int getKenttia();


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     * @example
     * <pre name="test">
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.ekaKentta() === 2;
     * </pre>
     */
    public abstract int ekaKentta();


    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     * @example
     * <pre name="test">
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.getKysymys(2) === "asia";
     * </pre>
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.parse("   2   |  2  |   Tekniikka  | 3 ");
     *   miel.anna(0) === "2";   
     *   miel.anna(1) === "2";   
     *   miel.anna(2) === "Tekniikka";   
     *   miel.anna(3) === "3";     
     * </pre>
     */
    public abstract String anna(int k);

    
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
     * </pre>
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Mielenkiinto miel = new Mielenkiinto();
     *   miel.parse("   2  |  2  |   Tekniikka  | 5 ");
     *   Object kopio = miel.clone();
     *   kopio.toString() === miel.toString();
     *   miel.parse("   1   |  1  |   Perhe  | 3");
     *   kopio.toString().equals(miel.toString()) === false;
     *   kopio instanceof Mielenkiinto === true;
     * </pre>
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Mielenkiinto mielenkiinto = new Mielenkiinto();
     *   mielenkiinto.parse("   2   |  2  |   Tekniikka  | 5 ");
     *   mielenkiinto.toString()    =R= "2\\|2\\|Tekniikka\\|5.*";
     * </pre>
     */
    @Override
    public abstract String toString();

}
