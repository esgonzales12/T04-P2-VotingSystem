package org.teamfour.display;

import org.teamfour.display.resolver.Resolver;

public interface Form {
    void setResolver(Resolver resolver);
    void invalid();
    void valid();
    void clear();
}
