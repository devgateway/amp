require File.dirname(__FILE__) + '/../test_helper'
require 'bluebook_controller'

# Re-raise errors caught by the controller.
class BluebookController; def rescue_action(e) raise e end; end

class BluebookControllerTest < Test::Unit::TestCase

  #fixtures :data

  def setup
    @controller = BluebookController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  # TODO Replace this with your actual tests
  def test_show
    get :show
    assert_response :success
    assert_equal 'image/png', @response.headers['Content-Type']
  end
  
end
