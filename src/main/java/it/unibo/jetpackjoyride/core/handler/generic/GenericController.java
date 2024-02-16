package it.unibo.jetpackjoyride.core.handler.generic;

import it.unibo.jetpackjoyride.core.entities.entity.api.Entity;
import javafx.scene.image.ImageView;

public class GenericController<T extends Entity,V extends GenericView> {
    private final T model;
    private final V view;

    public GenericController(final T model, final V view) {
        this.model = model;
        this.view = view;
    }

    public void update(final boolean isSpaceBarPressed) {
        this.model.update(isSpaceBarPressed);
        this.view.updateView(model);
    }

    public ImageView getImageView() {
        return this.view.getImageView();
    }

    public T getEntityModel() {
        return this.model;
    }
}
