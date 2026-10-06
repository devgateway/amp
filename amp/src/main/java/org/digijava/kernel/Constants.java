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

package org.digijava.kernel;

import java.io.File;

/**
 * Digi Constant class
 * @author Mikheil Kapanadze
 * @version 1.0
 */

public class Constants
{
  public final static String DEFAULT_SITE_NAME = "default";
  public static final String SITE_ID = "javax.servlet.include.site_id";
  public static final String INSTANCE_ID = "javax.servlet.include.instance_id";
  public static final String BASE_URL = "javax.servlet.include.base_url";
  public static final String DIGI_PARAM_MAP = "digijava.param.map";
  public static final String DIGI_PARAM_PARSED = "digijava.param.parsed";
  public static final String DEFAULT_INSTANCE = "default";
  @Deprecated
  public static final String REQUEST_DB_SESSION = "requestDBSession";

  /**
   * The request attribute under which the path information is stored for
   * processing during a RequestDispatcher.include() call.
   */
  public static final String INCLUDE_PATH_INFO =
      "javax.servlet.include.path_info";


  /**
   * The request attribute under which the servlet path information is stored
   * for processing during a RequestDispatcher.include() call.
   */
  public static final String INCLUDE_SERVLET_PATH =
      "javax.servlet.include.servlet_path";

  /**
   * @deprecated use attribute, got by MODULE_INSTANCE_OBJECT from request
   */
  public static final String MODULE_NAME =
      "org.digijava.kernel.module_name";

  /**
   * @deprecated use attribute, got by MODULE_INSTANCE_OBJECT from request
   */
  public static final String MODULE_INSTANCE =
      "org.digijava.kernel.module_instance";

  public static final String MODULE_INSTANCE_OBJECT =
      "org.digijava.kernel.module_instance_object";

  public static final String DIGI_CONTEXT =
      "org.digijava.kernel.digi_context";

 public static final String MASTER_LAYOUT =
     "org.digijava.kernel.masterLayout";

 public static final String CURRENT_SITE =
      "org.digijava.kernel.current_site";

  public static final String REQUEST_ALREADY_PROCESSED =
      "org.digijava.kernel.request_already_processed";

  /**
   * @deprecated use RequestUtils.getUser() to access user instead
   */
  public static final String USER =
      "org.digijava.kernel.user";

  public static final String REQUEST_USER =
      "org.digijava.kernel.user";

  public static final String SOURCE_USER =
      "org.digijava.kernel.source_user";

  public static final String NAVIGATION_LANGUAGE =
      "org.digijava.kernel.navigation_language";

    public static final String FORCED_LANGUAGE = "org.digijava.kernel.request_forced_language";
  
  public static final String ACTION_ROLES_PROCESS_RESULT =
      "org.digijava.kernel.action_roles_process_result";

  public static final String ORIGINAL_MAPPING =
      "org.digijava.kernel.original_mapping";

  public static final String MAIN_ACTION_FORM =
      "org.digijava.kernel.main_action_form";

  public static final String COOKIE_NAME = "org.digijava.kernel.single_sign_on";

  public static final String DG_REDIRECT_LOGON = "org.digijava.kernel.dg_redirect_logon";

  public static final String DG_UM_MODULE_ID = "org.digijava.kernel.dg_um_module_id";

  public static final String PROPERTY_CONTEXT = "context";

  public static final String PROPERTY_MODULE = "module";

  public static final String PROPERTY_MODULEINSTANCE = "moduleinstance";

  public static final int HTTP_DEFAULT_PORT = 80;

  public static final String REQUEST_URL = "org.digijava.kernel.dg_request_url";

  public static final String REFERRER_PARAM = "rfr";

  public static final String APP_SCOPE_REGION = "org.digijava.kernel.caches.application_scope";

  public static final int NUMBER_OF_ITEMS_IN_TEASER = 3;

  /**
   * @todo change with org.digijava.module.translation.translation_mode
   */
  public static final String TRANSLATION_MODE_INDICATOR = "mode";

  /**
   * @todo change with org.digijava.module.translation.translation_type
   */
  public static final String TRANSLATION_MODE_TYPE = "type";

  public static final String ALL_TOPICS_SITE = "alltopics";

  public static final String DG_ACTION_CONFIG = "org.digijava.kernel.dg_action_config";

  public static final String TRANSLATION_PARAMETERS = "org.digijava.kernel.translation_parameters";

  public static final String DG_UID_PARAMETER = "uid";

  public static final String DG_SESSION_ID_PARAMETER = "dgsessionid";

  public static final String KERNEL_QUERY_CACHE_REGION = "org.digijava.kernel.query_cache";

  public static final String DEVELPOMENT_MODE = "org.digijava.kernel.development_mode";

  public static final String ACTION_INFORMATION = "org.digijava.kernel.action_information";

    private static final String AMP_HOME_ENV_VAR = "AMP_HOME";
    public static final String AMP_HOME = determineAmpHome();

    public static final File AMP_OFFLINE_RELEASES = new File(AMP_HOME, "amp-offline-releases");

    private static String determineAmpHome() {
        if (System.getProperties().contains(AMP_HOME_ENV_VAR)) {
            return System.getProperty(AMP_HOME_ENV_VAR);
        }
        if (System.getenv(AMP_HOME_ENV_VAR) != null) {
            return System.getenv(AMP_HOME_ENV_VAR);
        }
        return new File(System.getProperty("user.home"), ".amp").getAbsolutePath();
    }
}
