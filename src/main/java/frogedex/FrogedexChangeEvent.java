package main.java.frogedex;

import main.java.frog.Frog;

import java.util.EventObject;

public class FrogedexChangeEvent extends EventObject {
    private final FrogedexModel.ChangeType changeType;
    private final Frog frog;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public FrogedexChangeEvent(Object source,FrogedexModel.ChangeType changeType, Frog frog) {
        super(source);
        this.changeType = changeType;
        this.frog = frog;
    }

    public FrogedexModel.ChangeType getChangeType() {
        return changeType;
    }

    public Frog getFrog() {
        return frog;
    }
}
