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

package org.digijava.kernel.entity;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "EP_ADDRESSES")
/** @author shamir.karkal */
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSeqGen")
    @SequenceGenerator(name = "addressSeqGen", sequenceName = "EP_ADDRESSES_seq", allocationSize = 1)
    @Column(name = "ADDRESS_ID")
    private Long addressId;

    @Column(name = "ADDRESS", length = 400)
    private String address;

    @Column(name = "ADDRESS2", length = 400)
    private String address2;

    @Column(name = "CITY", length = 200)
    private String city;

    @Column(name = "STATE", length = 200)
    private String state;

    @Column(name = "COUNTRY", length = 200)
    private String country;

    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;

    @Column(name = "POSTAL_CODE", length = 100)
    private String postalCode;

    @Column(name = "FIRST_NAMES", length = 200)
    private String firstNames;

    @Column(name = "LAST_NAME", length = 200)
    private String lastName;

    @Column(name = "TITLE", length = 200)
    private String title;

    @Column(name = "DEPARTMENT", length = 200)
    private String department;

    @Column(name = "PHONE", length = 100)
    private String phone;

    @Column(name = "FAX", length = 100)
    private String fax;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "URL", length = 300)
    private String url;

    @Column(name = "OLD_ADDRESS_ID")
    private Long oldAddressId;


    /** nullable persistent field */
    private Long agencyId;





    public Address(java.lang.Long addressId, java.lang.Long agencyId, java.lang.String address, java.lang.String address2, java.lang.String city, java.lang.String state, java.lang.String country, java.lang.String countryCode, java.lang.String postalCode, java.lang.String firstNames, java.lang.String lastName, java.lang.String title, java.lang.String department, java.lang.String phone, java.lang.String fax, java.lang.String email, java.lang.String url, java.lang.Long oldAddressId) {
        this.addressId = addressId;
        this.agencyId = agencyId;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.firstNames = firstNames;
        this.lastName = lastName;
        this.title = title;
        this.department = department;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.url = url;
        this.oldAddressId = oldAddressId;
    }

    /** default constructor */
    public Address() {
    }

    /** minimal constructor */
    public Address(java.lang.Long addressId) {
        this.addressId = addressId;
    }

    public java.lang.Long getAddressId() {
        return this.addressId;
    }

    public void setAddressId(java.lang.Long addressId) {
        this.addressId = addressId;
    }

    public java.lang.Long getAgencyId() {
        return this.agencyId;
    }

    public void setAgencyId(java.lang.Long agencyId) {
        this.agencyId = agencyId;
    }

    public java.lang.String getAddress() {
        return this.address;
    }

    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    public java.lang.String getAddress2() {
        return this.address2;
    }

    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }

    public java.lang.String getCity() {
        return this.city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getState() {
        return this.state;
    }

    public void setState(java.lang.String state) {
        this.state = state;
    }

    public java.lang.String getCountry() {
        return this.country;
    }

    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    public java.lang.String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }

    public java.lang.String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = postalCode;
    }

    public java.lang.String getFirstNames() {
        return this.firstNames;
    }

    public void setFirstNames(java.lang.String firstNames) {
        this.firstNames = firstNames;
    }

    public java.lang.String getLastName() {
        return this.lastName;
    }

    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    public java.lang.String getTitle() {
        return this.title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getDepartment() {
        return this.department;
    }

    public void setDepartment(java.lang.String department) {
        this.department = department;
    }

    public java.lang.String getPhone() {
        return this.phone;
    }

    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }

    public java.lang.String getFax() {
        return this.fax;
    }

    public void setFax(java.lang.String fax) {
        this.fax = fax;
    }

    public java.lang.String getEmail() {
        return this.email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getUrl() {
        return this.url;
    }

    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public java.lang.Long getOldAddressId() {
        return this.oldAddressId;
    }

    public void setOldAddressId(java.lang.Long oldAddressId) {
        this.oldAddressId = oldAddressId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("addressId", getAddressId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Address) ) return false;
        Address castOther = (Address) other;
        return this.getAddressId().equals(castOther.getAddressId());
    }

    public int hashCode() {
        return getAddressId().intValue();
    }

}
