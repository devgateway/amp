package org.dgfoundation.amp.test.translation;

import java.util.Collection;

import junit.framework.TestCase;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.module.translation.entity.MessageGroup;
import org.digijava.module.translation.entity.PatcherMessageGroup;

public class TrnUtilTest extends TestCase {

	private Collection<Message> allMessages = null;
	private Collection<MessageGroup> groups = null;
	private Collection<PatcherMessageGroup> patcherGroups = null;
	
	public TrnUtilTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		allMessages = TrnTestUtil.getMessagesForHashPatch();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		allMessages = null;
	}

	public void testGroupingOfStandardMessage() {
		int expectedSize = 4;
		MessageGroup group1 = null;
		MessageGroup group2 = null;
		MessageGroup group3 = null;
		MessageGroup group4 = null;
		int group1ExpectedSize = 3;
		int group2ExpectedSize = 2;
		int group3ExpectedSize = 2;
		int group4ExpectedSize = 1;
		try {
			//Uses standard MesageGroup bean.
			groups = TrnUtil.groupByKey(allMessages);
			assertNotNull(groups);
			assertEquals(expectedSize, groups.size());
			for (MessageGroup group : groups) {
				if ("key1".equals(group.getKey())){
					group1 = group;
				}
				if ("key2".equals(group.getKey())){
					group2 = group;
				}
				if ("key3".equals(group.getKey())){
					group3 = group;
				}
				if ("key4".equals(group.getKey())){
					group4 = group;
				}
			}
			assertNotNull(group1);
			assertNotNull(group2);
			assertNotNull(group3);
			assertNotNull(group4);
			assertEquals(group1ExpectedSize, group1.getAllMessages().size());
			assertEquals(group2ExpectedSize, group2.getAllMessages().size());
			assertEquals(group3ExpectedSize, group3.getAllMessages().size());
			assertEquals(group4ExpectedSize, group4.getAllMessages().size());
		} catch (DgException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testPatcherGrouping() {
		int expectedSize = 4;
		MessageGroup group1 = null;
		MessageGroup group2 = null;
		MessageGroup group3 = null;
		MessageGroup group4 = null;
		int group1ExpectedSize = 3;
		int group2ExpectedSize = 2;
		int group3ExpectedSize = 2;
		int group4ExpectedSize = 1;
		// Uses standard MesageGroup bean.
		try {
			this.patcherGroups = TrnUtil.groupByKey(allMessages, new TrnUtil.PatcherMessageGroupFactory());
		} catch (DgException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertNotNull(this.patcherGroups);
		assertEquals(expectedSize, this.patcherGroups.size());
		for (MessageGroup group : this.patcherGroups) {
			if ("key1".equals(group.getKey())) {
				group1 = group;
			}
			if ("key2".equals(group.getKey())) {
				group2 = group;
			}
			if ("key3".equals(group.getKey())) {
				group3 = group;
			}
			if ("key4".equals(group.getKey())) {
				group4 = group;
			}
		}
		assertNotNull(group1);
		assertNotNull(group2);
		assertNotNull(group3);
		assertNotNull(group4);
		assertEquals(group1ExpectedSize, group1.getAllMessages().size());
		assertEquals(group2ExpectedSize, group2.getAllMessages().size());
		assertEquals(group3ExpectedSize, group3.getAllMessages().size());
		assertEquals(group4ExpectedSize, group4.getAllMessages().size());
	}
	
	public void testPatcherHashKeyGrouping(){
		int expectedSize = 3;
		String key1 = "key1";
		String key2 = "key2";
		String key3 = "key3";
		String key1hash = TranslatorWorker.generateTrnKey(key1);
		String key2hash = TranslatorWorker.generateTrnKey(key2);
		String key3hash = TranslatorWorker.generateTrnKey(key3);
		//first group
		testPatcherGrouping();
		assertNotNull(this.patcherGroups);
		//Now Remove duplicates
		Collection<PatcherMessageGroup> hashGroups = TrnUtil.removeDuplicateHashCodes(this.patcherGroups);
		assertNotNull(hashGroups);
		assertEquals(expectedSize , hashGroups.size());
	}

}
