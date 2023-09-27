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

package org.digijava.module.um.util;

import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.ContentAlert;
import org.digijava.kernel.entity.Interests;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.dbentity.SuspendLogin;
import org.digijava.module.um.exception.UMException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;


public class UmUtil {


    public static final Comparator organizationNameComparator;
    private static final Random rand = new Random((new Date()).getTime());

    /**
     * encrypt trubudget password
     * @param plaintext
     * @return encrypted password
     */
    public static String encrypt(String plaintext, String secretKey) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Combine IV and encrypted data and encode in Base64
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }
    public static String generateAESKey(int keyLength) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyLength); // Key length in bits (128, 192, or 256)
        SecretKey key = keyGen.generateKey();
        byte[] keyBytes = key.getEncoded();
        StringBuilder hexString = new StringBuilder();
        for (byte b : keyBytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }


    /**
     * decrypt trubudget password
     * @param ciphertext
     * @return decrypted password
     */

    public static String decrypt(String ciphertext, String secretKey) throws Exception {
        byte[] combined = Base64.getDecoder().decode(ciphertext);
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Get random code
     *
     * @return
     * @throws UMException
     */
    public static String getRandomSHA1() throws UMException {
        String string = "RANDOMSH12F";
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            MessageDigest md = MessageDigest.getInstance("SHA");

            md.update(string.getBytes());
            byte[] digest = md.digest();
            random.setSeed(digest);
            digest = random.generateSeed(20);
            BigInteger integer = new BigInteger(1, digest);
            return integer.toString(16);
        }
        catch (Exception ex) {
            throw new UMException("getRandomSHA1() failed",ex);
        }
    }

    /**
     *
     * @param interests
     * @param request
     * @return
     * @throws UMException
     */
    public static Set getUserInterests(Set interests, List sites, String[] selected,
                                    HttpServletRequest request) throws
        UMException {

        Set sets = new HashSet();
        if( sites != null ) {
            Iterator iter = sites.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Site item = (Site) iter.next();
                if( interests != null ) {
                    for (Object interest : interests) {
                        Interests item2 = (Interests) interest;
                        if (item2.getSite().getId().longValue() ==
                                item.getId().longValue()) {
                            item2.setSiteUrl(DgUtil.getSiteUrl(item, request));
                            if (item.getName() == null ||
                                    item.getName().length() <= 0)
                                item2.setSiteDescription(item.getSiteId());
                            else
                                item2.setSiteDescription(item.getName());
                            sets.add(item2);
                            if (selected != null)
                                selected[i++] = item.getId().toString();
                            break;
                        }
                    }
                }
            }
        }

        return sets;
    }


    /**
     *
     * @param interests
     * @param request
     * @return
     * @throws UMException
     */
    public static ArrayList getGenerateInterests(Set interests, List sites, String[] selected,
                                    HttpServletRequest request) throws
        UMException {

        boolean add = false;

        User currentUser = RequestUtils.getUser(request);

        ArrayList sets = new ArrayList();
        if( sites != null ) {
            Iterator iter = sites.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Site item = (Site) iter.next();

                add = false;
                // ----- Find interests
                if( interests != null ) {
                    Iterator iter2 = interests.iterator();
                    while (iter2.hasNext()) {
                        Interests item2 = (Interests) iter2.next();
                        if (item2.getSite().getId().longValue() ==
                            item.getId().longValue()) {
                            Interests interest = new Interests();
                            interest.setContentAlert(new ContentAlert(item2.getContentAlert().getValue(),item2.getContentAlert().getName()));
                            interest.setReceiveAlerts(item2.isReceiveAlerts());
                            interest.setSite(item2.getSite());
                            interest.setSiteUrl(DgUtil.getSiteUrl(item2.getSite(), request));

                            if( item.getName() == null || item.getName().length() <= 0 )
                                interest.setSiteDescription(item.getSiteId());
                            else
                                interest.setSiteDescription(item.getName());
                            sets.add(interest);
                            if (selected != null)
                                selected[i++] = item.getId().toString();
                            add = true;
                            break;
                        }
                    }
                }
                // -------

                if (!add && !item.isInvisible() && !item.isSecure()) {
                    sets.add(createInterests(item, request));
                }
            }
        }

        return sets;
    }


    /**
     * Get organization full name by id
     *
     * @param type
     * @return
     * @throws UMException
     */
    public static String getOrganizationTypeById(String id) throws
        UMException {

        List organizationType = DbUtil.getOrganizationTypes();

        Iterator iter = organizationType.iterator();
        while (iter.hasNext()) {
            OrganizationType item = (OrganizationType) iter.next();
            if( item.getId().equalsIgnoreCase(id) ) {
                return item.getType();
            }
        }

        return "";
    }


    /**
     * Get country name by iso
     *
     * @param type
     * @return
     * @throws UMException
     */
    public static String getCountryNameByIso(String iso) throws
        UMException {

        List countries = DbUtil.getCountries();

        Iterator iter = countries.iterator();
        while (iter.hasNext()) {
            Country item = (Country) iter.next();
            if( item.getIso().equalsIgnoreCase(iso) ) {
                return item.getCountryName();
            }
        }

        return "";
    }


    /**
     *
     * @param site
     * @param request
     * @return
     */
    public static Interests createInterests(Site site, HttpServletRequest request) {
        Interests interest = new Interests();
        interest.setContentAlert( new ContentAlert( new Long(604800) ) );
        interest.setReceiveAlerts(false);
        interest.setSite(site);
        interest.setSiteUrl(DgUtil.getSiteUrl(site, request));
        if( site.getName() == null || site.getName().length() <= 0 )
            interest.setSiteDescription(site.getSiteId());
        else
            interest.setSiteDescription(site.getName());

            return interest;
    }
    
    public static AmpTeamMember assignWorkspaceToUser(HttpServletRequest request,Long roleId ,User user, AmpTeam ampTeam) {
        AmpTeamMember newMember = null;
        AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(roleId);
        if (role != null) {
            newMember = new AmpTeamMember();
            newMember.setUser(user);
            newMember.setAmpTeam(ampTeam);
            newMember.setAmpMemberRole(role);
            // add the default application settings for the user  
//          AmpApplicationSettings ampAppSettings = org.digijava.module.aim.util.DbUtil.getTeamAppSettings(ampTeam.getAmpTeamId());
//          AmpApplicationSettings newAppSettings = new AmpApplicationSettings();
//          //newAppSettings.setTeam(ampAppSettings.getTeam());
//          newAppSettings.setTeam(newMember.getAmpTeam());
//          newAppSettings.setMember(newMember);
//          newAppSettings.setDefaultRecordsPerPage(ampAppSettings
//                  .getDefaultRecordsPerPage());
//          newAppSettings.setCurrency(ampAppSettings.getCurrency());
//          newAppSettings.setFiscalCalendar(ampAppSettings
//                  .getFiscalCalendar());
//          newAppSettings.setLanguage(ampAppSettings.getLanguage());
//          newAppSettings.setUseDefault(new Boolean(true));
            Site site = RequestUtils.getSite(request);
            try{
                TeamUtil.addTeamMember(newMember,site);             
            }catch (Exception e){
                    e.printStackTrace();
                    //logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from team: "+ newMember.getAmpTeam().getName());
            }           
        }
        return newMember;
    }

    public static List<SuspendLogin> getUserSuspendReasons (User user) {
        return DbUtil.getUserSuspendReasonsFromDB(user);
    }


    static {
        organizationNameComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                OrganizationType org1 = (OrganizationType) o1;
                OrganizationType org2 = (OrganizationType) o2;

                return org1.getType().compareTo(org2.getType());
            }
        };
    }


}
