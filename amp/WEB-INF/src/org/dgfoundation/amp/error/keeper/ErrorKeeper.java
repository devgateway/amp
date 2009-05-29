package org.dgfoundation.amp.error.keeper;

import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;

public interface ErrorKeeper {
	public void store(Throwable e, ErrorUser user, ErrorScene scene);
}
