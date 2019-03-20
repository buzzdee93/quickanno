/* 
 * This file is part of the QuickAnno application (https://github.com/buzzdee93/quickanno).
 * Copyright (c) 2019 Sebastian Gombert.
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

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
