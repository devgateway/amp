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

package org.digijava.kernel.persistence;

/**
 * This interface defines the Constants used for Caching.
 * This defines different Region Names , Group Names and Keys.
 * Any Component making use of CacheManger has to read these names from here.
*/

public interface CacheConstants {

    /**
    * Region Name for caching objects under DGMarket
    */
    public static final String DGMARKET_REGION_NAME = "DGMARKET";
    /**
    * List of allowed region Names
    */
    //Everytime a new region name is added to this constants file , it should be added into this array too
    //public static final String[] REGION_NAME_LIST =
    //  { DGMARKET_REGION_NAME };

    //Group Names under DGMARKET region
    /**
    * Group Name for Country object under DGMARKET region
    */
    public static final String DGMARKET_COUNTRY_GROUPNAME = "DgMarket.Country";

    /**
    * Group Name for Region object under DGMARKET region
    */
    public static final String DGMARKET_REGION_GROUPNAME = "DgMarket.Region";


    /**
    * Group Name for Funding Agency object under DGMARKET region
    */
    public static final String DGMARKET_AGENCY_GROUPNAME = "DgMarket.Agency";


    /**
    * Group Name for Search object under DGMARKET region
    */
    public static final String DGMARKET_SEARCH_GROUPNAME = "DgMarket.Search";


    //Region names for different portals

    /**
    * Region Name for caching objects under AiDA
    */
    public static final String CORE_REGION_NAME = "CORE";
    public static final String AiDA_REGION_NAME = "AiDA";
    public static final String TRANSLATOR_REGION_NAME = "TRANSLATOR";
    /**
    * List of allowed region Names
    */
    //Everytime a new region name is added to this constants file , it should be added into this array too
    public static final String[] REGION_NAME_LIST =
        { DGMARKET_REGION_NAME, CORE_REGION_NAME, AiDA_REGION_NAME, TRANSLATOR_REGION_NAME };

    //Group Names under CORE region
    /**
    * Group Name for Region object under CORE region
    */
    public static final String CORE_REGION_GROUPNAME = "Core.Region";

    /**
    * Group Name for UN Region object under CORE region
    */
    public static final String CORE_UN_REGION_GROUPNAME = "Core.UN.Region";

    /**
    * Group Name for Organization object under CORE region
    */
    public static final String CORE_ORGANIZATION_GROUPNAME =
        "Core.Organization";

    /**
    * Group Name for Donor object under CORE region
    */
    public static final String CORE_DONOR_GROUPNAME = "Core.Donor";

    //Group Names under AiDA region
    /**
    * Group Name for Country object under AiDA region
    */
    public static final String AiDA_COUNTRY_GROUPNAME = "AiDA.Country";

    /**
    * Group Name for Donor object under AiDA region
    */
    public static final String AiDA_DONOR_GROUPNAME = "AiDA.Donor";

    /**
    * Group Name for Sector object under AiDA region
    */
    public static final String AiDA_SECTOR_GROUPNAME = "AiDA.Sector";

    /**
    * Group Name for Topic Window object under AiDA region
    */
    public static final String AiDA_TOPICWINDOW_GROUPNAME = "AiDA.TopicWindow";

    /**
    * Group Name for Topic object under AiDA region
    */
    public static final String AiDA_TOPIC_GROUPNAME = "AiDA.Topic";

    /**
    * Group Name for Country object under AiDA region
    */
    public static final String AiDA_RESOURCES_GROUPNAME = "AiDA.Resources";

    /**
    * Group Name for Activity object under AiDA region
    */
    public static final String AiDA_ACTIVITY_GROUPNAME = "AiDA.Activity";

    /**
    * Group Name for Source object under AiDA region
    */
    public static final String AiDA_SOURCE_GROUPNAME = "AiDA.Source";

    /**
    * Group Name for OrgType object under AiDA region
    */
    public static final String AiDA_ORGTYPE_GROUPNAME = "AiDA.OrgType";

    /**
    * Group Name for Search object under AiDA region
    */
    public static final String AiDA_SEARCH_GROUPNAME = "AiDA.Search";

    //Constants for Cache Attributes
    /**
    * Default key for specifying attributes
    */
    public static final String DEFAULT_ATTRIB_KEY = "DGSystem.Default";

    /**
    * Attribute key for specifying MaxLifeSeconds
    * This will be appended with a "." followed by the object key / group name / default key
    */
    public static final String ATTR_MAX_LIFE_SECS = "MaxLifeSeconds";

    /**
    * Attribute key for specifying whether the cache is enabled.
    * A value of 'Yes' or 'true' will turn the cache on for the group.
    * This will be appended with a "." followed by the object  group name / default key
    */
    public static final String ATTR_IS_CACHE_ENABLED = "CacheEnabled";

    //This is a translator region definition
    public static final String TRANSLATOR_GROUP_NAME =
        "Translator.DatabaseMessageResources";

    public static final String DGMARKET_CPV_NOTICENAME = "DgMarket.Notice";

    public static final String DGMARKET_CPV_GROUPNAME  = "DgMarket.CPV";

    public static final String DGMARKET_DACON_GROUPNAME = "DgMarket.Dacon";

    public static String SEARCH_PRECACHE_GROUPNAME = "DgMarket.Eproc.Keyword.PreCache";


}
