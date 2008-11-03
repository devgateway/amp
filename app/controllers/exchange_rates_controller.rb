class ExchangeRatesController < ApplicationController
  access_rule 'admin', :except => :index
  
  def index
    @exchange_rates = ExchangeRate.find(:all).group_by(&:year).sort
  end
  
  def new
    @exchange_rate = ExchangeRate.new
  end
    
  def create
    @exchange_rate = ExchangeRate.new(params[:exchange_rate])
  
    if @exchange_rate.save
      flash[:notice] = 'Exchange Rate was successfully created.'
      redirect_to exchange_rates_path
    else
      flash[:error] = 'There was an error creating an Exchange Rate, please check next to the fields for more information'
      render :action => 'new'
    end
  end
    
  def edit
    @exchange_rate = ExchangeRate.find(params[:id])
  end  
  
  def update
    @exchange_rate = ExchangeRate.find(params[:id])
    if @exchange_rate.update_attributes(params[:exchange_rate]) 
      flash[:notice] = 'Exchange Rate was successfully updated.'
      redirect_to exchange_rates_path
    else
      flash[:error] = 'There was an error editing the Exchange Rate, please check next to the fields for more information'
      render :action => 'edit'
    end
  end
end