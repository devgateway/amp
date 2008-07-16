/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.db;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.entity.OracleLocale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.util.TrnCountry;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.digijava.kernel.translator.util.TrnUtil;
import java.util.Collections;

public class OracleUtils {

  private static final String CACHE_REGION =
      "org.digijava.kernel.translator.util.TranslatorUtil.Query";

  private static Logger logger = Logger.getLogger(OracleUtils.class);

   /**
   * Returns collection of TrnCountry objects, translated to the given language
   * @param oracleLocale Oracle locale code
   * @return collection of TrnCountry objects
   * @throws DgException
   * @see org.digijava.kernel.translator.util.TrnCountry
   */
  public static Collection getCountries(String oracleLocale) throws DgException {
    List retVal = null;
    HashSet countries = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();

      Query query = session.createQuery(
          "select c.iso, msg.message from " +
          Country.class.getName() + " c, " +
          Message.class.getName() + " msg " +
          OracleLocale.class.getName() + " orloc " +
          " where msg.key=c.messageLangKey and msg.siteId='0' and c.available=true and msg.locale=orloc.locale.code and orloc.oracleLocale=:oracleLocale "
          );
      query.setString("oracleLocale", oracleLocale);
      query.setCacheable(true);
      query.setCacheRegion(CACHE_REGION);
      countries = new HashSet();

      Iterator iter = query.list().iterator();
      while (iter.hasNext()) {
        Object[] item = (Object[]) iter.next();
        TrnCountry country = new TrnCountry( (String) item[0], (String) item[1]);
        countries.add(country);
      }

      query = session.createQuery(
          "select c.iso, c.countryName from " +
          Country.class.getName() +
          " c where c.available=true"
          );
      query.setCacheable(true);
      query.setCacheRegion(CACHE_REGION);

      iter = query.list().iterator();
      while (iter.hasNext()) {
        Object[] item = (Object[]) iter.next();
        TrnCountry country = new TrnCountry( (String) item[0], (String) item[1]);
        if (!countries.contains(country)) {
          countries.add(country);
        }
      }

    }
    catch (Exception ex) {
      logger.debug("Unable to get Countries ", ex);
      throw new DgException("Unable to get Countries ", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex1) {
        logger.warn("releaseSession() failed ", ex1);
      }
    }

    if (countries != null) {
      retVal = new ArrayList(countries);
      Collections.sort(retVal, TrnUtil.countryNameComparator);
    }

    return retVal;
  }
}
