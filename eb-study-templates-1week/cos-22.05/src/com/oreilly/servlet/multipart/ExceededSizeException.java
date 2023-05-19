// Copyright (C) 2007-2022 by Jason Hunter <jhunter_AT_servlets_DOT_com>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.oreilly.servlet.multipart;

import java.io.IOException;

/** 
 * Thrown to indicate an upload exceeded the maximum size.
 *
 * @see com.oreilly.servlet.multipart.MultipartParser
 */
public class ExceededSizeException extends IOException {

  /**
   * Constructs a new ExceededSizeException with no detail message.
   */
  public ExceededSizeException() {
    super();
  }

  /**
   * Constructs a new ExceededSizeException with the specified
   * detail message.
   *
   * @param s the detail message
   */
  public ExceededSizeException(String s) {
    super(s);
  }
}
