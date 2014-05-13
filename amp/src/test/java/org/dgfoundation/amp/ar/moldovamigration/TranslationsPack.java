package org.dgfoundation.amp.ar.moldovamigration;

public class TranslationsPack 
{
	public final long id;
	public final String englishTranslation;
	public final String romanianTranslation;
	
	public String editorId;
	
	public TranslationsPack(Long id, String english, String romanian)
	{
		this(id, english, id, romanian);
	}
	
	public TranslationsPack(Long englishId, String english, Long romanianId, String romanian)
	{
		if (englishId == null)
		{
			this.id = romanianId;
			this.englishTranslation = romanian;
			this.romanianTranslation = romanian;
		}
		else
		{
			// englishId != null
			if (romanianId != null)
			{
				if (romanianId.longValue() != englishId.longValue())
					throw new RuntimeException("romanian Id != english Id!");
			}
			this.id = englishId;
			this.englishTranslation = choose(english, romanian);
			this.romanianTranslation = choose(romanian, english);
		}
	}
	
	public TranslationsPack setEditorKey(String editorKey)
	{	
		this.editorId = editorKey;		
		return this;
	}
	
	public static String choose(String firstOption, String secondOption)
	{
		if (firstOption != null)
			return firstOption;
		return secondOption;
	}
	
	@Override
	public String toString()
	{
		return String.format("(id: %d, editorKey: %s,\n\tenglish: %s\n\tromanian: %s\n", this.id, this.editorId, this.englishTranslation, this.romanianTranslation);
	}
}
