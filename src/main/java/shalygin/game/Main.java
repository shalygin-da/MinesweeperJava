package shalygin.game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application {

	static Button step;
	static Stage window = new Stage();
	private CheckBox solve;
	static Pane inGameLayout;
	static Scene scene;

	public static void main(String[] args) { launch(args); }

	public void start(Stage stage) {
		window = stage;
		stage.setTitle("Shalygin.Minesweeper.java");
		Button play = new Button("PLAY");
		Button exit = new Button("EXIT");
		Button toMenu = new Button("To Menu");
		step = new Button("Step");
		solve = new CheckBox("Use solver");

		String button = "-fx-padding: 8 15 15 15;\n" +
				" -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n" +
				" -fx-background-radius 10;\n" + " -fx-background-color: \n" +
				"lightgrey" + " -fx-text-fill #ffffff;" + " -fx-font-size: 4.0em;";

		String label = " -fx-background-radius 10;\n" + " -fx-background-color: \n" +
				"lightgrey" + " -fx-text-fill #ffffff;" + " -fx-font-size: 2.0em;";

		step.setStyle("-fx-background-radius 10;\n" + " -fx-background-color: \n" +
				"lightgrey" + " -fx-text-fill #ffffff;" + " -fx-font-size: 1.0em;");
		toMenu.setStyle("-fx-background-radius: 10;\n" + " -fx-background-color: \n" +
				"lightgrey" + " -fx-text-fill #ffffff;" + " -fx-font-size: 1.0em;");
		play.setStyle(button);
		exit.setStyle(button);
		solve.setStyle(label);


		TextField w = new TextField();
		w.setMaxWidth(125);
		TextField h = new TextField();
		h.setMaxWidth(125);
		TextField mines = new TextField();
		mines.setMaxWidth(125);
		solve.setSelected(true);

		w.setStyle(button);
		h.setStyle(button);
		mines.setStyle(button);

		Label wLabel = new Label("Insert Board Width");
		Label hLabel = new Label("Insert Board Height");
		Label minesLabel = new Label("Mines Amount");

		wLabel.setStyle(label);
		hLabel.setStyle(label);
		minesLabel.setStyle(label);

		stage.setOnCloseRequest(e -> System.exit(0));

		// Window setup

		StackPane.setMargin(play, new Insets(0, 0, 400, 0));
		StackPane.setMargin(exit, new Insets(0, 0, 0, 0));
		StackPane.setMargin(w, new Insets(450, 550, 0, 0));
		StackPane.setMargin(h, new Insets(450, 0, 0, 0));
		StackPane.setMargin(mines, new Insets(450, -550, 0, 0));
		StackPane.setMargin(wLabel, new Insets(300, 550, 0, 0));
		StackPane.setMargin(hLabel, new Insets(300, 0, 0, 0));
		StackPane.setMargin(minesLabel, new Insets(300, -550, 0, 0));
		StackPane.setMargin(solve, new Insets(0, 0, 200, 0));

		StackPane layout = new StackPane(play, exit, w, h, mines, wLabel, hLabel, minesLabel, solve);
		scene = new Scene(layout, 800, 600); // is for in-class screens
		stage.setScene(scene);
		stage.show();

		//Buttons setup

		exit.setOnAction(e -> window.close());

		play.setOnAction(e -> {
			if (w.getText().isEmpty() || h.getText().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Nah mate, it's all wrong!");
				alert.setHeaderText("Input 'Width', 'Height' & 'Mines Amount' before starting!");
				alert.showAndWait();
			}
			int wNum = Integer.parseInt(w.getText());
			int hNum = Integer.parseInt(h.getText());
			int minesNum = Integer.parseInt(mines.getText());
			if (wNum > 63 || hNum > 63 || minesNum > wNum * hNum - 1) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Nah mate, it's all wrong!");
				alert.setHeaderText("Max width & height: 63; Amount of mines should be less than the board's size.");
				alert.showAndWait();
			} else {
				toMenu.setLayoutX(wNum * 30 - 102);
				toMenu.setLayoutY(hNum * 30);
				step.setLayoutX(wNum * 30 / 2 - 20);
				step.setLayoutY(hNum * 30);
				inGameLayout = new Pane(toMenu, step);
				window.setScene(new Scene(inGameLayout, wNum * 30, hNum * 30 + 25));
				Game game = new Game();
				game.solve = solve.isSelected();
				game.start(wNum, hNum, minesNum);
			}
		});
		toMenu.setOnAction(e -> window.setScene(scene));
	}

}
