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
package io.kvision.core

import io.kvision.html.TextNode

/**
 * Base interface for all containers.
 */
interface Container : Component {
    /**
     * Adds given component to the current container.
     * @param child child component
     * @return current container
     */
    fun add(child: Component): Container

    /**
     * Adds given component to the current container at the given position.
     * @param position the position to insert child component
     * @param child the child component
     * @return current container
     */
    fun add(position: Int, child: Component): Container

    /**
     * Adds a list of components to the current container.
     * @param children list of child components
     * @return current container
     */
    fun addAll(children: List<Component>): Container

    /**
     * Operator function for adding children in a DSL style.
     * @param children children components
     * @return current container
     */
    operator fun invoke(vararg children: Component): Container {
        return addAll(children.asList())
    }

    /**
     * Removes given component from the current container.
     * @param child child component
     * @return current container
     */
    fun remove(child: Component): Container

    /**
     * Removes child component from the current container at the given position.
     * @param position the position to be removed
     * @return current container
     */
    fun removeAt(position: Int): Container

    /**
     * Removes all children from the current container.
     * @return current container
     */
    fun removeAll(): Container

    /**
     * Removes all children from the current container and disposes them.
     * @return current container
     */
    fun disposeAll(): Container

    /**
     * Returns a list of children of the current container.
     * @return list of children
     */
    fun getChildren(): List<Component>

    /**
     * An operator to add a text node to the container.
     */
    operator fun String.unaryPlus() {
        add(TextNode(this))
    }
}
