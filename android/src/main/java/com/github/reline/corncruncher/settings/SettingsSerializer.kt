package com.github.reline.corncruncher.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.reline.Settings
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
	override val defaultValue: Settings = Settings.getDefaultInstance()

	@Suppress("BlockingMethodInNonBlockingContext")
	override suspend fun readFrom(input: InputStream): Settings {
		return try {
			Settings.parseFrom(input)
		} catch (e: InvalidProtocolBufferException) {
			throw CorruptionException("Cannot read proto.", e)
		}
	}

	@Suppress("BlockingMethodInNonBlockingContext")
	override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}
