class GovernmentCounterpartsController < ApplicationController
  access_rule 'admin'

  # GET /government_counterparts
  # GET /government_counterparts.xml
  def index
    @government_counterparts = GovernmentCounterpart.ordered.paginate(:all, :page => params[:page], :per_page => 30)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @government_counterparts }
    end
  end

  # GET /government_counterparts/1
  # GET /government_counterparts/1.xml
  def show
    @government_counterpart = GovernmentCounterpart.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @government_counterpart }
    end
  end

  # GET /government_counterparts/new
  # GET /government_counterparts/new.xml
  def new
    @government_counterpart = GovernmentCounterpart.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @government_counterpart }
    end
  end

  # GET /government_counterparts/1/edit
  def edit
    @government_counterpart = GovernmentCounterpart.find(params[:id])
  end

  # POST /government_counterparts
  # POST /government_counterparts.xml
  def create
    @government_counterpart = GovernmentCounterpart.new(params[:government_counterpart])

    respond_to do |format|
      if @government_counterpart.save
        flash[:notice] = 'GovernmentCounterpart was successfully created.'
        format.html { redirect_to(@government_counterpart) }
        format.xml  { render :xml => @government_counterpart, :status => :created, :location => @government_counterpart }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @government_counterpart.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /government_counterparts/1
  # PUT /government_counterparts/1.xml
  def update
    @government_counterpart = GovernmentCounterpart.find(params[:id])

    respond_to do |format|
      if @government_counterpart.update_attributes(params[:government_counterpart])
        flash[:notice] = 'GovernmentCounterpart was successfully updated.'
        format.html { redirect_to(@government_counterpart) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @government_counterpart.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /government_counterparts/1
  # DELETE /government_counterparts/1.xml
  def destroy
    @government_counterpart = GovernmentCounterpart.find(params[:id])
    @government_counterpart.destroy

    respond_to do |format|
      format.html { redirect_to(government_counterparts_url) }
      format.xml  { head :ok }
    end
  end
end
