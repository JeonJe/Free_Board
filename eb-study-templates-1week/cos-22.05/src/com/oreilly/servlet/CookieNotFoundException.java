// Copyright (C) 1998-2022 by Jason Hunter <jhunter_AT_servlets_DOT_com>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.oreilly.servlet;

/** 
 * Thrown to indicate a cookie does not exist.
 *
 * @see com.oreilly.servlet.CookieParser
 */
public class CookieNotFoundException extends Exception {

  /**
   * Constructs a new CookieNotFoundException with no detail message.
   */
  public CookieNotFoundException() {
    super();
  }

  /**
   * Constructs a new CookieNotFoundException with the specified
   * detail message.
   *
   * @param s the detail message
   */
  public CookieNotFoundException(String s) {
    super(s);
  }
}
