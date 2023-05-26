// Copyright (C) 1998-2022 by Jason Hunter <jhunter_AT_servlets_DOT_com>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.oreilly.servlet;

/** 
 * Thrown to indicate a parameter does not exist.
 *
 * @see com.oreilly.servlet.ParameterParser
 */
public class ParameterNotFoundException extends Exception {

  /**
   * Constructs a new ParameterNotFoundException with no detail message.
   */
  public ParameterNotFoundException() {
    super();
  }

  /**
   * Constructs a new ParameterNotFoundException with the specified
   * detail message.
   *
   * @param s the detail message
   */
  public ParameterNotFoundException(String s) {
    super(s);
  }
}
