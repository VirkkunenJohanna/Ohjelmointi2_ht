package fxMainonta;

import java.net.URL;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.Dialogs; 
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane; 
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane; 
import javafx.stage.Stage;
import mainonta.Henkilo;

/**
 * Kysytään henkilön tiedot luomalla sille uusi dialogi
 * 
 * @author virkkmjv
 * @version 21.4.2019 7.vaihe
 *
 */
public class HenkiloDialogController implements ModalControllerInterface<Henkilo>,Initializable  {

    @FXML private ScrollPane panelHenkilo;
    @FXML private GridPane gridHenkilo;
    @FXML private Label labelVirhe;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    @FXML private void handleOK() {
        if ( henkiloKohdalla != null && henkiloKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
        henkiloKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

// ========================================================    
    private Henkilo henkiloKohdalla;
    private static Henkilo apuhenkilo = new Henkilo(); // henkilö jolta voidaan kysellä tietoja.
    private TextField[] edits;
    private int kentta = 0;
    
    
    /**
     * Luodaan GridPaneen henkilön tiedot
     * @param gridHenkilo mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridHenkilo) {
        gridHenkilo.getChildren().clear();
        TextField[] edits = new TextField[apuhenkilo.getKenttia()];
        
        for (int i=0, k = apuhenkilo.ekaKentta(); k < apuhenkilo.getKenttia(); k++, i++) {
            Label label = new Label(apuhenkilo.getKysymys(k));
            gridHenkilo.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridHenkilo.add(edit, 1, i);
        }
        return edits;
    }
    

    /**
     * Tyhjentään tekstikentät 
     * @param edits tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit: edits) 
            if ( edit != null ) edit.setText(""); 
    }
    
    
    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }


    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa henkilöiden tiedot.
     */
    protected void alusta() {
        edits = luoKentat(gridHenkilo);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosHenkiloon((TextField)(e.getSource())));
        panelHenkilo.setFitToHeight(true);
    }
    
    
    @Override
    public void setDefault(Henkilo oletus) {
        henkiloKohdalla = oletus;
        naytaHenkilo(edits, henkiloKohdalla);
    }

    
    @Override
    public Henkilo getResult() {
        return henkiloKohdalla;
    }
    
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
    
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apuhenkilo.ekaKentta(), Math.min(kentta, apuhenkilo.getKenttia()-1));
        edits[kentta].requestFocus();
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

    
    /**
     * Käsitellään henkilöön tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosHenkiloon(TextField edit) {
        if (henkiloKohdalla == null) return;
        int k = getFieldId(edit,apuhenkilo.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = henkiloKohdalla.aseta(k,s); 
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään henkilön tiedot TextField komponentteihin
     * @param edits taulukko TextFieldeistä johon näytetään
     * @param henkilo näytettävä henkilö
     */
    public static void naytaHenkilo(TextField[] edits, Henkilo henkilo) {
        if (henkilo == null) return;
        for (int k = henkilo.ekaKentta(); k < henkilo.getKenttia(); k++) {
            edits[k].setText(henkilo.anna(k));
        }
    }
    
    
    /**
     * Luodaan henkilön kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Henkilo kysyHenkilo(Stage modalityStage, Henkilo oletus, int kentta) {
        return ModalController.<Henkilo, HenkiloDialogController>showModal(
                    HenkiloDialogController.class.getResource("HenkiloDialogView.fxml"),
                    "Mainontarekisteri",
                    modalityStage, oletus,
                    ctrl -> ctrl.setKentta(kentta) 
                );
    }

}