/*
 * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.test.database.auto;

import com.orientechnologies.orient.client.db.ODatabaseHelper;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.exception.OStorageException;
import com.orientechnologies.orient.core.metadata.security.ORole;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Test(groups = "db")
public class DbCreationTest extends ObjectDBBaseTest {

  private OPartitionedDatabasePool pool;

  @Parameters(value = "url")
  public DbCreationTest(@Optional String url) {
    super(url);
    setAutoManageDatabase(false);

    Orient.instance().getProfiler().startRecording();
  }

  @BeforeClass
  @Override
  public void beforeClass() throws Exception {
    pool = new OPartitionedDatabasePool(url, "admin", "admin");
  }

  @AfterClass
  @Override
  public void afterClass() throws Exception {
    pool.close();
  }

  @BeforeMethod
  @Override
  public void beforeMethod() throws Exception {}

  @AfterMethod
  @Override
  public void afterMethod() throws Exception {}

  @AfterMethod
  public void tearDown() {
    if (url.contains("remote:")) ODatabaseDocumentPool.global().close();
  }

  @Test
  public void testDbCreationDefault() throws IOException {
    if (ODatabaseHelper.existsDatabase(url))
      ODatabaseHelper.dropDatabase(new OObjectDatabaseTx(url), url, getStorageType());

    ODatabaseHelper.createDatabase(new OObjectDatabaseTx(url), url, getStorageType());
  }

  @Test(dependsOnMethods = {"testDbCreationDefault"})
  public void testDbExists() throws IOException {
    Assert.assertTrue(
        ODatabaseHelper.existsDatabase(new ODatabaseDocumentTx(url), getStorageType()));
  }

  @Test(dependsOnMethods = {"testDbExists"})
  public void testDbOpen() {
    database = new OObjectDatabaseTx(url);
    database.open("admin", "admin");
    Assert.assertNotNull(database.getName());
    database.close();
  }

  @Test(dependsOnMethods = {"testDbOpen"})
  public void testDbOpenWithLastAsSlash() {
    database = new OObjectDatabaseTx(url + "/");
    database.open("admin", "admin");
    database.close();
  }

  @Test(dependsOnMethods = {"testDbOpenWithLastAsSlash"})
  public void testDbOpenWithBackSlash() {
    database = new OObjectDatabaseTx(url.replace('/', '\\'));
    database.open("admin", "admin");
    database.close();
  }

  @Test(dependsOnMethods = {"testDbOpenWithBackSlash"})
  public void testChangeLocale() throws IOException {
    database = new OObjectDatabaseTx(url);
    database.open("admin", "admin");
    database
        .command(new OCommandSQL(" ALTER DATABASE LOCALELANGUAGE  ?"))
        .execute(Locale.GERMANY.getLanguage());
    database
        .command(new OCommandSQL(" ALTER DATABASE LOCALECOUNTRY  ?"))
        .execute(Locale.GERMANY.getCountry());
    database.reload();
    Assert.assertEquals(
        database.get(ODatabase.ATTRIBUTES.LOCALELANGUAGE), Locale.GERMANY.getLanguage());
    Assert.assertEquals(
        database.get(ODatabase.ATTRIBUTES.LOCALECOUNTRY), Locale.GERMANY.getCountry());
    database.set(ODatabase.ATTRIBUTES.LOCALECOUNTRY, Locale.ENGLISH.getCountry());
    database.set(ODatabase.ATTRIBUTES.LOCALELANGUAGE, Locale.ENGLISH.getLanguage());
    Assert.assertEquals(
        database.get(ODatabase.ATTRIBUTES.LOCALECOUNTRY), Locale.ENGLISH.getCountry());
    Assert.assertEquals(
        database.get(ODatabase.ATTRIBUTES.LOCALELANGUAGE), Locale.ENGLISH.getLanguage());
    database.close();
  }

  @Test(dependsOnMethods = {"testChangeLocale"})
  public void testRoles() throws IOException {
    database = new OObjectDatabaseTx(url);
    database.open("admin", "admin");
    database.query(new OSQLSynchQuery<ORole>("select from ORole where name = 'admin'"));
    database.close();
  }

  @Test(dependsOnMethods = {"testChangeLocale"})
  public void testSubFolderDbCreate() throws IOException {
    int pos = url.lastIndexOf("/");

    final String u;
    if (pos > -1) u = url.substring(0, pos) + "/sub/subTest";
    else {
      pos = url.lastIndexOf(":");
      u = url.substring(0, pos + 1) + "sub/subTest";
    }

    ODatabaseDocumentTx db = new ODatabaseDocumentTx(u);

    ODatabaseHelper.dropDatabase(db, getStorageType());
    ODatabaseHelper.createDatabase(db, u, getStorageType());
    db.open("admin", "admin");
    db.close();

    ODatabaseHelper.dropDatabase(db, getStorageType());
  }

  @Test(dependsOnMethods = {"testChangeLocale"})
  public void testSubFolderDbCreateConnPool() throws IOException {
    int pos = url.lastIndexOf("/");

    final String u;
    if (pos > -1) u = url.substring(0, pos) + "/sub/subTest";
    else {
      pos = url.lastIndexOf(":");
      u = url.substring(0, pos + 1) + "sub/subTest";
    }

    ODatabaseDocumentTx db = new ODatabaseDocumentTx(u);

    ODatabaseHelper.dropDatabase(db, getStorageType());
    ODatabaseHelper.createDatabase(db, u, getStorageType());

    db = ODatabaseDocumentPool.global().acquire(u, "admin", "admin");
    if (u.startsWith("remote:")) db.close();

    ODatabaseHelper.dropDatabase(db, getStorageType());
  }

  @Test(dependsOnMethods = "testSubFolderDbCreateConnPool")
  public void testCreateAndConnectionPool() throws IOException {
    ODatabaseDocument db = new ODatabaseDocumentTx(url);

    db.activateOnCurrentThread();
    ODatabaseHelper.dropDatabase(db, getStorageType());

    ODatabaseHelper.createDatabase(db, url, getStorageType());

    if (pool != null) {
      pool.close();
    }
    pool = new OPartitionedDatabasePool(url, "admin", "admin");

    // Get connection from pool
    db = pool.acquire();
    db.close();

    // Destroy db in the back of the pool
    db = new ODatabaseDocumentTx(url);
    ODatabaseHelper.dropDatabase(db, getStorageType());

    // Re-create it so that the db exists for the pool
    db = new ODatabaseDocumentTx(url);
    ODatabaseHelper.createDatabase(db, url, getStorageType());
  }

  @Test(dependsOnMethods = {"testCreateAndConnectionPool"})
  public void testOpenCloseConnectionPool() throws IOException {
    ODatabaseDocumentTx db = new ODatabaseDocumentTx(url);
    if (!ODatabaseHelper.existsDatabase(db, null)) {
      ODatabaseHelper.createDatabase(db, url, getStorageType());
      db.close();
    }
    if (pool != null) {
      pool.close();
    }
    pool = new OPartitionedDatabasePool(url, "admin", "admin");

    for (int i = 0; i < 500; i++) {
      pool.acquire().close();
    }
  }

  @Test(dependsOnMethods = {"testChangeLocale"})
  public void testSubFolderMultipleDbCreateSameName() throws IOException {
    int pos = url.lastIndexOf("/");
    String u = url;

    if (pos > -1) u = url.substring(0, pos);
    else {
      pos = url.lastIndexOf(":");
      u = url.substring(0, pos + 1);
    }

    for (int i = 0; i < 3; ++i) {
      String ur = u + "/" + i + "$db";
      ODatabaseDocumentTx db = new ODatabaseDocumentTx(ur);

      try {
        ODatabaseHelper.dropDatabase(db, getStorageType());
      } catch (OStorageException e) {
        Assert.assertTrue(e.getCause().getMessage().contains("doesn't exits."));
      }
      ODatabaseHelper.createDatabase(db, ur, getStorageType());
      Assert.assertTrue(ODatabaseHelper.existsDatabase(db, getStorageType()));
      db.open("admin", "admin");
    }

    for (int i = 0; i < 3; ++i) {
      String ur = u + "/" + i + "$db";
      ODatabaseDocumentTx db = new ODatabaseDocumentTx(ur);
      Assert.assertTrue(ODatabaseHelper.existsDatabase(db, getStorageType()));
      db.activateOnCurrentThread();
      ODatabaseHelper.dropDatabase(db, getStorageType());
      Assert.assertFalse(ODatabaseHelper.existsDatabase(db, getStorageType()));
    }
  }

  public void testZipCompression() {
    if (database == null || !database.getURL().startsWith("plocal:")) return;

    OGlobalConfiguration.STORAGE_COMPRESSION_METHOD.setValue("gzip");

    final String buildDirectory = System.getProperty("buildDirectory", ".");
    String dburl = "plocal:" + buildDirectory + "/test-db/" + this.getClass().getSimpleName();

    final OrientGraphFactory factory = new OrientGraphFactory(dburl, "admin", "admin");
    if (factory.exists()) factory.drop();
    factory.close();
    OrientGraphNoTx db = factory.getNoTx();
    db.drop();
    OGlobalConfiguration.STORAGE_COMPRESSION_METHOD.setValue(
        OGlobalConfiguration.STORAGE_COMPRESSION_METHOD.getValue());
  }

  public void testDbIsNotRemovedOnSecondTry() {
    final String buildDirectory =
        new File(System.getProperty("buildDirectory", ".")).getAbsolutePath();
    final String dbPath =
        buildDirectory + File.separator + this.getClass().getSimpleName() + "Remove";
    final String dburl = "plocal:" + dbPath;

    ODatabaseDocumentTx db = new ODatabaseDocumentTx(dburl);
    db.create();
    db.close();

    Assert.assertTrue(new File(dbPath).exists());

    final ODatabaseDocumentTx dbTwo = new ODatabaseDocumentTx(dburl);
    try {
      dbTwo.create();
      Assert.fail();
    } catch (ODatabaseException e) {
      // ignore all is correct
    }

    Assert.assertTrue(new File(dbPath).exists());

    db = new ODatabaseDocumentTx(dburl);

    db.open("admin", "admin");
    db.drop();
  }

  public void testDbOutOfPath() throws IOException {
    if (!url.startsWith("remote")) return;

    // TRY UNIX PATH
    try {
      ODatabaseDocumentTx db = new ODatabaseDocumentTx("remote:/db");
      db.open("admin", "admin");
      Assert.fail("Security breach: database with path /db was created");
    } catch (Exception e) {
    }

    // TRY WINDOWS PATH
    try {
      ODatabaseDocumentTx db = new ODatabaseDocumentTx("remote:C:/db");
      db.open("admin", "admin");
      Assert.fail("Security breach: database with path c:/db was created");
    } catch (Exception e) {
    }
  }
}
