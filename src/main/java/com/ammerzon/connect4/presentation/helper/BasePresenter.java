package com.ammerzon.connect4.presentation.helper;

import com.airhacks.afterburner.views.FXMLView;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public abstract class BasePresenter {
    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void show(Class<?> clazz, String title) {
        open(clazz, title, stage);
    }

    protected void presentModally(Class<?> clazz, String title) {
        open(clazz, title, new Stage());
    }

    private void open(Class<?> clazz, String title, Stage newStage) {
        FXMLView view = null;

        try {
            view = (FXMLView) clazz.newInstance();
            Scene scene = new Scene(view.getView());

            BasePresenter presenter = (BasePresenter) view.getPresenter();
            presenter.setStage(newStage);
            newStage.setTitle(title);
            newStage.setScene(scene);
            presenter.maximize();
            newStage.show();
        } catch (IllegalAccessException | InstantiationException e) {
            System.err.println("Couldn't open the presenter!");
            e.printStackTrace();
        }
    }

    protected void showDialog(String heading, String body) {
        JFXDialogLayout layout = new JFXDialogLayout();
        String message;

        layout.setHeading(new Label(heading));
        layout.setBody(new Label(body));
        JFXAlert<Void> alert = new JFXAlert<>(stage);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.setContent(layout);
        alert.initModality(Modality.NONE);
        alert.show();
    }

    /**
     * Maximizes the current stage.
     */
    protected void maximize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }
}
