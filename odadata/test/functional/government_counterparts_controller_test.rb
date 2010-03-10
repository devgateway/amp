require 'test_helper'

class GovernmentCounterpartsControllerTest < ActionController::TestCase
  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:government_counterparts)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create government_counterpart" do
    assert_difference('GovernmentCounterpart.count') do
      post :create, :government_counterpart => { }
    end

    assert_redirected_to government_counterpart_path(assigns(:government_counterpart))
  end

  test "should show government_counterpart" do
    get :show, :id => government_counterparts(:one).to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => government_counterparts(:one).to_param
    assert_response :success
  end

  test "should update government_counterpart" do
    put :update, :id => government_counterparts(:one).to_param, :government_counterpart => { }
    assert_redirected_to government_counterpart_path(assigns(:government_counterpart))
  end

  test "should destroy government_counterpart" do
    assert_difference('GovernmentCounterpart.count', -1) do
      delete :destroy, :id => government_counterparts(:one).to_param
    end

    assert_redirected_to government_counterparts_path
  end
end
