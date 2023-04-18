package org.teamfour.display.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

public class Icons {


    public static final FontIcon ENTER_ICON =
            new Builder(FontAwesome.ARROW_CIRCLE_RIGHT)
                    .withSize(20)
                    .withFill(Color.valueOf("#4f7849"))
                    .build();
    public static final FontIcon DELETE_ICON =
            new Builder(FontAwesome.CHEVRON_CIRCLE_LEFT)
                    .withSize(20)
                    .withFill(Color.valueOf("#98384f"))
                    .build();

    public static final FontIcon EXIT_ICON =
            new Builder(FontAwesome.REMOVE)
                    .withSize(40)
                    .withFill(Color.valueOf("#98384f"))
                    .build();

    public static final FontIcon CONTINUE_ICON =
            new Builder(FontAwesome.CHEVRON_CIRCLE_RIGHT)
                    .withSize(40)
                    .withFill(Color.valueOf("#4f7849"))
                    .build();

    public static class Builder {
        private final FontIcon fontIcon;
        public Builder(FontAwesome fontAwesome) {
            fontIcon = new FontIcon(fontAwesome);
        }

        public Builder withSize(int size) {
            fontIcon.setIconSize(size);
            return this;
        }

        public Builder withFill(Paint value) {
            fontIcon.setFill(value);
            return this;
        }

        public FontIcon build() {
            return this.fontIcon;
        }

    }
}
