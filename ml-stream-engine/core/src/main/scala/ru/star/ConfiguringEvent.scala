package ru.star

final case class ConfiguringEvent(modelId: String,
                                  internalEvent: InternalEvent)
