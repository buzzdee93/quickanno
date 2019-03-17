package de.sebastiangombert;


import de.sebastiangombert.gui.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QuickAnno extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
	@Override
	public void start(Stage primary) throws Exception {
		primary.setTitle("QuickAnno");
		
		FXMLLoader loader = new FXMLLoader(QuickAnno.class.getClassLoader().getResource("gui.fxml"));
		
		loader.setController(new GuiController());
		
		Parent root = (Parent) loader.load();
		
		GuiController ctrl = loader.<GuiController>getController();

		ctrl.init(primary);
		
		Scene scene = new Scene(root);
		
		primary.setScene(scene);
		primary.show();
		
	}

}
