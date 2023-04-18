package org.teamfour.display.resolver.data;

import org.teamfour.display.Form;
import org.teamfour.display.enums.ResolutionPolicy;
import org.teamfour.display.enums.TransitionPolicy;

import java.util.concurrent.atomic.AtomicBoolean;

public class Step {
    private final AtomicBoolean invalid;
    private final ResolutionPolicy resolutionPolicy;
    private final TransitionPolicy transitionPolicy;
    private final Form form;

    public Step(Builder builder) {
        this.resolutionPolicy = builder.resolutionPolicy;
        this.transitionPolicy = builder.transitionPolicy;
        this.form = builder.form;
        invalid = new AtomicBoolean(false);
    }

    public ResolutionPolicy getStepPolicy() {
        return resolutionPolicy;
    }

    public TransitionPolicy getTransitionPolicy() {
        return transitionPolicy;
    }

    public Form getForm() {
        return form;
    }

    public void invalidate() {
        invalid.set(true);
    }

    public boolean invalidated() {
        return invalid.get();
    }

    public static class Builder {
    private ResolutionPolicy resolutionPolicy;
    private TransitionPolicy transitionPolicy;
    private Form form;

    public Builder withResolutionPolicy(ResolutionPolicy resolutionPolicy) {
        this.resolutionPolicy = resolutionPolicy;
        return this;
    }

    public Builder withTransition(TransitionPolicy transitionPolicy) {
        this.transitionPolicy = transitionPolicy;
        return this;
    }

    public Builder withForm(Form form) {
        this.form = form;
        return this;
    }

    public Step build() {
        return new Step(this);
    }
    }
}
