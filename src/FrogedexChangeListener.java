import java.util.EventListener;

public interface FrogedexChangeListener extends EventListener {
    void stateChanged(FrogedexChangeEvent changeEvent);
}
