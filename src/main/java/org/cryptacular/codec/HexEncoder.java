/* See LICENSE for licensing and NOTICE for copyright. */
package org.cryptacular.codec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.cryptacular.EncodingException;

/**
 * Stateless hexadecimal byte-to-character encoder.
 *
 * @author  Middleware Services
 */
public class HexEncoder implements Encoder
{

  /** Hex character encoding table. */
  private static final char[] ENCODING_TABLE = new char[16];


  /* Initializes the encoding character table. */
  static {
    final String charset = "0123456789abcdef";
    for (int i = 0; i < charset.length(); i++) {
      ENCODING_TABLE[i] = charset.charAt(i);
    }
  }

  /** Flag indicating whether to delimit every two characters with ':' as in key fingerprints, etc. */
  private final boolean delimit;


  /** Creates a new instance that does not delimit bytes in the output hex string. */
  public HexEncoder()
  {
    this(false);
  }

  /**
   * Creates a new instance with optional delimiting of bytes in the output hex string.
   *
   * @param  delimitBytes  True to delimit every two characters (i.e. every byte) with ':' character.
   */
  public HexEncoder(final boolean delimitBytes)
  {
    delimit = delimitBytes;
  }


  @Override
  public void encode(final ByteBuffer input, final CharBuffer output) throws EncodingException
  {
    byte current;
    while (input.hasRemaining()) {
      current = input.get();
      output.put(ENCODING_TABLE[(current & 0xf0) >> 4]);
      output.put(ENCODING_TABLE[current & 0x0f]);
      if (delimit && input.hasRemaining()) {
        output.put(':');
      }
    }
  }


  @Override
  public void finalize(final CharBuffer output) throws EncodingException {}


  @Override
  public int outputSize(final int inputSize)
  {
    int size = inputSize * 2;
    if (delimit) {
      size += inputSize - 1;
    }
    return size;
  }
}
