package fxMainonta;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import mainonta.Mainonta;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Johanna Virkkunen
 * @version 26.1.2019
 * @version 16.2.2019 vaiheen 3 viimeistelyt tehty 
 * @version 04.03.2019 vaihe 5 aloitettu
 * @version 30.3.2019 vaihe 6 aloitettu
 * @version 4.4.2019 vaihe 6 jatkettu
 *
 *Pääohjelma Mainontaohjelman käynnistämiseksi
 */
public class MainontaMain extends Application {
	@Override
	public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("MainontaGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final MainontaGUIController MainontaCtrl = (MainontaGUIController)ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("mainonta.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Mainontarekisteri");
            
            // Platform.setImplicitExit(false); // tätä ei kai saa laittaa

            primaryStage.setOnCloseRequest((event) -> {
                if ( !MainontaCtrl.voikoSulkea() ) event.consume();
            });
            
            Mainonta mainonta = new Mainonta();  
            MainontaCtrl.setMainonta(mainonta); 
            
            primaryStage.show();
            
            //MainontaCtrl.lueTiedosto("mainoskunkku");

            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
                MainontaCtrl.lueTiedosto(params.getRaw().get(0));  
            else
                if ( !MainontaCtrl.avaa() ) Platform.exit();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
