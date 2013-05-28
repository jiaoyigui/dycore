/**
 * Copyright (C) 2006-2009 Dustin Sallings
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */

package com.dooioo.commons.memcache.spy.compat.log;

/**
 * Logging implementation using the sun logger.
 */
public class SunLogger extends AbstractLogger {

  // Can't really import this without confusion as there's another thing
  // by this name in here.
  private final java.util.logging.Logger sunLogger;

  /**
   * Get an instance of SunLogger.
   */
  public SunLogger(String name) {
    super(name);

    // Get the sun logger instance.
    sunLogger = java.util.logging.Logger.getLogger(name);
  }

  /**
   * True if the underlying logger would allow Level.FINE through.
   */
  @Override
  public boolean isDebugEnabled() {
    return (sunLogger.isLoggable(java.util.logging.Level.FINE));
  }

  /**
   * True if the underlying logger would allow Level.INFO through.
   */
  @Override
  public boolean isInfoEnabled() {
    return (sunLogger.isLoggable(java.util.logging.Level.INFO));
  }

  /**
   * Wrapper around sun logger.
   *
   * @param level net.spy.compat.log.AbstractLogger level.
   * @param message object message
   * @param e optional throwable
   */
  @Override
  public void log(Level level, Object message, Throwable e) {
    java.util.logging.Level sLevel = java.util.logging.Level.SEVERE;

    switch (level == null ? Level.FATAL : level) {
    case DEBUG:
      sLevel = java.util.logging.Level.FINE;
      break;
    case INFO:
      sLevel = java.util.logging.Level.INFO;
      break;
    case WARN:
      sLevel = java.util.logging.Level.WARNING;
      break;
    case ERROR:
      sLevel = java.util.logging.Level.SEVERE;
      break;
    case FATAL:
      sLevel = java.util.logging.Level.SEVERE;
      break;
    default:
      // I don't know what this is, so consider it fatal
      sLevel = java.util.logging.Level.SEVERE;
      sunLogger.log(sLevel, "Unhandled log level:  " + level
          + " for the following message");
    }

    // Figure out who was logging.
    Throwable t = new Throwable();
    StackTraceElement[] ste = t.getStackTrace();
    StackTraceElement logRequestor = null;
    String alclass = AbstractLogger.class.getName();
    for (int i = 0; i < ste.length && logRequestor == null; i++) {
      if (ste[i].getClassName().equals(alclass)) {
        // Make sure there's another stack frame.
        if (i + 1 < ste.length) {
          logRequestor = ste[i + 1];
          if (logRequestor.getClassName().equals(alclass)) {
            logRequestor = null;
          } // Also AbstractLogger
        } // Found something that wasn't abstract logger
      } // check for abstract logger
    }

    // See if we could figure out who was doing the original logging,
    // if we could, we want to include a useful class and method name
    if (logRequestor != null) {
      if (e != null) {
        sunLogger.logp(sLevel, logRequestor.getClassName(),
            logRequestor.getMethodName(), message.toString(), e);
      } else {
        sunLogger.logp(sLevel, logRequestor.getClassName(),
            logRequestor.getMethodName(), message.toString());
      }
    } else {
      if (e != null) {
        sunLogger.log(sLevel, message.toString(), e);
      } else {
        sunLogger.log(sLevel, message.toString());
      }
    }
  }
}
