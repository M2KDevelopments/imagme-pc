package mdu.toasts

import javafx.fxml.FXMLLoader
import javafx.scene.layout.AnchorPane
import storylyn.toasts.ToastController
import java.io.IOException

/**
 *
 * @author Martin Kululanga
 */
object Toast {
    @JvmStatic
    fun makeToast(msg: String?, mainView: AnchorPane) {
        try {
            val loader = FXMLLoader()
            loader.location = Toast::class.java.classLoader.getResource("fxmls/toast.fxml")
            loader.load<Any>()
            val ctrl: ToastController = loader.getController()
            ctrl.setup(mainView, msg)
        } catch (ex: IOException) {
            println("Error loading toast.fxml" + ex.message)
        }
    }
}