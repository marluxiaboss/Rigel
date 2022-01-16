package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe représentant un animateur de temps.
 * <p>
 * Publique, finale.
 * 
 * @author Orfeas Liossatos (310738)
 * @author Henrique Da Silva Gameiro (315689)
 */
public final class TimeAnimator extends AnimationTimer {

    /// L'instant d'observation
    private DateTimeBean viewingDateTime = new DateTimeBean();
    
    /// Propriétés
    private ObjectProperty<TimeAccelerator> acceleratorProperty = new SimpleObjectProperty<>(null);
    private SimpleBooleanProperty runningProperty = new SimpleBooleanProperty(false);
    
    /// Attributs auxiliares
    private long prevNanos = 0;
    private long deltaNanos = 0;
    private boolean handleHasBeenCalled = false;
    
    /**
     * Constructeur de TimeAnimator. Construit un animateur du temps à partir de 
     * l'instant d'observation {@code viewingDateTime} donné.
     * @param viewingDateTime
     *          l'instant d'observation.
     */
    public TimeAnimator(DateTimeBean viewingDateTime) {
        this.viewingDateTime = viewingDateTime;
    }
    
    /**
     * Méthode appelée une fois par image. Gère l'animation du temps étant donné 
     * l'horodatage {@code timestampNanos} en nanosecondes donné.
     * @param timestampNanos
     *          l'horodatage de l'instant d'observation pour l'image courante en nanosecondes
     */
    @Override
    public void handle(long timestampNanos) {
        if (!handleHasBeenCalled) {
            prevNanos = timestampNanos;
            handleHasBeenCalled = true;
        } 
        
        deltaNanos = (timestampNanos - prevNanos);
        prevNanos = timestampNanos;
        
        viewingDateTime.setZonedDateTime(
                acceleratorProperty.getValue().adjust(
                        viewingDateTime.getZonedDateTime(), deltaNanos));
    }
    
    /**
     * Démarre le minuteur et l'animateur du temps. 
     * On peut les arrêter avec la méthode {@link #stop()}. La méthode {@link #handle(long)} 
     * de cet animateur du temps sera appelée une fois par image.
     */
    @Override
    public void start() {
        handleHasBeenCalled = false;
        
        runningProperty.setValue(true);
        
        super.start();
    }
    
    /**
     * Arrête le minuteur et l'animateur du temps. 
     * On peut les redémarrer avec la méthode {@link #start()}
     */
    @Override 
    public void stop() {
        runningProperty.setValue(false);
        
        super.stop();
    }
    
    /**
     * Méthode retournant la propriété de l'état de l'animateur {@code running} en lecture seulement
     * @return
     *          la propriété de l'état de l'animateur {@code running} en lecture seulement.
     */
    public ReadOnlyBooleanProperty getRunning() {
        return ReadOnlyBooleanProperty.readOnlyBooleanProperty(runningProperty);
    }
    
    /**
     * Retourne la propriété de l'accélérateur du temps
     * @return
     *          la propriété de l'accélérateur du temps
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return acceleratorProperty;
    }

    /**
     * Modifie l'accélérateur de temps pour qu'il soit égal à {@code accelerator}
     * @param accelerator
     *          le nouveau accélérateur de temps
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.acceleratorProperty.setValue(accelerator);
    }
    
}


