class UserSessionsController < ApplicationController
  def new
    @user_session = UserSession.new
  end

  def create
    @user_session = UserSession.new(params[:user_session])
    if @user_session.save
      flash[:notice] = lc(:login_success)
      redirect_back_or_default current_user.home_path || '/'
    else
      render :action => :new
    end
  end

  def destroy
    current_user_session.destroy
    flash[:notice] = lc(:logout_success)
    redirect_back_or_default new_user_session_url
  end
end
