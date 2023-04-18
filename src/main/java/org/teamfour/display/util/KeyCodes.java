package org.teamfour.display.util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class KeyCodes {
    public static final String ENTER = "ENTER";
    public static final String DELETE = "DELETE";
    public static final String HEX_ALPHA_NUM = "ABCDEF1234567890";
    public static final
        UnaryOperator<TextFormatter.Change> HEX_FILTER =
            change -> change
                        .getControlNewText()
                        .matches("^[ABCDEF|0-9]*$") ? change : null;
}
