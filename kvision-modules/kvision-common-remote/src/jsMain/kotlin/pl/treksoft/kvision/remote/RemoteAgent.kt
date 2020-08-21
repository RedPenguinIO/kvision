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
package pl.treksoft.kvision.remote

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer
import pl.treksoft.kvision.types.JsonDateSerializer
import pl.treksoft.kvision.types.toStringInternal
import kotlin.js.Date
import kotlin.reflect.KClass

class NotStandardTypeException(type: String) : Exception("Not a standard type: $type!")

class NotEnumTypeException : Exception("Not the Enum type!")

/**
 * Interface for client side agent for JSON-RPC remote calls.
 */
interface RemoteAgent {

    /**
     * @suppress
     * Internal function
     */
    @OptIn(InternalSerializationApi::class)
    @Suppress("ComplexMethod", "TooGenericExceptionCaught", "NestedBlockDepth")
    fun trySerialize(kClass: KClass<Any>, value: Any): String {
        return if (value is List<*>) {
            if (value.size > 0) {
                when {
                    value[0] is String ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(String.serializer()) as KSerializer<Any>, value)
                    value[0] is Date ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(JsonDateSerializer) as KSerializer<Any>, value)
                    value[0] is Int ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Int.serializer()) as KSerializer<Any>, value)
                    value[0] is Long ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Long.serializer()) as KSerializer<Any>, value)
                    value[0] is Boolean ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Boolean.serializer()) as KSerializer<Any>, value)
                    value[0] is Float ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Float.serializer()) as KSerializer<Any>, value)
                    value[0] is Double ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Double.serializer()) as KSerializer<Any>, value)
                    value[0] is Char ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Char.serializer()) as KSerializer<Any>, value)
                    value[0] is Short ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Short.serializer()) as KSerializer<Any>, value)
                    value[0] is Byte ->
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(Byte.serializer()) as KSerializer<Any>, value)
                    value[0] is Enum<*> -> "[" + value.joinToString(",") { "\"$it\"" } + "]"
                    else -> try {
                        @Suppress("UNCHECKED_CAST")
                        JSON.plain.encodeToString(ListSerializer(kClass.serializer()) as KSerializer<Any>, value)
                    } catch (e: Throwable) {
                        try {
                            @Suppress("UNCHECKED_CAST")
                            JSON.plain.encodeToString(
                                ListSerializer(value[0]!!::class.serializer()) as KSerializer<Any>,
                                value
                            )
                        } catch (e: Throwable) {
                            try {
                                @Suppress("UNCHECKED_CAST")
                                JSON.plain.encodeToString(
                                    ListSerializer(String.serializer()) as KSerializer<Any>,
                                    value
                                )
                            } catch (e: Throwable) {
                                value.toString()
                            }
                        }
                    }
                }
            } else {
                "[]"
            }
        } else {
            when (value) {
                is Enum<*> -> "\"$value\""
                is String -> value
                is Char -> "\"$value\""
                is Date -> "\"${value.toStringInternal()}\""
                else -> try {
                    @Suppress("UNCHECKED_CAST")
                    JSON.plain.encodeToString(kClass.serializer(), value)
                } catch (e: Throwable) {
                    value.toString()
                }
            }
        }
    }

    /**
     * @suppress
     * Internal function
     */
    @Suppress("UNCHECKED_CAST", "ComplexMethod")
    fun <RET> deserialize(value: String, jsType: String): RET {
        return when (jsType) {
            "String" -> JSON.plain.decodeFromString(String.serializer(), value) as RET
            "Number" -> JSON.plain.decodeFromString(Double.serializer(), value) as RET
            "Long" -> JSON.plain.decodeFromString(Long.serializer(), value) as RET
            "Boolean" -> JSON.plain.decodeFromString(Boolean.serializer(), value) as RET
            "BoxedChar" -> JSON.plain.decodeFromString(Char.serializer(), value) as RET
            "Short" -> JSON.plain.decodeFromString(Short.serializer(), value) as RET
            "Date" -> JSON.plain.decodeFromString(JsonDateSerializer, value) as RET
            "Byte" -> JSON.plain.decodeFromString(Byte.serializer(), value) as RET
            else -> throw NotStandardTypeException(jsType)
        }
    }

    /**
     * @suppress
     * Internal function
     */
    @Suppress("UNCHECKED_CAST", "ComplexMethod")
    fun <RET> deserializeList(value: String, jsType: String): List<RET> {
        return when (jsType) {
            "String" -> JSON.plain.decodeFromString(ListSerializer(String.serializer()), value) as List<RET>
            "Number" -> JSON.plain.decodeFromString(ListSerializer(Double.serializer()), value) as List<RET>
            "Long" -> JSON.plain.decodeFromString(ListSerializer(Long.serializer()), value) as List<RET>
            "Boolean" -> JSON.plain.decodeFromString(ListSerializer(Boolean.serializer()), value) as List<RET>
            "BoxedChar" -> JSON.plain.decodeFromString(ListSerializer(Char.serializer()), value) as List<RET>
            "Short" -> JSON.plain.decodeFromString(ListSerializer(Short.serializer()), value) as List<RET>
            "Date" -> JSON.plain.decodeFromString(ListSerializer(JsonDateSerializer), value) as List<RET>
            "Byte" -> JSON.plain.decodeFromString(ListSerializer(Byte.serializer()), value) as List<RET>
            else -> throw NotStandardTypeException(jsType)
        }
    }

    /**
     * @suppress
     * Internal function
     */
    @Suppress("TooGenericExceptionCaught", "ThrowsCount")
    fun tryDeserializeEnum(kClass: KClass<Any>, value: String): Any {
        return try {
            if (kClass.asDynamic().jClass.`$metadata$`.interfaces[0].name == "Enum") {
                findEnumValue(kClass, JSON.plain.decodeFromString(String.serializer(), value))
                    ?: throw NotEnumTypeException()
            } else {
                throw NotEnumTypeException()
            }
        } catch (e: Throwable) {
            throw NotEnumTypeException()
        }
    }

    /**
     * @suppress
     * Internal function
     */
    @Suppress("TooGenericExceptionCaught", "ThrowsCount")
    fun tryDeserializeEnumList(kClass: KClass<Any>, value: String): List<Any> {
        return try {
            if (kClass.asDynamic().jClass.`$metadata$`.interfaces[0].name == "Enum") {
                JSON.plain.decodeFromString(ListSerializer(String.serializer()), value).map {
                    findEnumValue(kClass, JSON.plain.decodeFromString(String.serializer(), it))
                        ?: throw NotEnumTypeException()
                }
            } else {
                throw NotEnumTypeException()
            }
        } catch (e: Throwable) {
            throw NotEnumTypeException()
        }
    }

    fun findEnumValue(kClass: KClass<Any>, value: String): Any? {
        return (kClass.asDynamic().jClass.values() as Array<Any>).find {
            it.asDynamic().name == value
        }
    }
}
