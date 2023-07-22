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


import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "DG_HOW_DID_U_HEAR")
public class HowDidYouHear {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "referral")
    private String referral;

    public HowDidYouHear() {}

    public HowDidYouHear(String id, String referral) {
        this.id = id;
        this.referral = referral;
    }


    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
