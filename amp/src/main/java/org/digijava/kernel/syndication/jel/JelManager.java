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

package org.digijava.kernel.syndication.jel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JelManager {
    public JelManager() {
    }

    public static String evaluate(String expression) throws Throwable {
        String ret = null;

        Class[] stLib = new Class[2];
        stLib[0] = java.lang.Math.class;
        stLib[1] = JelStaticMethods.class;

        Class[] dotLib = new Class[3];
        dotLib[0] = java.lang.String.class;
        dotLib[1] = java.lang.Double.class;
        dotLib[2] = gnu.jel.reflect.Double.class;

        gnu.jel.Library lib =
            new gnu.jel.Library(stLib, null, dotLib, null, null);

        gnu.jel.CompiledExpression expr;
        expr = gnu.jel.Evaluator.compile(expression,
                                         lib);
        ret = expr.evaluate(null).toString();

        return ret;
    }

}
