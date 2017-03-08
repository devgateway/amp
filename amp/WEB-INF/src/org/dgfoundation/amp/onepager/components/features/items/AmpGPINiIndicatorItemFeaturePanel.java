package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPINiIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;

/**
 * @author Viorel Chihai
 * @since Mar 02, 2017
 */
public class AmpGPINiIndicatorItemFeaturePanel extends AmpFeaturePanel<AmpGPINiIndicator> {

	private static final long serialVersionUID = 3285773837906913394L;

	public AmpGPINiIndicatorItemFeaturePanel(String id, String fmName, final IModel<AmpGPINiIndicator> indicator) {
		super(id, indicator, fmName, true);

		Label indicatorNameLabel = new TrnLabel("indicatorName", new PropertyModel<String>(indicator, "name"));
		add(indicatorNameLabel);
		
		final AbstractReadOnlyModel<List<AmpGPINiQuestion>> listModel = new AbstractReadOnlyModel<List<AmpGPINiQuestion>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpGPINiQuestion> getObject() {
				Set<AmpGPINiQuestion> questions = (Set<AmpGPINiQuestion>) indicator.getObject().getQuestions();
				List<AmpGPINiQuestion> list = questions.stream()
						.filter(q -> q.getRequiresDataEntry())
						.collect(Collectors.toList());
				
				Collections.sort(list, new AmpGPINiQuestion.GPINiQuestionComparator());

				return list;
			}
		};
		
		ListView<AmpGPINiQuestion> list = new ListView<AmpGPINiQuestion>("listQuestions", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpGPINiQuestion> item) {
				item.add(new AmpGPINiQuestionItemFeaturePanel("questionItem", item.getModel()));
			}
		};
		
		list.setReuseItems(true);
		add(list);
		
	}
}
