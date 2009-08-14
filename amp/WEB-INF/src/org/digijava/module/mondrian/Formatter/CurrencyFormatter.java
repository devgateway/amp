package org.digijava.module.mondrian.Formatter;

import org.digijava.module.aim.helper.FormatHelper;

import mondrian.olap.Member;

public class CurrencyFormatter implements mondrian.olap.MemberFormatter {

	@Override
	public String formatMember(Member member) {
	
	return FormatHelper.formatNumber(new Double(member.getUniqueName().substring(member.getParentMember().toString().length()+2,member.getUniqueName().length()-1)));
		
	}
}
