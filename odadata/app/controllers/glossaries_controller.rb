class GlossariesController < ApplicationController
  def show
    glossary = I18n.translate("#{params[:model]}.#{params[:method]}", 
                                :scope => [:glossary, :attributes])
    render :text => textilize(glossary)
  end
end
