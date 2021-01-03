package fxMainonta;

import static fxMainonta.TietueDialogController.getFieldId;
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;



import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid; 
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView; 
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
//import javafx.scene.text.Font;
import mainonta.Henkilo;
import mainonta.Mainonta;
import mainonta.Mielenkiinto;
import mainonta.SailoException;


/**
 * Luokka mainontarekisterin käyttöliittymän tapahtumien hoitamiseksi.
 * @author Johanna Virkkunen
 * @version 26.1.2019
 * @version 4.3.2019 vaiheen 5 muutoksia
 * @version 5.3.2019 lisätty mielenkiintojen käsittely
 * @version 30.3.2019 vaihe 6 aloitettu
 * @version 4.4.2019 vaihe 6 jatkettu 
 * @version 21.4.2019 vaihe 7 
 *
 */
public class MainontaGUIController implements Initializable {

    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelHenkilo;
    @FXML private GridPane gridHenkilo;
    @FXML private ListChooser<Henkilo> chooserHenkilot;
    @FXML private StringGrid<Mielenkiinto> tableMielenkiinnot;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();       
    }
    
    @FXML private void handleHakuehto() {
        hae(0); 
    }
        
        @FXML private void handleTallenna() {
            tallenna();
        }
        
        
        
        @FXML private void handleAvaa() {
            avaa();
        }
        
        
        @FXML private void handleTulosta() {
            TulostusController tulostusCtrl = TulostusController.tulosta(null); 
            tulostaValitut(tulostusCtrl.getTextArea()); 
        } 

        
        @FXML private void handleLopeta() {
            tallenna();
            if ( !Dialogs.showQuestionDialog("Poistutaanko?",
                    "Haluatko varmasti poistua?", "Kyllä", "Ei"));
            Platform.exit();
        }
            
            @FXML private void handleUusiHenkilo() {
                uusiHenkilo();
            }
            
            
            @FXML private void handleMuokkaaHenkilo() {
                muokkaa(kentta);
            }
            
            
            @FXML private void handlePoistaHenkilo() {
                poistaHenkilo();
            }
            
             
            @FXML private void handleUusiMielenkiinto() {
                uusiMielenkiinto();
            }
            
            @FXML private void handleMuokkaaMielenkiinto() {
                muokkaaMielenkiintoa();
            }
            

            @FXML private void handlePoistaMielenkiinto() {
                poistaMielenkiinto();
            }
            

            @FXML private void handleApua() {
                avustus();
            }
            
          //===========================================================================================    
         // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
            
            private String yrityksennimi = "mainoskunkku";
            private Mainonta mainonta;
            private Henkilo henkiloKohdalla;
            private TextField edits[];
            private int kentta = 0;
            private static Henkilo apuhenkilo = new Henkilo();
            private static Mielenkiinto apumielenkiinto = new Mielenkiinto();
            
            /**
             * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
             * yksi iso tekstikenttä, johon voidaan tulostaa henkilöiden tiedot.
             * Alustetaan myös henkilölistan kuuntelija 
             */
            protected void alusta() {      
                chooserHenkilot.clear();
                chooserHenkilot.addSelectionListener(e -> naytaHenkilo());
                
                cbKentat.clear(); 
                for (int k = apuhenkilo.ekaKentta(); k < apuhenkilo.getKenttia(); k++) 
                    cbKentat.add(apuhenkilo.getKysymys(k), null); 
                cbKentat.getSelectionModel().select(0); 
                
                edits = TietueDialogController.luoKentat(gridHenkilo, apuhenkilo); 
                for (TextField edit: edits)  
                    if ( edit != null ) {  
                        edit.setEditable(false);  
                        edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                        edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
                        edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);}); 
                    } 
                // alustetaan mielenkiintotaulukon otsikot 
                int eka = apumielenkiinto.ekaKentta(); 
                int lkm = apumielenkiinto.getKenttia(); 
                String[] headings = new String[lkm-eka]; 
                for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apumielenkiinto.getKysymys(k); 
                tableMielenkiinnot.initTable(headings); 
                tableMielenkiinnot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
                tableMielenkiinnot.setEditable(false); 
                tableMielenkiinnot.setPlaceholder(new Label("Ei vielä mielenkiinnonkohteita")); 
                  
                tableMielenkiinnot.setColumnSortOrderNumber(1); 
                tableMielenkiinnot.setColumnSortOrderNumber(2); 
                tableMielenkiinnot.setColumnWidth(1, 60);
                tableMielenkiinnot.setColumnWidth(2, 60); 
                
                tableMielenkiinnot.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaMielenkiintoa(); } );
                tableMielenkiinnot.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaMielenkiintoa();}); 
            }
            
            
             private void naytaVirhe(String virhe) {
                 if ( virhe == null || virhe.isEmpty() ) {
                     labelVirhe.setText("");
                     labelVirhe.getStyleClass().removeAll("virhe");
                     return;
                 }
                 labelVirhe.setText(virhe);
                 labelVirhe.getStyleClass().add("virhe");
             }
             
             
             private void setTitle(String title) {
                 ModalController.getStage(hakuehto).setTitle(title);
             }
             
             
             /**
              * Alustaa yrityksen lukemalla sen valitun nimisestä tiedostosta
              * @param nimi tiedosto josta yrityksen tiedot luetaan
              * @return null jos onnistuu, muuten virhe tekstinä
              */
             protected String lueTiedosto(String nimi) {
                 yrityksennimi = nimi;
                 setTitle("Mainontarekisteri - " + yrityksennimi);
                 try {
                     mainonta.lueTiedostosta(nimi);
                     hae(0);
                     return null;
                 } catch (SailoException e) {
                     hae(0);
                     String virhe = e.getMessage(); 
                     if ( virhe != null ) Dialogs.showMessageDialog(virhe);
                     return virhe;
                 }
              }
             
             
             /**
              * Kysytään tiedoston nimi ja luetaan se
              * @return true jos onnistui, false jos ei
              */
             public boolean avaa() {
                 String uusinimi = YrityksenNimiController.kysyNimi(null, yrityksennimi);
                 if (uusinimi == null) return false;
                 lueTiedosto(uusinimi);
                 return true;
             }
             

             /**
              * Tietojen tallennus
              * @return null jos onnistuu, muuten virhe tekstinä
              */
             private String tallenna() {
                 try {
                     mainonta.tallenna();
                     return null;
                 } catch (SailoException ex) {
                     Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
                     return ex.getMessage();
                 }
             }
             
             /**
              * Tarkistetaan onko tallennus tehty
              * @return true jos saa sulkea sovelluksen, false jos ei
              */
             public boolean voikoSulkea() {
                 tallenna();
                 return true;
             }
             
             /**
              * Näyttää listasta valitun henkilön tiedot tekstikenttiin 
              */
             protected void naytaHenkilo() {
                 henkiloKohdalla = chooserHenkilot.getSelectedObject();
                 if (henkiloKohdalla == null) return;
                 
                 TietueDialogController.naytaTietue(edits, henkiloKohdalla);
                 naytaMielenkiinnot(henkiloKohdalla);
             }
             

             /**
              * Hakee henkilöiden tiedot listaan
              * @param hnr henkilön numero, joka aktivoidaan haun jälkeen
              */
             protected void hae(int hnr) {
                 int hnro = hnr; // hnro henkilön numero, joka aktivoidaan haun jälkeen 
                 if ( hnro <= 0 ) { 
                     Henkilo kohdalla = henkiloKohdalla; 
                     if ( kohdalla != null ) hnro = kohdalla.getTunnusNro(); 
                 }
                 
                 int k = cbKentat.getSelectionModel().getSelectedIndex() + apuhenkilo.ekaKentta(); 
                 String ehto = hakuehto.getText(); 
                 if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
                 
                 chooserHenkilot.clear();

                 int index = 0;
                 Collection<Henkilo> henkilot;
                 try {
                     henkilot = mainonta.etsi(ehto, k);
                     int i = 0;
                     for (Henkilo henkilo:henkilot) {
                         if (henkilo.getTunnusNro() == hnro) index = i;
                         chooserHenkilot.add(henkilo.getNimi(), henkilo);
                         i++; 
                     }
                 } catch (SailoException ex) {
                     Dialogs.showMessageDialog("Henkilön hakemisessa ongelmia! " + ex.getMessage());
                 }
                 chooserHenkilot.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää henkilön
             }
             
             
             /**
              * Luo uuden henkilön jota aletaan editoimaan 
              */
             protected void uusiHenkilo() {
                 try {
                     Henkilo uusi = new Henkilo();
                     uusi = TietueDialogController.kysyTietue(null, uusi, 1);  
                     if ( uusi == null ) return;
                     uusi.rekisteroi();
                     mainonta.lisaa(uusi);
                     hae(uusi.getTunnusNro());
                 } catch (SailoException e) {
                     Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
                     return;
                 }
             }
             
             
             /**
              * kaikkien mielenkiintojen näyttäminen
              * @param henkilo henkilö, jonka mielenkiinnot näytetään
              */
             private void naytaMielenkiinnot(Henkilo henkilo) {
                 tableMielenkiinnot.clear();
                 if ( henkilo == null ) return;
                 
                 try {
                     List<Mielenkiinto> mielenkiinnot = mainonta.annaMielenkiinnot(henkilo);
                     if ( mielenkiinnot.size() == 0 ) return;
                     for (Mielenkiinto miel: mielenkiinnot)
                         naytaMielenkiinto(miel);
                 } catch (SailoException e) {
                     naytaVirhe(e.getMessage());
                 } 
             }

             /**
              * mielenkiinnon näyttäminen
              * @param miel näytettävä mielenkiinto
              */
             private void naytaMielenkiinto(Mielenkiinto miel) {
                 int kenttia = miel.getKenttia(); 
                 String[] rivi = new String[kenttia-miel.ekaKentta()]; 
                 for (int i=0, k=miel.ekaKentta(); k < kenttia; i++, k++) 
                     rivi[i] = miel.anna(k); 
                 tableMielenkiinnot.add(miel,rivi);
             }
             
             
             /**
              * Tekee uuden tyhjän mielenkiinnon editointia varten 
              */
             public void uusiMielenkiinto() {
                 if ( henkiloKohdalla == null ) return;
                 try {
                     Mielenkiinto uusi = new Mielenkiinto(henkiloKohdalla.getTunnusNro());
                     uusi = TietueDialogController.kysyTietue(null, uusi, 0);
                     if ( uusi == null ) return;
                     uusi.rekisteroi();
                     mainonta.lisaa(uusi);
                     naytaMielenkiinnot(henkiloKohdalla); 
                     tableMielenkiinnot.selectRow(1000);  // järjestetään viimeinen rivi valituksi
                 } catch (SailoException e) {
                     Dialogs.showMessageDialog("Lisääminen epäonnistui: " + e.getMessage());
                 }
             }
             
             
             /**
              * Mielenkiinnon muokkaus, jos samalla tunnusnumerolla olevaa ei löydy, lisätään uutena
              */
             private void muokkaaMielenkiintoa() {
                 int r = tableMielenkiinnot.getRowNr();
                 if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
                 Mielenkiinto miel = tableMielenkiinnot.getObject();
                 if ( miel == null ) return;
                 int k = tableMielenkiinnot.getColumnNr()+miel.ekaKentta();
                 try {
                     miel = TietueDialogController.kysyTietue(null, miel.clone(), k);
                     if ( miel == null ) return;
                     mainonta.korvaaTaiLisaa(miel); 
                     naytaMielenkiinnot(henkiloKohdalla); 
                     tableMielenkiinnot.selectRow(r);  // järjestetään sama rivi takaisin valituksi
                 } catch (CloneNotSupportedException  e) { /* clone on tehty */  
                 } catch (SailoException e) {
                     Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
                 }
             }
             
             
             /**
              * Muokataan henkilöä
              */
             private void muokkaa(int k) { 
                 if ( henkiloKohdalla == null ) return; 
                 try { 
                     Henkilo henkilo; 
                     henkilo = TietueDialogController.kysyTietue(null, henkiloKohdalla.clone(), k);
                     if ( henkilo == null ) return; 
                     mainonta.korvaaTaiLisaa(henkilo); 
                     hae(henkilo.getTunnusNro()); 
                 } catch (CloneNotSupportedException e) { 
                     // 
                 } catch (SailoException e) { 
                     Dialogs.showMessageDialog(e.getMessage()); 
                 } 
             }
             
             
             /**
              * @param mainonta Mainontayritys jota käytetään tässä käyttöliittymässä
              */
             public void setMainonta(Mainonta mainonta) {
                 this.mainonta = mainonta;
                 naytaHenkilo();
             }
             
             
             /**
              * Poistetaan mielenkiintotaulukosta valitulla kohdalla oleva mielenkiinto. 
              */
             private void poistaMielenkiinto() {
                 int rivi = tableMielenkiinnot.getRowNr();
                 if ( rivi < 0 ) return;
                 Mielenkiinto mielenkiinto = tableMielenkiinnot.getObject();
                 if ( mielenkiinto == null ) return;
                 mainonta.poistaMielenkiinto(mielenkiinto);
                 naytaMielenkiinnot(henkiloKohdalla);
                 int mielenkiintoja = tableMielenkiinnot.getItems().size(); 
                 if ( rivi >= mielenkiintoja ) rivi = mielenkiintoja -1;
                 tableMielenkiinnot.getFocusModel().focus(rivi);
                 tableMielenkiinnot.getSelectionModel().select(rivi);
             }
             
             
             /**
              * Poistetaan listalta valittu henkilö
              */
             private void poistaHenkilo() {
                 Henkilo henkilo = henkiloKohdalla;
                 if ( henkilo == null ) return;
                 if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko henkilö: " + henkilo.getNimi(), "Kyllä", "Ei") )
                     return;
                 mainonta.poista(henkilo);
                 int index = chooserHenkilot.getSelectedIndex();
                 hae(0);
                 chooserHenkilot.setSelectedIndex(index);
             } 
             
             
             /**
              * Näytetään ohjelman suunnitelma erillisessä selaimessa.
              */
             private void avustus() {
                 Desktop desktop = Desktop.getDesktop();
                 try {
                     URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/virkkmjv");
                     desktop.browse(uri);
                 } catch (URISyntaxException e) {
                     return;
                 } catch (IOException e) {
                     return;
                 }
             }
                 
                 /**
                  * Tulostaa henkilön tiedot
                  * @param os tietovirta johon tulostetaan
                  * @param henkilo tulostettava henkilo
                  */
                 public void tulosta(PrintStream os, final Henkilo henkilo) {
                     os.println("----------------------------------------------");
                     henkilo.tulosta(os);
                     os.println("----------------------------------------------");
                     try {
                         List<Mielenkiinto> mielenkiinnot = mainonta.annaMielenkiinnot(henkilo);
                         for (Mielenkiinto miel:mielenkiinnot) 
                             miel.tulosta(os);     
                     } catch (SailoException ex) {
                         Dialogs.showMessageDialog("Mielenkiintojen hakemisessa ongelmia! " + ex.getMessage());
                     }    
                 }
                 
                 /**
                  * Tulostaa listassa olevat henkilöt tekstialueeseen
                  * @param text alue johon tulostetaan
                  */
                 public void tulostaValitut(TextArea text) {
                     try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
                         os.println("Tulostetaan kaikki henkilöt");
                         for (Henkilo henkilo: chooserHenkilot.getObjects()) {
                             tulosta(os, henkilo);
                             os.println("\n\n");
                         } 
                     }
                 }
    }

