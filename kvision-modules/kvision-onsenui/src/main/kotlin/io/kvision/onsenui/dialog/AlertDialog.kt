/*
 * Copyright (c) 2017-present Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.kvision.onsenui.dialog

import com.github.snabbdom.VNode
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.Component
import io.kvision.core.Container
import io.kvision.html.Div
import io.kvision.panel.SimplePanel
import io.kvision.utils.set

/**
 * An alert dialog component.
 *
 * @constructor Creates an alert dialog component.
 * @param dialogTitle a title of the alert dialog
 * @param cancelable whether the dialog can be canceled
 * @param animation determines if the transitions are animated
 * @param rowfooter horizontally aligns the footer buttons
 * @param classes a set of CSS class names
 * @param init an initializer extension function
 */
@Suppress("LeakingThis")
open class AlertDialog(
    dialogTitle: String? = null,
    cancelable: Boolean? = null,
    animation: Boolean? = null,
    rowfooter: Boolean? = null,
    classes: Set<String> = setOf(),
    init: (AlertDialog.() -> Unit)? = null
) : Dialog(cancelable, animation, classes) {

    /**
     * A title of the alert dialog.
     */
    var dialogTitle: String?
        get() = titlePanel.content
        set(value) {
            titlePanel.content = value
        }

    /**
     * Horizontally aligns the footer buttons.
     */
    var rowfooter: Boolean? by refreshOnUpdate(rowfooter)

    /**
     * The alert dialog title component.
     */
    val titlePanel = Div(dialogTitle, classes = setOf("alert-dialog-title"))

    /**
     * The alert dialog content container.
     */
    val contentPanel = SimplePanel(setOf("alert-dialog-content"))

    /**
     * The alert dialog footer container.
     */
    val footerPanel = SimplePanel(setOf("alert-dialog-footer"))

    init {
        titlePanel.parent = this
        contentPanel.parent = this
        footerPanel.parent = this
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render(
            "ons-alert-dialog",
            arrayOf(titlePanel.renderVNode(), contentPanel.renderVNode(), footerPanel.renderVNode())
        )
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        super.buildAttributeSet(attributeSetBuilder)
        val modRowfooter = if (rowfooter == true) "rowfooter" else null
        val modList = listOfNotNull(modifier, modRowfooter)
        if (modList.isNotEmpty()) {
            attributeSetBuilder.add("modifier", modList.joinToString(" "))
        }
    }

    override fun add(child: Component): AlertDialog {
        contentPanel.add(child)
        return this
    }

    override fun add(position: Int, child: Component): AlertDialog {
        contentPanel.add(position, child)
        return this
    }

    override fun addAll(children: List<Component>): AlertDialog {
        contentPanel.addAll(children)
        return this
    }

    override fun remove(child: Component): AlertDialog {
        contentPanel.remove(child)
        return this
    }

    override fun removeAt(position: Int): AlertDialog {
        contentPanel.removeAt(position)
        return this
    }

    override fun removeAll(): AlertDialog {
        contentPanel.removeAll()
        return this
    }

    override fun disposeAll(): AlertDialog {
        contentPanel.disposeAll()
        return this
    }

    override fun getChildren(): List<Component> {
        return contentPanel.getChildren()
    }

    override fun dispose() {
        super.dispose()
        titlePanel.dispose()
        contentPanel.dispose()
        footerPanel.dispose()
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
@Suppress("unused")
fun Container.alertDialog(
    dialogTitle: String? = null,
    cancelable: Boolean? = null,
    animation: Boolean? = null,
    rowfooter: Boolean? = null,
    classes: Set<String>? = null,
    className: String? = null,
    init: (AlertDialog.() -> Unit)? = null
): AlertDialog {
    return AlertDialog(dialogTitle, cancelable, animation, rowfooter, classes ?: className.set, init)
}
