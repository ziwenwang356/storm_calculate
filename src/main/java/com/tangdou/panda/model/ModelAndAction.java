package com.tangdou.panda.model;

import java.util.Comparator;

public class ModelAndAction implements Comparable<ModelAndAction> {
    private static Comparator<String> nullSafeStringComparator = Comparator.nullsFirst(String::compareTo);
    private static Comparator<ModelAndAction> modelAndActionComparator = Comparator
            .comparing(ModelAndAction::getModel, nullSafeStringComparator)
            .thenComparing(ModelAndAction::getAction, nullSafeStringComparator);

    public static ModelAndAction of(String model, String action) {
        return new ModelAndAction(model, action);
    }

    public static ModelAndAction of(AdClickAndDisplayLog message) {
        return new ModelAndAction(message.u_mod, message.u_ac);
    }

    private String model;
    private String action;

    private ModelAndAction(String model, String action) {
        this.model = model;
        this.action = action;
    }

    private String getModel() {
        return model;
    }

    private String getAction() {
        return action;
    }

    @Override
    public int compareTo(ModelAndAction that) {
        return modelAndActionComparator.compare(this, that);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ModelAndAction && this.compareTo((ModelAndAction) o) == 0;
    }
}
