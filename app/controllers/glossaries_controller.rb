class GlossariesController < ApplicationController
  access_rule 'admin', :except => :show
    
  def index
    @glossaries = Glossary.all
  end

  def show
    @glossary = Glossary.find(params[:id])
    render :layout => false
  end
  
  def new
    @models = parse_models
    @glossary = Glossary.new
  end
  
  def create
    @glossary = Glossary.new(params[:glossary])
    if @glossary.save
      redirect_to glossaries_path
    else
      render :action => 'new'
    end
  end  
  
  def edit
    @models = parse_models
    @glossary = Glossary.find(params[:id])
  end
  
  def update
    @glossary = Glossary.find(params[:id])
    if @glossary.update_attributes(params[:glossary])
        flash[:notice] = 'Glossary was successfully updated.'
        redirect_to glossaries_path
      else
        render :action => 'edit'
      end
  end
  
protected
  def parse_models
    files = Dir["#{RAILS_ROOT}/app/models/**/*.rb"]
    files.map { |m| File.basename(m).sub('.rb', '').classify }
  end
end
