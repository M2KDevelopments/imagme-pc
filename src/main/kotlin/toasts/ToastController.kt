package storylyn.toasts

import javafx.animation.FadeTransition
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.util.Duration

/**
 * FXML Controller class
 *
 * @author Martin Kululanga
 */
class ToastController {

    @FXML
    private lateinit var lblMessage: Label

    @FXML
    private lateinit var toastContainer: VBox

    fun setup(mainView: AnchorPane, message: String?) {
        AnchorPane.setBottomAnchor(toastContainer, 20.0)
        if (mainView.width <= toastContainer.prefWidth) {
            AnchorPane.setLeftAnchor(toastContainer, 0.0)
            AnchorPane.setRightAnchor(toastContainer, 0.0)
        } else {
            AnchorPane.setLeftAnchor(toastContainer, mainView.width / 2.0 - toastContainer.prefWidth / 2.0)
        }

        mainView.children.add(toastContainer)
        lblMessage.text = message

        //duration settings
        val DURATION_SECONDS = 1
        val DELAY_DURATION_SECONDS = 2.0

        //fade in transition
        var fadeTransition = FadeTransition(Duration.seconds(DURATION_SECONDS.toDouble()), toastContainer)
        fadeTransition.fromValue = 0.0
        fadeTransition.toValue = 1.0
        fadeTransition.play()

        //fade out transition
        fadeTransition = FadeTransition(Duration.seconds(DURATION_SECONDS.toDouble()), toastContainer)
        fadeTransition.delay = Duration.seconds(DELAY_DURATION_SECONDS)
        fadeTransition.fromValue = 1.0
        fadeTransition.toValue = 0.0
        fadeTransition.play()
        fadeTransition.onFinished = EventHandler {
            //close the toast
            mainView.children.remove(toastContainer)
        }
    }
}