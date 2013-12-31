package org.cryptosis.bean.adapter;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;

/**
 * Adapts a {@link AEADBlockCipherAdapter}.
 *
 * @author Marvin S. Addison
 */
public class AEADBlockCipherAdapter implements BlockCipherAdapter
{
  /** All methods delegate to this instance. */
  private final AEADBlockCipher delegate;


  /**
   * Creates a new instance that delegates to the given cipher.
   *
   * @param  delegate  Adapted cipher.
   */
  public AEADBlockCipherAdapter(final AEADBlockCipher delegate)
  {
    this.delegate = delegate;
  }


  /** {@inheritDoc} */
  @Override
  public int getOutputSize(final int len)
  {
    return delegate.getOutputSize(len);
  }


  /** {@inheritDoc} */
  @Override
  public void init(final boolean forEncryption, final CipherParameters params)
  {
    delegate.init(forEncryption, params);
  }


  /** {@inheritDoc} */
  @Override
  public int processBytes(final byte[] in, final int inOff, final int len, final byte[] out, final int outOff)
  {
    return delegate.processBytes(in, inOff, len, out, outOff);
  }


  /** {@inheritDoc} */
  @Override
  public int doFinal(final byte[] out, final int outOff)
  {
    try {
      return delegate.doFinal(out, outOff);
    } catch (InvalidCipherTextException e) {
      throw new RuntimeException("Error finalizing cipher", e);
    }
  }


  /** {@inheritDoc} */
  @Override
  public void reset()
  {
    delegate.reset();
  }
}
