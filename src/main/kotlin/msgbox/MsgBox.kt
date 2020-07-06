/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdu.msgbox

import Main
import com.jfoenix.controls.JFXButton
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.IOException
import java.util.*

/**
 *
 * @author Martin Kululanga
 */
object MsgBox {
    private var response = false
    private var primaryStage: Stage? = null
    private var darkenAnchorPanes: Array<out AnchorPane>? = null

    private var msgBoxResponse = -1
    const val RESPONSE_YES = 1
    const val RESPONSE_NO = 0
    const val RESPONSE_CANCEL = -1

    //setup layer for darken screen for message box dialogues
    private var paneDarkLayer: Pane? = null
    private var extraPaneDarkLayer: Pane? = null

    @JvmStatic
    fun setup(vararg darkenAnchorPanes: AnchorPane) {
        response = false
        primaryStage = Stage()
        primaryStage!!.icons.add(Image(Objects.requireNonNull(Main::class.java.classLoader.getResourceAsStream("icons/icon_m2kdev.png"))))
        primaryStage!!.initModality(Modality.APPLICATION_MODAL)
        primaryStage!!.title = "M2K Devs"
        primaryStage!!.initStyle(StageStyle.TRANSPARENT)
        primaryStage!!.centerOnScreen()

        MsgBox.darkenAnchorPanes = darkenAnchorPanes
        paneDarkLayer = Pane()


        // set up constrains
        AnchorPane.setTopAnchor(paneDarkLayer, 0.0)
        AnchorPane.setBottomAnchor(paneDarkLayer, 0.0)
        AnchorPane.setLeftAnchor(paneDarkLayer, 0.0)
        AnchorPane.setRightAnchor(paneDarkLayer, 0.0)
        paneDarkLayer!!.background = Background(
            BackgroundFill(
                Color(0.0, 0.0, 0.0, 0.3),
                CornerRadii.EMPTY, Insets.EMPTY
            )
        )
        extraPaneDarkLayer = Pane()
        // set up constrains
        AnchorPane.setTopAnchor(extraPaneDarkLayer, 0.0)
        AnchorPane.setBottomAnchor(extraPaneDarkLayer, 0.0)
        AnchorPane.setLeftAnchor(extraPaneDarkLayer, 0.0)
        AnchorPane.setRightAnchor(extraPaneDarkLayer, 0.0)
        extraPaneDarkLayer!!.background = Background(
            BackgroundFill(
                Color(0.0, 0.0, 0.0, 0.3),
                CornerRadii.EMPTY, Insets.EMPTY
            )
        )
    }

    @JvmStatic
    fun yesAndNo(msg: String, darkenPanes: AnchorPane): Boolean {
        openFXML("yesno", msg, darkenPanes)
        darkenAllScreens(false, darkenPanes)
        return response
    }

    fun yesNoAndCancel(msg: String, darkenPanes: AnchorPane): Int {
        openFXML("yesnocancel", msg, darkenPanes)
        darkenAllScreens(false, darkenPanes)
        return msgBoxResponse
    }

    fun warn(msg: String, darkenPanes: AnchorPane): Boolean {
        openFXML("warn", msg, darkenPanes)
        darkenAllScreens(false, darkenPanes)
        return true
    }

    fun alert(msg: String, darkenPanes: AnchorPane): Boolean {
        openFXML("alert", msg, darkenPanes)
        darkenAllScreens(false, darkenPanes)
        return true
    }

    fun success(msg: String, darkenPanes: AnchorPane): Boolean {
        openFXML("success", msg, darkenPanes)
        darkenAllScreens(false, darkenPanes)
        return true
    }

    fun error(msg: String, darkenPanes: AnchorPane): Boolean {
        openFXML("error", msg, darkenPanes)
        darkenAllScreens(false, darkenPanes)
        return true
    }

    private fun openFXML(msgType: String, msg: String, vararg extraDarkenPanes: AnchorPane) {
        try {
            darkenAllScreens(true, *extraDarkenPanes)
            //root scene settings
            val anchor =
                FXMLLoader.load<AnchorPane>(MsgBox::class.java.classLoader.getResource("fxmls/msgbox_$msgType.fxml"))
            val scene = Scene(anchor)
            scene.fill = Color.TRANSPARENT

            //when the buttons for alert, error, success, warn are clicked
            for (okayNode in anchor.children) {
                if (okayNode is JFXButton) {
                    okayNode.addEventFilter(MouseEvent.MOUSE_CLICKED) { primaryStage!!.close() }
                } else if (okayNode is HBox) {
                    for (yesNoNode in okayNode.children) {
                        if (yesNoNode is JFXButton) {
                            if (yesNoNode.text == "YES") {
                                yesNoNode.addEventHandler(MouseEvent.MOUSE_CLICKED) {
                                    response = true
                                    msgBoxResponse = RESPONSE_YES
                                    primaryStage!!.close()
                                }
                            } else if (yesNoNode.text == "NO") {
                                yesNoNode.addEventHandler(MouseEvent.MOUSE_CLICKED) {
                                    response = false
                                    msgBoxResponse = RESPONSE_NO
                                    primaryStage!!.close()
                                }
                            } else if (yesNoNode.text == "CANCEL") {
                                yesNoNode.addEventHandler(MouseEvent.MOUSE_CLICKED) {
                                    msgBoxResponse = RESPONSE_CANCEL
                                    primaryStage!!.close()
                                }
                            }
                        } else if (yesNoNode is Label) {
                            yesNoNode.text = msg
                        }
                    }
                } else if (okayNode is Label) {
                    okayNode.text = msg
                }
            }
            //stage settings
            primaryStage!!.scene = scene
            primaryStage!!.showAndWait()
        } catch (ex: IOException) {
            println("Failed to loaded $msgType")
        }
    }

    /**
     * This abstract is used to darken the Anchor Panes to emphasizes pop ups.
     *
     * @param mainView the anchor pane on which to darken the screen
     * @param darken toggle to darken the screen or not
     */
    private fun darkenScreen(mainView: AnchorPane, darken: Boolean) {
        if (darken) {
            mainView.children.add(paneDarkLayer)
        } else {
            mainView.children.remove(paneDarkLayer)
        }
    }

    private fun extraDarkenScreen(mainview: AnchorPane, darken: Boolean) {
        if (darken) {
            mainview.children.add(extraPaneDarkLayer)
        } else {
            mainview.children.remove(extraPaneDarkLayer)
        }
    }

    private fun darkenAllScreens(darken: Boolean, vararg extraDarkenPanes: AnchorPane) {
        if (darkenAnchorPanes != null) {
            for (dap in darkenAnchorPanes!!) {
                darkenScreen(dap, darken)
            }
        }
        for (edap in extraDarkenPanes) {
            extraDarkenScreen(edap, darken)
        }
    }
}