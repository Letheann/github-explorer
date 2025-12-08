package com.desafio.shared.data.mapper

internal abstract class BaseMapper<T, K> {

    abstract fun transformTo(source: T): K
}