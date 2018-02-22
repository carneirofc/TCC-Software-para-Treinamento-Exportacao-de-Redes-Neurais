package main.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class MaterialDialog extends Dialog<ButtonType> {

    public static enum AlertType {
        /**
         * The NONE alert type has the effect of not setting any default properties
         * in the Alert.
         */
        NONE,

        /**
         * The INFORMATION alert type configures the Alert dialog to appear in a
         * way that suggests the content of the dialog is informing the user of
         * a piece of information. This includes an 'information' image, an
         * appropriate title and header, and just an OK button for the user to
         * click on to dismiss the dialog.
         */
        INFORMATION,

        /**
         * The WARNING alert type configures the Alert dialog to appear in a
         * way that suggests the content of the dialog is warning the user about
         * some fact or action. This includes a 'warning' image, an
         * appropriate title and header, and just an OK button for the user to
         * click on to dismiss the dialog.
         */
        WARNING,

        /**
         * The CONFIRMATION alert type configures the Alert dialog to appear in a
         * way that suggests the content of the dialog is seeking confirmation from
         * the user. This includes a 'confirmation' image, an
         * appropriate title and header, and both OK and Cancel buttons for the
         * user to click on to dismiss the dialog.
         */
        CONFIRMATION,

        /**
         * The ERROR alert type configures the Alert dialog to appear in a
         * way that suggests that something has gone wrong. This includes an
         * 'error' image, an appropriate title and header, and just an OK button
         * for the user to click on to dismiss the dialog.
         */
        ERROR
    }


}
