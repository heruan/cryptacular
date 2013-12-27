package org.cryptosis.bean;

import org.cryptosis.io.FileResource;
import org.cryptosis.io.Resource;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link KeyStoreFactoryBean}.
 *
 * @author Marvin S. Addison
 */
public class KeyStoreFactoryBeanTest
{
  private static final String KS_PATH = "src/test/resources/keystores/";

  @DataProvider(name = "keystore-data")
  public Object[][] getKeyStoreData()
  {
    return new Object[][] {
        new Object[] { "JCEKS", new FileResource(new File(KS_PATH + "keystore.jceks")), 1 },
        new Object[] { "JKS", new FileResource(new File(KS_PATH + "keystore.jks")), 1 },
        new Object[] { "PKCS12", new FileResource(new File(KS_PATH + "keystore.p12")), 1 },
    };
  }


  @Test(dataProvider = "keystore-data")
  public void testNewInstance(final String type, final Resource resource, final int expectedSize) throws Exception
  {
    final KeyStoreFactoryBean factory = new KeyStoreFactoryBean();
    factory.setType(type);
    factory.setResource(resource);
    factory.setPassword("vtcrypt");
    assertEquals(factory.newInstance().size(), expectedSize);
  }
}
