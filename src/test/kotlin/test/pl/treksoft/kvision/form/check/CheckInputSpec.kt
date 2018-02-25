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
package test.pl.treksoft.kvision.form.check

import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.form.check.CheckInputType
import pl.treksoft.kvision.form.check.CheckInput
import test.pl.treksoft.kvision.DomSpec
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckInputSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test")
            val ci = CheckInput(value = true).apply {
                name = "name"
                id = "idti"
                disabled = true
            }
            root.add(ci)
            val element = document.getElementById("test")
            assertEquals(
                "<input id=\"idti\" type=\"checkbox\" checked=\"checked\" name=\"name\" disabled=\"disabled\">",
                element?.innerHTML,
                "Should render correct checkbox control"
            )
        }
    }

    @Test
    fun renderAsRadio() {
        run {
            val root = Root("test")
            val ci = CheckInput(type = CheckInputType.RADIO, value = true).apply {
                name = "name"
                id = "idti"
                extraValue = "abc"
            }
            root.add(ci)
            val element = document.getElementById("test")
            assertEquals(
                "<input id=\"idti\" type=\"radio\" checked=\"checked\" name=\"name\" value=\"abc\">",
                element?.innerHTML,
                "Should render correct radio button control"
            )
        }
    }

}