package ru.star

/**
  * Inner representation of Event.
  * All messages transform into this representation in input-adapter.
  * And core of the system works with this events only.
  *
  * @param id - unique message identifier
  * @param message - message context
  */
// todo: replace message with event representation
final case class InternalEvent(id: String, message: String)
