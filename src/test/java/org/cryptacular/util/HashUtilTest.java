/* See LICENSE for licensing and NOTICE for copyright. */
package org.cryptacular.util;

import java.io.File;
import java.io.InputStream;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.cryptacular.FailListener;
import org.cryptacular.SaltedHash;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link HashUtil}.
 *
 * @author  Middleware Services
 */
@Listeners(FailListener.class)
public class HashUtilTest
{
  private static final byte[] SALT = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};

  @DataProvider(name = "salted-hashes")
  public Object[][] getSaltedHashData()
  {
    return
      new Object[][] {
        {
          new SHA1Digest(),
          new Object[] {ByteUtil.toBytes("deoxyribonucleic acid"), },
          1,
          "0aDM5g/qqfVV/8MIqkTKQaklWSg=",
        },
        {
          new SHA1Digest(),
          new Object[] {
            ByteUtil.toBytes("protoporphyrin-9"),
            SALT,
          },
          1,
          "6SafHIoTusYN6dnK1pxx7udaBLA=",
        },
        {
          new SHA256Digest(),
          new Object[] {
            SALT,
            ByteUtil.toBytes("N-arachidonoylethanolamine"),
          },
          5,
          "RWIg3BIXdqZPI9C7PFvSn62miU3L9ponVZLvKmC9XlQ=",
        },
      };
  }

  @DataProvider(name = "hash-compare")
  public Object[][] getHashCompareData()
  {
    return
      new Object[][] {
        {
          new SHA1Digest(),
          CodecUtil.b64("7fyOZXGp+gKMziV/2Px7RIMkxyI2O1H8"),
          1,
          ByteUtil.toBytes("password"),
        },
        {
          new SHA1Digest(),
          CodecUtil.b64("0aDM5g/qqfVV/8MIqkTKQaklWSg="),
          1,
          ByteUtil.toBytes("deoxyribonucleic acid"),
        },
      };
  }

  @DataProvider(name = "salted-hash-compare")
  public Object[][] getSaltedHashCompareData()
  {
    return
      new Object[][] {
        {
          new SHA1Digest(),
          new SaltedHash(CodecUtil.b64("7fyOZXGp+gKMziV/2Px7RIMkxyI2O1H8"), 20, true),
          1,
          true,
          ByteUtil.toBytes("password"),
        },
      };
  }


  @DataProvider(name = "file-hashes")
  public Object[][] getFileHashes()
  {
    return
      new Object[][] {
        new Object[] {
          "src/test/resources/plaintexts/lorem-1200.txt",
          "f0746e8978b3eccca05284dd12f098fdea32c8bc",
        },
        new Object[] {
          "src/test/resources/plaintexts/lorem-5000.txt",
          "1142d7a2661760624fa41b002be6c66c23b50602",
        },
      };
  }


  @Test(dataProvider = "salted-hashes")
  public void testSaltedHash(final Digest digest, final Object[] data, final int iterations, final String expected)
    throws Exception
  {
    assertEquals(CodecUtil.b64(HashUtil.hash(digest, iterations, data)), expected);
  }


  @Test(dataProvider = "hash-compare")
  public void testCompareHash(final Digest digest, final byte[] hash, final int iterations, final byte[] data)
    throws Exception
  {
    assertTrue(HashUtil.compareHash(digest, hash, iterations, data));
  }


  @Test(dataProvider = "salted-hash-compare")
  public void testCompareSaltedHash(
    final Digest digest,
    final SaltedHash saltedHash,
    final int iterations,
    final boolean saltAfterData,
    final byte[] data)
    throws Exception
  {
    assertTrue(HashUtil.compareHash(digest, saltedHash, iterations, saltAfterData, data));
  }


  @Test(dataProvider = "file-hashes")
  public void testHashStream(final String path, final String expected)
    throws Exception
  {
    try (InputStream in = StreamUtil.makeStream(new File(path))) {
      assertEquals(Hex.toHexString(HashUtil.sha1(in)), expected);
    }
  }
}
