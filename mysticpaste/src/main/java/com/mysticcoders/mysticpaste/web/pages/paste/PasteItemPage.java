package com.mysticcoders.mysticpaste.web.pages.paste;

import com.mysticcoders.mysticpaste.model.LanguageSyntax;
import com.mysticcoders.mysticpaste.model.PasteItem;
import com.mysticcoders.mysticpaste.services.InvalidClientException;
import com.mysticcoders.mysticpaste.services.PasteService;
import com.mysticcoders.mysticpaste.web.components.highlighter.HighlighterPanel;
import com.mysticcoders.mysticpaste.web.pages.BasePage;
import com.mysticcoders.mysticpaste.web.pages.view.ViewPrivatePage;
import com.mysticcoders.mysticpaste.web.pages.view.ViewPublicPage;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Paste Item page.
 *
 * @author Steve Forsyth
 *         Date: Mar 8, 2009
 */
public class PasteItemPage extends BasePage {

    @SpringBean
    PasteService pasteService;

    public PasteItemPage() {
        super(PasteItemPage.class);
        add(new FeedbackPanel("feedback"));
        add(new PasteForm("pasteForm", new CompoundPropertyModel<PasteItem>(new PasteItem())));
    }

    private String spamEmail;

    public String getSpamEmail() {
        return spamEmail;
    }

    public void setSpamEmail(String spamEmail) {
        this.spamEmail = spamEmail;
    }


    public class PasteForm extends Form<PasteItem> {

        private boolean twitter = true;
        private LanguageSyntax languageType = HighlighterPanel.getLanguageSyntax("plain");

        public LanguageSyntax getLanguageType() {
            return languageType;
        }

        public void setLanguageType(LanguageSyntax languageType) {
            this.languageType = languageType;
        }

        public boolean isTwitter() {
            return twitter;
        }

        public void setTwitter(boolean twitter) {
            this.twitter = twitter;
        }

        public PasteForm(String id, IModel<PasteItem> model) {
            super(id, model);
            add(new CheckBox("private"));
            add(new CheckBox("twitter", new PropertyModel<Boolean>(PasteForm.this, "twitter")));

            DropDownChoice languageDDC = new DropDownChoice<LanguageSyntax>("type",
                    new PropertyModel<LanguageSyntax>(PasteForm.this, "languageType"),
                    HighlighterPanel.getLanguageSyntaxList(), new IChoiceRenderer<LanguageSyntax>() {
                        public String getDisplayValue(LanguageSyntax syntax) {
                            return syntax.getName();
                        }

                        public String getIdValue(LanguageSyntax languageSyntax, int index) {
                            return languageSyntax.getLanguage();
                        }

                    });
            add(languageDDC);
            add(new TextArea("content"));
            add(new TextField<String>("email", new PropertyModel<String>(PasteItemPage.this, "spamEmail")));
        }

        @Override
        protected void onSubmit() {
            PasteItem pasteItem = PasteForm.this.getModelObject();
            if (pasteItem.getContent() == null || pasteItem.getContent().equals("")) {
                error("Paste content is required!");
                return;
            }

            if (getSpamEmail() != null || hasSpamKeywords(pasteItem.getContent())) {
                error("Spam Spam Spam Spam");
                return;
            }

            pasteItem.setType(getLanguageType() != null ? getLanguageType().getLanguage() : "text");

            try {
                pasteService.createItem("web", pasteItem, twitter);
                PageParameters params = new PageParameters();
                if (pasteItem.isPrivate()) {
                    this.setRedirect(true);
                    params.put("0", pasteItem.getPrivateToken());
                    setResponsePage(ViewPrivatePage.class, params);
                } else {
                    this.setRedirect(true);
                    params.put("0", Long.toString(pasteItem.getId()));
                    setResponsePage(ViewPublicPage.class, params);
                }
            } catch (InvalidClientException e) {
                // Do nothing at this point as we haven't defined what an "Invalid Client" is.
                e.printStackTrace();
            }
        }

        public boolean hasSpamKeywords(String content) {
            String lowercasedContent = content.toLowerCase();

            for (String badWord : badWords) {
                if (lowercasedContent.indexOf(badWord) != -1) return true;
            }

            return false;
        }

        private String[] badWords = new String[]{
                "[/URL]",
                "[/url]",
                "adipex",
                "adultfriendfinder",
                "adult-dvd",
                "adult-friend-finder",
                "adult-personal",
                "adult personal",
                "adult-stories",
                "adult friends",
                "boob",
                "casino",
                "cheap hotel",
                "cialis",
                "classified ad",
                "diazepam",
                "diazepan",
                "fiksa.org",
                "forexcurrency",
                "free ringtones",
                "fuck",
                "gay porn",
                "geo.ya",
                "httpgeo",
                "hot sex",
                "hydroconone",
                "incest",
                "inderal",
                "insulin",
                "jewish dating",
                "keflex",
                "klonopin",
                "lamictal",
                "lasix",
                "levaquin",
                "levitra",
                "lipitor",
                "male porn",
                "malenhancement",
                "masya",
                "mature porn",
                "milf",
                "murphy bed",
                "nude celebrity",
                "oxycodone",
                "paxil",
                "payday",
                "phenergan",
                "phentermine",
                "poker",
                "porn link",
                "porn video",
                "porno portal",
                "pornmaster",
                "premarin",
                "prozac",
                "rape",
                "strattera",
                "tramadol",
                "tussionex",
                "valium",
                "viagra",
                "vicodin",
                "web gratis",
                "without prescription",
                "xanax",
                "xxx ",
                " xxx",
                "xxxvideo",
                "youradult",
                "zelnorm",
                "zenegra",
                "megaupload.com",
                "members.lycos.co.uk",
                "lix.in"
        };
    }
}