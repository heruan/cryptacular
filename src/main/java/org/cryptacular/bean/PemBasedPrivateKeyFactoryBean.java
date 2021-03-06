/* See LICENSE for licensing and NOTICE for copyright. */
package org.cryptacular.bean;

import java.security.PrivateKey;

import org.cryptacular.EncodingException;
import org.cryptacular.util.ByteUtil;
import org.cryptacular.util.KeyPairUtil;
import org.cryptacular.util.PemUtil;

/**
 * Factory for creating a public key from a PEM-encoded private key in any format supported by {@link
 * KeyPairUtil#decodePrivateKey(byte[])}. Note that this component does not support encrypted private keys; see {@link
 * ResourceBasedPrivateKeyFactoryBean} for encryption support.
 *
 * @author  Middleware Services
 * @see  org.cryptacular.util.KeyPairUtil#decodePrivateKey(byte[])
 * @see  ResourceBasedPrivateKeyFactoryBean
 */
public class PemBasedPrivateKeyFactoryBean implements FactoryBean<PrivateKey>
{

  /** PEM-encoded public key data. */
  private String encodedKey;


  /** Creates a new instance. */
  public PemBasedPrivateKeyFactoryBean() {}


  /**
   * Creates a new instance by specifying all properties.
   *
   * @param  pemEncodedKey  PEM-encoded private key data.
   */
  public PemBasedPrivateKeyFactoryBean(final String pemEncodedKey)
  {
    setEncodedKey(pemEncodedKey);
  }


  /** @return  PEM-encoded private key data. */
  public String getEncodedKey()
  {
    return encodedKey;
  }


  /**
   * Sets the PEM-encoded private key data.
   *
   * @param  pemEncodedKey  PEM-encoded private key data.
   */
  public void setEncodedKey(final String pemEncodedKey)
  {
    if (!PemUtil.isPem(ByteUtil.toBytes(pemEncodedKey))) {
      throw new IllegalArgumentException("Data is not PEM encoded.");
    }
    this.encodedKey = pemEncodedKey;
  }


  @Override
  public PrivateKey newInstance() throws EncodingException
  {
    return KeyPairUtil.decodePrivateKey(PemUtil.decode(encodedKey));
  }
}
