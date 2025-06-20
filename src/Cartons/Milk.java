package Cartons;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Milk extends Application {
    private Stage stage;
    private Label label;
    private Button yesButton;
    private Button noButton;
    private VBox layout;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        label = new Label("Do you have a problem with milk?");
        yesButton = new Button("Yes");
        noButton = new Button("No");

        HBox buttonBox = new HBox(20, yesButton, noButton);
        buttonBox.setStyle("-fx-alignment: center;");

        layout = new VBox(30, label, buttonBox);
        layout.setStyle("-fx-padding: 40; -fx-alignment: center;");

        yesButton.setOnAction(e -> askToWatchVideo());
        noButton.setOnAction(e -> sayGoodbye());

        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Milk Problem Solver");
        stage.setResizable(false);
        stage.show();
    }

    private void askToWatchVideo() {
        label.setText("Do you want to watch a video to solve it?");
        yesButton.setOnAction(e -> playVideo());
        noButton.setOnAction(e -> sayComeBackLater());
    }

    private void playVideo() {
        try {
            // Get directory of the running jar/exe
            String jarDir = new File(Milk.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getParent();

            File videoFile = new File(jarDir, "MilkProblems.mp4");

            // Extract from JAR if not already copied
            if (!videoFile.exists()) {
                try (InputStream in = getClass().getResourceAsStream("/resources/MilkProblems.mp4");
                     OutputStream out = new FileOutputStream(videoFile)) {

                    if (in == null) {
                        throw new FileNotFoundException("Video resource not found inside JAR.");
                    }

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }

            Media media = new Media(videoFile.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            MediaView mediaView = new MediaView(player);

            double windowWidth = 700, windowHeight = 500;
            mediaView.setPreserveRatio(true);
            mediaView.setFitWidth(windowWidth - 40);
            mediaView.setFitHeight(windowHeight - 100);

            VBox videoLayout = new VBox(10, mediaView);
            videoLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
            Scene videoScene = new Scene(videoLayout, windowWidth, windowHeight);

            stage.setScene(videoScene);
            stage.setResizable(false);
            stage.centerOnScreen();

            player.setOnEndOfMedia(() -> {
                Label finalMessage = new Label(
                        "If you're never gonna have it in your glass,\n" +
                                "you're never gonna have a problem with it.");
                finalMessage.setStyle("-fx-font-size: 16px; -fx-padding: 20;");
                VBox finalLayout = new VBox(finalMessage);
                finalLayout.setStyle("-fx-alignment: center; -fx-padding: 30;");
                Scene endScene = new Scene(finalLayout, windowWidth, windowHeight);
                stage.setScene(endScene);
            });

            player.play();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            label.setText("Error loading video.");
        }
    }

    private void sayComeBackLater() {
        label.setText("Come back if you want to watch the video.");
        yesButton.setVisible(false);
        noButton.setVisible(false);
    }

    private void sayGoodbye() {
        label.setText("Goodbye!!");
        yesButton.setVisible(false);
        noButton.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
