/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.PIYesNoAnswerModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;

import java.util.*;

/**
 * @author aartimon@dginternational.org
 * @since Mar 30, 2011
 */
public class AmpPIQuestionItemFeaturePanel extends AmpFeaturePanel<AmpAhsurveyIndicator> {
    

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpPIQuestionItemFeaturePanel(String id, String fmName, final IModel<AmpAhsurveyIndicator> surveyIndicator, final IModel<AmpAhsurvey> survey){
        super(id, surveyIndicator, fmName, true);
        
        if (surveyIndicator.getObject() != null){
            Iterator<AmpAhsurveyQuestion> it = surveyIndicator.getObject().getQuestions().iterator();
            while (it.hasNext()) {
                AmpAhsurveyQuestion qq = (AmpAhsurveyQuestion) it
                        .next();
                qq.getAmpTypeId().getName();
            }
        }
        
        
        final AbstractReadOnlyModel<List<AmpAhsurveyQuestion>> listModel = new AbstractReadOnlyModel<List<AmpAhsurveyQuestion>>() {
            private static final long serialVersionUID = 3706184421459839210L;
            @Override
            public List<AmpAhsurveyQuestion> getObject() {
                Set<AmpAhsurveyQuestion> set = (Set<AmpAhsurveyQuestion>)surveyIndicator.getObject().getQuestions();
                ArrayList<AmpAhsurveyQuestion> list = new ArrayList<AmpAhsurveyQuestion>(set);
                Collections.sort(list, new AmpAhsurveyQuestion.AhsurveyQuestionComparator());
                
                ArrayList<AmpAhsurveyQuestion> listOrderedByParent = new ArrayList<AmpAhsurveyQuestion>();
                
                //now sort the question after their parent, iterate each question, find siblings and move them below                
                for (AmpAhsurveyQuestion parent : list) {
                    if (parent.getParentQuestion() != null)
                        continue;
                    listOrderedByParent.add(parent);

                    for (AmpAhsurveyQuestion sibling : list) 
                        if (parent.equals(sibling.getParentQuestion()))
                            listOrderedByParent.add(sibling);
                    

                }
        
                return listOrderedByParent;
            }
        };
        
        
        ListView<AmpAhsurveyQuestion> list = new ListView<AmpAhsurveyQuestion>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<AmpAhsurveyQuestion> item) {
                
                Set<AmpAhsurveyResponse> responses = survey.getObject().getResponses();
                
                AmpAhsurveyResponse response = null;
                Iterator<AmpAhsurveyResponse> it = responses.iterator();
                while (it.hasNext()) {
                    AmpAhsurveyResponse rs = (AmpAhsurveyResponse) it
                            .next();
                    if (rs.getAmpQuestionId().getAmpQuestionId().compareTo(item.getModelObject().getAmpQuestionId()) == 0){
                        response = rs;
                        break;
                    }
                }
                if (response == null){
                    response = new AmpAhsurveyResponse();
                    response.setAmpAHSurveyId(survey.getObject());
                    response.setAmpQuestionId(item.getModelObject());
                    responses.add(response);
                }
                
                IModel<AmpAhsurveyResponse> responseModel = PersistentObjectModel.getModel(response);
                

                
                
                Label indName = new TrnLabel("qtext", new Model<String>() {
                    PropertyModel<String> pm=new PropertyModel<String>(item.getModelObject(), "questionText");                  
                    @Override
                    public String getObject() {
                        return (item.getModelObject().getParentQuestion()!=null?"- ":"")+pm.getObject();        
                    }                   
                });
                
                
                if(item.getModelObject().getParentQuestion()!=null) {               
                    indName.add(new AttributeModifier("style", "padding-left:4em;font-style:italic"));                  
                }
                                
                item.add(indName);
        

                AmpTextFieldPanel<String> references = new AmpTextFieldPanel<String>("references", new PropertyModel<String>(responseModel, "references"), "References:", false, true);
                item.add(references);
                
                String qtype = item.getModelObject().getAmpTypeId().getName();
                if (qtype.compareTo("yes-no") == 0){
                    final String[] elements = new String[] {"Yes", "No"};
                    RadioChoice<String> answer = new RadioChoice<String>("answer", new PIYesNoAnswerModel(new PropertyModel<String>(responseModel, "response")), Arrays.asList(elements));
                    answer.setSuffix(" ");
                    item.add(answer);
                    
                    TextField hidden = new TextField("answerInput");
                    hidden.setVisible(false);
                    item.add(hidden);
                    
                } else
                    if (qtype.compareTo("calculated") == 0){
                        Label l = new Label("answer", "");
                        item.add(l);
                        TextField hidden = new TextField("answerInput");
                        hidden.setVisible(false);
                        item.add(hidden);
                    } else
                        if (qtype.compareTo("input") == 0){
                            TextField<String> input = new TextField<String>("answerInput", new PropertyModel<String>(responseModel, "response"));
                            item.add(input);
                            WebMarkupContainer hidden = new WebMarkupContainer("answer");
                            hidden.setVisible(false);
                            item.add(hidden);
                        }
            }
        };
        list.setReuseItems(true);
        add(list);
    }
}
