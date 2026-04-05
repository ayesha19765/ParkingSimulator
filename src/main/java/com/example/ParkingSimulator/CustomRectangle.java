package com.example.ParkingSimulator;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomRectangle extends StackPane {
    private Rectangle rectangle;
    private Text label;

    public CustomRectangle() {
        // Création du rectangle avec des dimensions 50x50 et une couleur de fond gris clair
        rectangle = new Rectangle(50, 50, Color.LIGHTGRAY);
        rectangle.setStroke(Color.BLACK); // Définir la couleur de la bordure du rectangle en noir
        label = new Text(); // Création du label (texte)
        label.setFont(new Font("Arial", 12)); // Définir la police et la taille du texte
        this.getChildren().addAll(rectangle, label); // Ajouter le rectangle et le texte à la StackPane
        this.setStyle("-fx-padding: 5;"); //  Un style CSS pour ajouter un padding de 5 pixels
    }

    // Méthode pour définir la couleur du rectangle
    public void setColor(Color color) {
        rectangle.setFill(color);
    }

    // Méthode pour définir le texte du label
    public void setText(String text) {
        label.setText(text);
    }
}
