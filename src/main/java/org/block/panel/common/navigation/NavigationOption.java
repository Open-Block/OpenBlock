package org.block.panel.common.navigation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NavigationOption extends VBox {

    public NavigationOption(String text){
        this(text, null);
    }

    public NavigationOption(String text, @Nullable Image image){
        this(new Text(text), image == null ? null : new ImageView(image));
    }

    public NavigationOption(Text text, @Nullable ImageView image){
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        if(image != null){
            this.getChildren().add(image);
        }
        this.getChildren().add(text);
    }

    public Text getText(){
        Optional<Text> opText = this.getChildren().parallelStream().filter(n -> n instanceof Text).findFirst().map(n -> (Text)n);
        if(opText.isEmpty()){
            throw new IllegalStateException("Something has removed the Text child.");
        }
        return opText.get();
    }

    public void setOnAction(EventHandler<ActionEvent> setEvent){
        this.getChildren().parallelStream().forEach(n -> {
            n.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ActionEvent actionEvent = new ActionEvent(event, NavigationOption.this);
                setEvent.handle(actionEvent);
                if(actionEvent.isConsumed()){
                    event.consume();
                }
            });
        });
    }
}
