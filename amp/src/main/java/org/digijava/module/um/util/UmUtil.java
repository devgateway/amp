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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;


public class UmUtil {

    public static final Comparator organizationNameComparator;
    private static final Random rand = new Random((new Date()).getTime());
    private static final Logger logger = Logger.getLogger(UmUtil.class);
    
    // Environment variable and system property names for master encryption key
    private static final String TRU_BUDGET_MASTER_KEY_ENV = "TRU_BUDGET_MASTER_KEY";
    private static final String TRU_BUDGET_MASTER_KEY_PROP = "trubudget.master.key";
    
    // PBKDF2 parameters for key derivation
    private static final int PBKDF2_ITERATIONS = 100000; // High iteration count for security
    private static final int AES_KEY_LENGTH = 256; // Use AES-256
    private static final int SALT_LENGTH = 16; // 16 bytes salt
    private static final Pattern TRU_BUDGET_PASSWORD_POLICY =
            Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,16}$");

    public static boolean isValidTruBudgetPassword(String password) {
        return password != null && TRU_BUDGET_PASSWORD_POLICY.matcher(password).matches();
    }

    /**
     * Encrypt TruBudget password using master key (secure method).
     * Uses PBKDF2 to derive a user-specific key from the master key and user identifier.
     * The master key should be stored in environment variable TRU_BUDGET_MASTER_KEY
     * or system property trubudget.master.key.
     * 
     * @param plaintext The password to encrypt
     * @param userIdentifier User identifier (typically email) used for key derivation
     * @return Encrypted password in format: "v2:" + Base64(IV + Salt + EncryptedData)
     * @throws Exception If encryption fails or master key is not configured
     */
    public static String encryptTruBudgetPassword(String plaintext, String userIdentifier) throws Exception {
        String masterKey = getMasterEncryptionKey();
        if (masterKey == null || masterKey.isEmpty()) {
            throw new Exception("TruBudget master encryption key not configured. " +
                    "Please set environment variable " + TRU_BUDGET_MASTER_KEY_ENV + 
                    " or system property " + TRU_BUDGET_MASTER_KEY_PROP);
        }
        
        SecureRandom secureRandom = new SecureRandom();
        
        // Generate random salt for key derivation
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        
        // Generate random IV for encryption
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        
        // Derive user-specific key using PBKDF2
        SecretKey derivedKey = deriveKeyFromMasterKey(masterKey, userIdentifier, salt);
        
        // Encrypt the password
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, derivedKey, new IvParameterSpec(iv));
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        
        // Combine salt, IV, and encrypted data: [salt(16) + iv(16) + encrypted]
        byte[] combined = new byte[salt.length + iv.length + encryptedBytes.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(iv, 0, combined, salt.length, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, salt.length + iv.length, encryptedBytes.length);
        
        // Prefix with "v2:" to indicate new encryption format
        return "v2:" + Base64.getEncoder().encodeToString(combined);
    }
    
    /**
     * Decrypt TruBudget password. Supports both new (master key) and legacy (per-user key) encryption.
     * 
     * @param ciphertext Encrypted password (may be in "v2:" format or legacy format)
     * @param userIdentifier User identifier (typically email) - required for v2 format
     * @param legacyKey Legacy encryption key (from truBudgetKeyGen) - required for legacy format
     * @return Decrypted password
     * @throws Exception If decryption fails
     */
    public static String decryptTruBudgetPassword(String ciphertext, String userIdentifier, String legacyKey) throws Exception {
        if (ciphertext == null || ciphertext.isEmpty()) {
            throw new Exception("Ciphertext cannot be null or empty");
        }
        
        // Check if it's the new v2 format
        if (ciphertext.startsWith("v2:")) {
            return decryptTruBudgetPasswordV2(ciphertext.substring(3), userIdentifier);
        } else {
            // Legacy format - use the old method
            return decrypt(ciphertext, legacyKey);
        }
    }
    
    /**
     * Decrypt TruBudget password using master key (v2 format).
     * 
     * @param ciphertextBase64 Base64 encoded encrypted data (without "v2:" prefix)
     * @param userIdentifier User identifier used for key derivation
     * @return Decrypted password
     * @throws Exception If decryption fails
     */
    private static String decryptTruBudgetPasswordV2(String ciphertextBase64, String userIdentifier) throws Exception {
        String masterKey = getMasterEncryptionKey();
        if (masterKey == null || masterKey.isEmpty()) {
            throw new Exception("TruBudget master encryption key not configured");
        }
        
        byte[] combined = Base64.getDecoder().decode(ciphertextBase64);
        
        // Extract salt, IV, and encrypted data
        byte[] salt = new byte[SALT_LENGTH];
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - salt.length - iv.length];
        
        System.arraycopy(combined, 0, salt, 0, salt.length);
        System.arraycopy(combined, salt.length, iv, 0, iv.length);
        System.arraycopy(combined, salt.length + iv.length, encryptedBytes, 0, encryptedBytes.length);
        
        // Derive the same key using the salt
        SecretKey derivedKey = deriveKeyFromMasterKey(masterKey, userIdentifier, salt);
        
        // Decrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, derivedKey, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    /**
     * Derives a user-specific AES-256 key from the master key using PBKDF2.
     * 
     * @param masterKey The master encryption key
     * @param userIdentifier User identifier (typically email)
     * @param salt Random salt for key derivation
     * @return Derived SecretKey for AES-256
     * @throws Exception If key derivation fails
     */
    private static SecretKey deriveKeyFromMasterKey(String masterKey, String userIdentifier, byte[] salt) throws Exception {
        // Combine master key and user identifier for key derivation
        String keyMaterial = masterKey + ":" + userIdentifier;
        
        PBEKeySpec spec = new PBEKeySpec(keyMaterial.toCharArray(), salt, PBKDF2_ITERATIONS, AES_KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        
        return new SecretKeySpec(keyBytes, "AES");
    }
    
    /**
     * Gets the master encryption key from global settings (trubudget section).
     * Falls back to environment variable or system property for backward compatibility.
     * 
     * @return Master encryption key, or null if not configured
     */
    private static String getMasterEncryptionKey() {
        try {
            // Try global settings first (preferred method)
            List<org.digijava.module.aim.dbentity.AmpGlobalSettings> settings = 
                org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection("trubudget");
            if (settings != null && !settings.isEmpty()) {
                try {
                    String key = org.digijava.module.um.util.DbUtil.getSettingValue(settings, "masterEncryptionKey");
                    if (key != null && !key.isEmpty()) {
                        return key;
                    }
                } catch (Exception e) {
                    // Setting not found, continue to fallback methods
                    logger.debug("masterEncryptionKey not found in global settings, trying fallback methods");
                }
            }
        } catch (Exception e) {
            logger.warn("Error reading master encryption key from global settings: " + e.getMessage());
        }
        
        // Fallback to environment variable (for backward compatibility)
        String key = System.getenv(TRU_BUDGET_MASTER_KEY_ENV);
        if (key != null && !key.isEmpty()) {
            logger.warn("Using master encryption key from environment variable. " +
                       "Consider migrating to global settings for better security.");
            return key;
        }
        
        // Fall back to system property (for backward compatibility)
        key = System.getProperty(TRU_BUDGET_MASTER_KEY_PROP);
        if (key != null && !key.isEmpty()) {
            logger.warn("Using master encryption key from system property. " +
                       "Consider migrating to global settings for better security.");
            return key;
        }
        
        return null;
    }
    
    /**
     * Legacy encrypt method - kept for backward compatibility.
     * For new code, use encryptTruBudgetPassword() instead.
     * 
     * @param plaintext
     * @param secretKey
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
     * Legacy decrypt method - kept for backward compatibility.
     * For new code, use decryptTruBudgetPassword() instead.
     * 
     * @param ciphertext
     * @param secretKey
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
                    Iterator iter2 = interests.iterator();
                    while (iter2.hasNext()) {
                        Interests item2 = (Interests) iter2.next();
                        if (item2.getSite().getId().longValue() ==
                            item.getId().longValue()) {
                            item2.setSiteUrl(DgUtil.getSiteUrl(item, request));
                            if (item.getName() == null ||
                                item.getName().length() <= 0)
                                item2.setSiteDescription(item.getSiteId());
                            else
                                item2.setSiteDescription(item.getName());
                            sets.add(item2);
                            if( selected != null )
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
