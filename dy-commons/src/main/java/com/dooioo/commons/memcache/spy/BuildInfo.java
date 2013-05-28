// generated by src/scripts/write-version-info.sh

package com.dooioo.commons.memcache.spy;

import java.net.URL;
import java.util.Properties;

import java.io.InputStream;
import java.io.FileNotFoundException;

public final class BuildInfo extends Properties {
  public static final String VERSION="2.8.2";
  public static final String GIT_HASH="bda95eccd29f881c5c7f4fc92ed1efa2e42d1890";
  public static final String TREE_VERSION="2.8.2";
  public static final String COMPILE_USER="ingenthr";
  public static final String COMPILE_HOST="ingenthr-mbp.local";
  public static final String COMPILE_DATE="Mon Jul 30 18:15:16 PDT 2012";

  /**
   * Get an instance of BuildInfo that describes the spy.jar build.
   */
  public BuildInfo() {
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(256);
    sb.append("Spymemcached ");
    sb.append(VERSION);
    sb.append("\n\nTree Version: ");
    sb.append(TREE_VERSION);
    sb.append("\nLast Commit ID: ");
    sb.append(GIT_HASH);
    sb.append("\n\nCompiled by ");
    sb.append(COMPILE_USER);
    sb.append("@");
    sb.append(COMPILE_HOST);
    sb.append(" on ");
    sb.append(COMPILE_DATE);
    return sb.toString();
  }

  public URL getFile(String rel) throws FileNotFoundException {
    ClassLoader cl=getClass().getClassLoader();
    URL u=cl.getResource(rel);
    if(u == null) {
      throw new FileNotFoundException("Can't find " + rel);
    }
    return(u);
  }

  public static void main(String args[]) throws Exception {
    BuildInfo bi=new BuildInfo();
    String cl="%" + "CHANGELOG" + "%";

    System.out.println(bi);

    // If there was a changelog, let it be shown.
    if(!cl.equals("net/spy/memcached/changelog.txt")) {
      if(args.length > 0 && args[0].equals("-c")) {
        System.out.println(" -- Changelog:\n");

        URL u=bi.getFile("net/spy/memcached/changelog.txt");
        InputStream is=u.openStream();
        try {
          byte data[]=new byte[8192];
          int bread=0;
          do {
            bread=is.read(data);
            if(bread > 0) {
              System.out.write(data, 0, bread);
            }
          } while(bread != -1);
        } finally {
          is.close();
        }
      } else {
        System.out.println("(add -c to see the recent changelog)");
      }
    }
  }
}
